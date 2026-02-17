package com.tybingham.graphics.app;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private final String title;
    private final int width;
    private final int height;

    private long handle;

    public Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        handle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (handle == 0) throw new RuntimeException("Failed to create GLFW window");

        GLFW.glfwMakeContextCurrent(handle);
        GLFW.glfwSwapInterval(1); // vsync
        GLFW.glfwShowWindow(handle);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);

        glViewport(0, 0, width, height);

        // Resize callback
        GLFW.glfwSetFramebufferSizeCallback(handle, (w, newW, newH) -> {
            glViewport(0, 0, newW, newH);
        });

        // ESC to close
        GLFW.glfwSetKeyCallback(handle, (w, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                GLFW.glfwSetWindowShouldClose(handle, true);
            }
        });
    }

    public void update() {
        GLFW.glfwPollEvents();
    }

    public void render() {
        glClearColor(0.08f, 0.09f, 0.12f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        GLFW.glfwSwapBuffers(handle);
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void destroy() {
        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null);
    }

    public long getHandle() {
        return handle;
    }
}