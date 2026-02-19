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
    private int uLightModeLoc;
    private int uPointLightPosLoc;
    private int uLightIntensityLoc;
    private int lightMode = -1; // -1 = OFF, 0 = Sun, 1 = Point, 2 = Rainbow
    public void toggleLightMode(int mode) {
        lightMode = (lightMode == mode) ? -1 : mode;
    }
    private float lightIntensity = 1.0f;


    private static Vec3 hsvToRgb(float h, float s, float v) {
        float c = v * s;
        float x = c * (1 - Math.abs((h * 6) % 2 - 1));
        float m = v - c;

        float r=0,g=0,b=0;
        float hh = h * 6f;

        if (hh < 1) { r=c; g=x; }
        else if (hh < 2) { r=x; g=c; }
        else if (hh < 3) { g=c; b=x; }
        else if (hh < 4) { g=x; b=c; }
        else if (hh < 5) { r=x; b=c; }
        else { r=c; b=x; }

        return new Vec3(r+m, g+m, b+m);
    }

    public void init() {
        String vert = Utils.readResource("shaders/basic.vert");
        String frag = Utils.readResource("shaders/basic.frag");
        shader = new ShaderProgram(vert, frag);

        uMvpLoc       = shader.getUniformLocation("uMVP");
        uModelLoc     = shader.getUniformLocation("uModel");
        uLightDirLoc  = shader.getUniformLocation("uLightDir");
        uLightColorLoc = shader.getUniformLocation("uLightColor");
        uViewPosLoc   = shader.getUniformLocation("uViewPos");

        uLightModeLoc      = shader.getUniformLocation("uLightMode");
        uPointLightPosLoc  = shader.getUniformLocation("uPointLightPos");
        uLightIntensityLoc = shader.getUniformLocation("uLightIntensity");
    }

    public void render(Scene scene, Camera camera, Window window) {
        if (!(scene instanceof DemoScene demo)) return;

        glEnable(GL_DEPTH_TEST);

        float aspect = (float) window.getWidth() / (float) window.getHeight();
        Mat4 proj = Mat4.perspective((float) Math.toRadians(60f), aspect, 0.1f, 100f);
        Mat4 view = camera.getViewMatrix();

        // Simple sunlight direction + white light
        Vec3 sunDir = new Vec3(-0.2f, -1.0f, -0.3f);

        float t = (float) org.lwjgl.glfw.GLFW.glfwGetTime();
        Vec3 pointPos = new Vec3((float)Math.sin(t) * 3.0f, 2.0f, 0.0f);

        Vec3 lightColor = new Vec3(1f,1f,1f);
        if (lightMode == 2) {
            float h = (t * 0.1f) % 1.0f;
            lightColor = hsvToRgb(h, 1.0f, 1.0f);
        }

        shader.bind();

        float effectiveIntensity = (lightMode == -1) ? 0.0f : lightIntensity;
        glUniform1f(uLightIntensityLoc, effectiveIntensity);
        glUniform1i(uLightModeLoc, lightMode); // shader must handle -1

        // Set per-frame uniforms (once)
        glUniform1i(uLightModeLoc, lightMode);
        glUniform3f(uLightDirLoc, sunDir.x, sunDir.y, sunDir.z);
        glUniform3f(uPointLightPosLoc, pointPos.x, pointPos.y, pointPos.z);

        glUniform3f(uLightColorLoc, lightColor.x, lightColor.y, lightColor.z);
        glUniform1f(uLightIntensityLoc, lightIntensity);

        Vec3 camPos = camera.getPosition();
        glUniform3f(uViewPosLoc, camPos.x, camPos.y, camPos.z);

        // Draw all objects
        for (var obj : demo.getObjects()) {
            if (obj.mesh == null) continue;

            Mat4 model = obj.transform.modelMatrix();
            Mat4 mvp = Mat4.mul(proj, Mat4.mul(view, model));

            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer fb = stack.mallocFloat(16);

                fb.put(mvp.m).flip();
                glUniformMatrix4fv(uMvpLoc, false, fb);

                fb.clear();
                fb.put(model.m).flip();
                glUniformMatrix4fv(uModelLoc, false, fb);
            }

            obj.mesh.draw();
        }

        shader.unbind();
    }

    public void setLightMode(int mode) { this.lightMode = mode; }
    public int getLightMode() { return lightMode; }

    public void addIntensity(float delta) {
        lightIntensity = Math.max(0.1f, Math.min(5.0f, lightIntensity + delta));
    }

    public void destroy() {
        if (shader != null) shader.destroy();
    }
}