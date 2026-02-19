package com.tybingham.graphics.render;

import com.tybingham.graphics.app.Window;
import org.lwjgl.stb.STBEasyFont;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Simple on-screen text overlay for OpenGL 3.3 CORE using STBEasyFont.
 * STBEasyFont outputs quads - Convert to triangles (because GL_QUADS is not in core profile).
 */

public class UiRenderer {

    private static final int VERT_STRIDE_BYTES = 16;

    private static final int MAX_QUADS = 20000;

    private static final int MAX_VERTS_QUAD = MAX_QUADS * 4;
    private static final int MAX_VERTS_TRI  = MAX_QUADS * 6;

    private int vao, vbo;
    private ShaderProgram shader;
    private int uOrthoLoc;

    private ByteBuffer quadBuffer; // STBEasyFont output (quads)
    private ByteBuffer triBuffer;  // Converted triangles

    private int uScaleLoc;
    private float uiScale = 1.5f; // tweak: 1.5–2.5 usually good

    public void init() {
        // Buffers sized for worst-case text usage
        quadBuffer = MemoryUtil.memAlloc(MAX_VERTS_QUAD * VERT_STRIDE_BYTES);
        triBuffer  = MemoryUtil.memAlloc(MAX_VERTS_TRI  * VERT_STRIDE_BYTES);

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, (long) triBuffer.capacity(), GL_DYNAMIC_DRAW);

        // aPos (vec2) at offset 0  (x,y)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, VERT_STRIDE_BYTES, 0L);
        glEnableVertexAttribArray(0);

        // aCol (rgba bytes) at offset 8
        glVertexAttribPointer(1, 4, GL_UNSIGNED_BYTE, true, VERT_STRIDE_BYTES, 8L);
        glEnableVertexAttribArray(1);


        glBindVertexArray(0);

