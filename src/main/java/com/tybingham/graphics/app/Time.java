package com.tybingham.graphics.app;

import org.lwjgl.glfw.GLFW;

public final class Time {
    private static double last;
    private static float dt;

    private Time() {}

    public static void init() {
        last = GLFW.glfwGetTime();
        dt = 0f;
    }

    public static void update() {
        double now = GLFW.glfwGetTime();
        dt = (float)(now - last);
        last = now;

        // Safety clamp
        if (dt > 0.1f) dt = 0.1f;
        if (dt < 0f) dt = 0f;
    }

    public static float dt() {
        return dt;
    }

    public static float deltaTime() {
        return dt;
    }
}
