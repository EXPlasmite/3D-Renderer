package com.tybingham.graphics.app;

import org.lwjgl.glfw.GLFW;

public class Input {
    private final long window;

    private double lastMouseX, lastMouseY;
    private float deltaX, deltaY;
    private boolean firstMouse = true;

    private boolean rWasDown = false;

    public Input(long windowHandle) {
        this.window = windowHandle;

        // Hide + lock cursor for FPS-style camera
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        GLFW.glfwSetCursorPosCallback(window, (w, xpos, ypos) -> {
            if (firstMouse) {
                lastMouseX = xpos;
                lastMouseY = ypos;
                firstMouse = false;
            }

            deltaX += (float)(xpos - lastMouseX);
            deltaY += (float)(lastMouseY - ypos); // Inverted so moving mouse up looks up

            lastMouseX = xpos;
            lastMouseY = ypos;
        });

    }

    public boolean keyDown(int key) {
        return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
    }

    public boolean keyPressedOnce(int key) {
        return pressedOnceInternal(key);
    }

    public float consumeMouseDX() {
        float v = deltaX;
        deltaX = 0;
        return v;
    }

    public float consumeMouseDY() {
        float v = deltaY;
        deltaY = 0;
        return v;
    }

    public void resetMouse() {
        firstMouse = true;
        deltaX = deltaY = 0;
    }

    private boolean pressedOnceInternal(int key) {
        boolean down = keyDown(key);

        if (key == GLFW.GLFW_KEY_R) {
            boolean pressed = down && !rWasDown;
            rWasDown = down;
            return pressed;
        }

        return false;
    }
}