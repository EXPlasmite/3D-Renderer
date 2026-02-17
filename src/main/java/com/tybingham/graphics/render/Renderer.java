package com.tybingham.graphics.render;

import com.tybingham.graphics.app.Window;
import com.tybingham.graphics.math.Mat4;
import com.tybingham.graphics.scene.DemoScene;
import com.tybingham.graphics.scene.Scene;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    private int vao;
    private int vbo;
    private int ebo;

    private ShaderProgram shader;
    private int uMvpLoc;

    public void init() {
        // Cube vertices: position + color (RGB)
        float[] vertices = {
                // Positions          // Colours
                -0.5f,-0.5f,-0.5f,    1f,0f,0f,
                 0.5f,-0.5f,-0.5f,    0f,1f,0f,
                 0.5f, 0.5f,-0.5f,    0f,0f,1f,
                -0.5f, 0.5f,-0.5f,    1f,1f,0f,

                -0.5f,-0.5f, 0.5f,    1f,0f,1f,
                 0.5f,-0.5f, 0.5f,    0f,1f,1f,
                 0.5f, 0.5f, 0.5f,    1f,1f,1f,
                -0.5f, 0.5f, 0.5f,    0.3f,0.7f,0.9f
        };

        int[] indices = {
                // Back face
                0,1,2, 2,3,0,
                // Front face
                4,5,6, 6,7,4,
                // Left
                4,7,3, 3,0,4,
                // Right
                1,5,6, 6,2,1,
                // Bottom
                4,5,1, 1,0,4,
                // Top
                3,2,6, 6,7,3
        };

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        int stride = 6 * Float.BYTES;
        // Position (location=0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);
        // Colour (location=1)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 3L * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);

        String vert = Utils.readResource("shaders/basic.vert");
        String frag = Utils.readResource("shaders/basic.frag");
        shader = new ShaderProgram(vert, frag);
        uMvpLoc = shader.getUniformLocation("uMVP");
    }

    public void render(Scene scene, Camera camera, Window window) {
        if (!(scene instanceof DemoScene demo)) {
            return; // For now only handle DemoScene
        }

        glEnable(GL_DEPTH_TEST);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        float aspect = (float) window.getWidth() / (float) window.getHeight();
        Mat4 proj = Mat4.perspective((float)Math.toRadians(60f), aspect, 0.1f, 100f);
        Mat4 view = camera.getViewMatrix();
        Mat4 model = Mat4.rotateY(demo.getAngle());

        Mat4 mvp = Mat4.mul(proj, Mat4.mul(view, model));

        shader.bind();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            fb.put(mvp.m).flip();
            glUniformMatrix4fv(uMvpLoc, false, fb);
        }

        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        shader.unbind();
    }

    public void destroy() {
        if (shader != null) shader.destroy();
        if (ebo != 0) glDeleteBuffers(ebo);
        if (vbo != 0) glDeleteBuffers(vbo);
        if (vao != 0) glDeleteVertexArrays(vao);
    }
}