        shader = new ShaderProgram(
                Utils.readResource("shaders/ui.vert"),
                Utils.readResource("shaders/ui.frag")
        );
        uOrthoLoc = shader.getUniformLocation("uOrtho");
        uScaleLoc = shader.getUniformLocation("uScale");
    }

    public void render(Window window, Renderer renderer) {
        // Build the lines to display
        List<String> lines = buildOverlayLines(renderer);

       quadBuffer.clear();

        float x = 12f;
        float y = 30f;
        float lineH = 30f;

        int totalQuads = 0;

        for (String s : lines) {
            if (s == null) continue;

            // Write at current buffer position
            int startPos = quadBuffer.position();

            int quads = STBEasyFont.stb_easy_font_print(x, y, s, null, quadBuffer);
            totalQuads += quads;

            // Advance position manually (4 verts per quad, 16 bytes per vert)
            int bytesWritten = quads * 4 * VERT_STRIDE_BYTES;
            quadBuffer.position(startPos + bytesWritten);

            y += lineH;

            // Safety: stop before overflow
            if (quadBuffer.remaining() < 4 * VERT_STRIDE_BYTES) break;
        }

        // Limit buffer to only what was written
        quadBuffer.flip();

        // Convert only the quads we actually wrote
        triBuffer.clear();
        int triVerts = quadsToTriangles(quadBuffer, totalQuads, triBuffer);


        boolean depthWasEnabled = glIsEnabled(GL_DEPTH_TEST);
        boolean cullWasEnabled  = glIsEnabled(GL_CULL_FACE);

        // Upload + draw
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        shader.bind();

        // Set ortho matrix (pixel space -> NDC)
        // NDC: x [-1..1], y [-1..1]. Origin top-left.
        float w = window.getWidth();
        float h = window.getHeight();
        float[] ortho = ortho(0, w, h, 0);
        glUniformMatrix4fv(uOrthoLoc, false, ortho);
        glUniform1f(uScaleLoc, uiScale);

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, triBuffer);

        glDrawArrays(GL_TRIANGLES, 0, triVerts);

        glBindVertexArray(0);
        shader.unbind();

        glDisable(GL_BLEND);

        if (depthWasEnabled) glEnable(GL_DEPTH_TEST);
        if (cullWasEnabled)  glEnable(GL_CULL_FACE);
    }

    private List<String> buildOverlayLines(Renderer renderer) {
        int mode = renderer.getLightMode(); // -1 off, 0 sun, 1 point, 2 rainbow
        float intensity = renderer.getLightIntensity();

        String sunState     = (mode == 0) ? "ON" : "OFF";
        String pointState   = (mode == 1) ? "ON" : "OFF";
        String rainbowState = (mode == 2) ? "ON" : "OFF";

        List<String> lines = new ArrayList<>();
        lines.add("=== Controls ===");
        lines.add("Lighting (press same key again to toggle ON/OFF):");
        lines.add("  [1] Sunlight (Directional): " + sunState);
        lines.add("  [2] Point Light (Moving): " + pointState);
        lines.add("  [3] Rainbow Point Light: " + rainbowState);
        lines.add("  Mouse Wheel: Intensity (Point / Rainbow)");
        lines.add("  Current Intensity: " + String.format("%.2f", intensity));
        lines.add("");
        lines.add("Camera:");
        lines.add("  W/A/S/D: Move | Mouse: Look");
        lines.add("  SPACE: Up | LSHIFT: Down");
        lines.add("  LCTRL: Sprint | R: Reset Camera");
        lines.add("  ESC: Quit");
        return lines;
    }

    /**
     * Converts STBEasyFont quad vertices to triangles:
     * For each quad (v0,v1,v2,v3) -> (v0,v1,v2) + (v0,v2,v3)
     */

    private int quadsToTriangles(ByteBuffer quadBuf, int quadCount, ByteBuffer outTriBuf) {
        // STBEasyFont wrote into quadBuf at position 0..N bytes
        // Each quad = 4 vertices. Each vertex = 16 bytes.
        int vertsOut = 0;

        // Indices in bytes
        for (int q = 0; q < quadCount; q++) {
            int base = q * 4 * VERT_STRIDE_BYTES;

            // v0,v1,v2,v3 byte offsets
            int v0 = base;
            int v1 = base + 1 * VERT_STRIDE_BYTES;
            int v2 = base + 2 * VERT_STRIDE_BYTES;
            int v3 = base + 3 * VERT_STRIDE_BYTES;

            // tri1: v0 v1 v2
            copyVertex(quadBuf, v0, outTriBuf);
            copyVertex(quadBuf, v1, outTriBuf);
            copyVertex(quadBuf, v2, outTriBuf);

            // tri2: v0 v2 v3
            copyVertex(quadBuf, v0, outTriBuf);
            copyVertex(quadBuf, v2, outTriBuf);
            copyVertex(quadBuf, v3, outTriBuf);

            vertsOut += 6;
        }

        outTriBuf.flip();
        return vertsOut;
    }

    private void copyVertex(ByteBuffer src, int srcPosBytes, ByteBuffer dst) {
        // Copy 16 bytes
        for (int i = 0; i < VERT_STRIDE_BYTES; i++) {
            dst.put(src.get(srcPosBytes + i));
        }
    }

    /**
     * Column-major ortho matrix for OpenGL
     */

    private float[] ortho(float left, float right, float bottom, float top) {
        float[] m = new float[16];

        // Identity
        m[0] = 1f; m[5] = 1f; m[10] = 1f; m[15] = 1f;

        m[0]  =  2f / (right - left);
        m[5]  =  2f / (top - bottom);
        m[10] = -1f;
        m[12] = -(right + left) / (right - left);
        m[13] = -(top + bottom) / (top - bottom);

        return m;
    }

    public void destroy() {
        if (shader != null) shader.destroy();
        if (vbo != 0) glDeleteBuffers(vbo);
        if (vao != 0) glDeleteVertexArrays(vao);

        if (quadBuffer != null) MemoryUtil.memFree(quadBuffer);
        if (triBuffer != null) MemoryUtil.memFree(triBuffer);
    }
}