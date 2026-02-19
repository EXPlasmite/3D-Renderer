package com.tybingham.graphics.app;

import org.lwjgl.glfw.GLFW;

public class Input {
    private final long window;

    private double lastMouseX, lastMouseY;
    private float deltaX, deltaY;
    private boolean firstMouse = true;

    // Key edge detection
    private final boolean[] wasDown = new boolean[GLFW.GLFW_KEY_LAST + 1];

    // Scroll wheel
    private float scrollY = 0f;

    public Input(long windowHandle) {
        this.window = windowHandle;

        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        GLFW.glfwSetCursorPosCallback(window, (w, xpos, ypos) -> {
            if (firstMouse) {
                lastMouseX = xpos;
                lastMouseY = ypos;
                firstMouse = false;
            }
            deltaX += (float)(xpos - lastMouseX);
            deltaY += (float)(lastMouseY - ypos);

            lastMouseX = xpos;
            lastMouseY = ypos;
        });

        GLFW.glfwSetScrollCallback(window, (w, xoff, yoff) -> {
            scrollY += (float) yoff;
        });
    }

    public boolean keyDown(int key) {
        return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
    }

    public boolean keyPressedOnce(int key) {
        boolean down = keyDown(key);
        boolean pressed = down && !wasDown[key];
        wasDown[key] = down;
        return pressed;
    }

    public float consumeMouseDX() {
        float v = deltaX; deltaX = 0; return v;
    }

    public float consumeMouseDY() {
        float v = deltaY; deltaY = 0; return v;
    }

    public float consumeScrollY() {
        float v = scrollY; scrollY = 0; return v;
    }

    public void resetMouse() {
        firstMouse = true;
        deltaX = deltaY = 0;
    }
}