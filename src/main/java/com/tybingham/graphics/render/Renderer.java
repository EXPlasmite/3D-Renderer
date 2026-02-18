package com.tybingham.graphics.render;

import com.tybingham.graphics.app.Window;
import com.tybingham.graphics.math.Mat4;
import com.tybingham.graphics.math.Vec3;
import com.tybingham.graphics.scene.DemoScene;
import com.tybingham.graphics.scene.Scene;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Renderer {
    private ShaderProgram shader;

    // Uniform locations
    private int uMvpLoc;
    private int uModelLoc;
    private int uLightDirLoc;
    private int uLightColorLoc;
    private int uViewPosLoc;

    public void init() {
        String vert = Utils.readResource("shaders/basic.vert");
        String frag = Utils.readResource("shaders/basic.frag");
        shader = new ShaderProgram(vert, frag);

        uMvpLoc       = shader.getUniformLocation("uMVP");
        uModelLoc     = shader.getUniformLocation("uModel");
        uLightDirLoc  = shader.getUniformLocation("uLightDir");
        uLightColorLoc = shader.getUniformLocation("uLightColour");
        uViewPosLoc   = shader.getUniformLocation("uViewPos");
    }

    public void render(Scene scene, Camera camera, Window window) {
        if (!(scene instanceof DemoScene demo)) return;

        glEnable(GL_DEPTH_TEST);

        float aspect = (float) window.getWidth() / (float) window.getHeight();
        Mat4 proj = Mat4.perspective((float) Math.toRadians(60f), aspect, 0.1f, 100f);
        Mat4 view = camera.getViewMatrix();

        // Simple sunlight direction + white light
        Vec3 lightDir = new Vec3(-0.2f, -1.0f, -0.3f);
        Vec3 lightColor = new Vec3(1.0f, 1.0f, 1.0f);

        shader.bind();

        // Set per-frame uniforms (once)
        glUniform3f(uLightDirLoc, lightDir.x, lightDir.y, lightDir.z);
        glUniform3f(uLightColorLoc, lightColor.x, lightColor.y, lightColor.z);

        Vec3 camPos = camera.getPosition();
        glUniform3f(uViewPosLoc, camPos.x, camPos.y, camPos.z);

        // Draw all objects
        for (var obj : demo.getObjects()) {
            if (obj.mesh == null) continue;

            Mat4 model = obj.transform.modelMatrix();
            Mat4 mvp = Mat4.mul(proj, Mat4.mul(view, model));

            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer fbMvp = stack.mallocFloat(16);
                fbMvp.put(mvp.m).flip();
                glUniformMatrix4fv(uMvpLoc, false, fbMvp);

                FloatBuffer fbModel = stack.mallocFloat(16);
                fbModel.put(model.m).flip();
                glUniformMatrix4fv(uModelLoc, false, fbModel);
            }

            obj.mesh.draw();
        }

        shader.unbind();
    }

    public void destroy() {
        if (shader != null) shader.destroy();
    }
}