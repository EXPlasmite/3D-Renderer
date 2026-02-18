package com.tybingham.graphics.render;

import com.tybingham.graphics.app.Window;
import com.tybingham.graphics.math.Mat4;
import com.tybingham.graphics.scene.DemoScene;
import com.tybingham.graphics.scene.Scene;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Renderer {
    private ShaderProgram shader;
    private int uMvpLoc;

    public void init() {
        String vert = Utils.readResource("shaders/basic.vert");
        String frag = Utils.readResource("shaders/basic.frag");
        shader = new ShaderProgram(vert, frag);
        uMvpLoc = shader.getUniformLocation("uMVP");
    }

    public void render(Scene scene, Camera camera, Window window) {
        if (!(scene instanceof DemoScene demo)) return;

        glEnable(GL_DEPTH_TEST);

        float aspect = (float) window.getWidth() / (float) window.getHeight();
        Mat4 proj = Mat4.perspective((float)Math.toRadians(60f), aspect, 0.1f, 100f);
        Mat4 view = camera.getViewMatrix();

        shader.bind();

        for (var obj : demo.getObjects()) {
            if (obj.mesh == null) continue;

            Mat4 model = obj.transform.modelMatrix();
            Mat4 mvp = Mat4.mul(proj, Mat4.mul(view, model));

            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer fb = stack.mallocFloat(16);
                fb.put(mvp.m).flip();
                glUniformMatrix4fv(uMvpLoc, false, fb);
            }

            obj.mesh.draw();
        }

        shader.unbind();
    }

    public void destroy() {
        if (shader != null) shader.destroy();
    }
}