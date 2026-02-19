package com.tybingham.graphics.app;

import org.lwjgl.glfw.GLFW;

import com.tybingham.graphics.render.Camera;
import com.tybingham.graphics.render.Renderer;
import com.tybingham.graphics.render.UiRenderer;
import com.tybingham.graphics.scene.DemoScene;
import com.tybingham.graphics.scene.Scene;

public class App {
    private Window window;
    private Renderer renderer;
    private Scene scene;
    private Camera camera;

    public void run() {
        window = new Window("3D Renderer", 1280, 720);
        window.init();

        renderer = new Renderer();
        renderer.init();

        camera = new Camera();
        camera.setPosition(0f, 0f, 3f);

        scene = new DemoScene();
        scene.init();

        Time.init();

        UiRenderer ui = new UiRenderer();
        ui.init();

        while (!window.shouldClose()) {
            Time.update();
            float dt = Time.deltaTime();

            window.update();

            Input input = window.getInput();

            camera.update(dt, input);
            scene.update(dt);

            // Light mode toggles
            if (input.keyPressedOnce(GLFW.GLFW_KEY_1)) renderer.toggleLightMode(0); // Sun 
            if (input.keyPressedOnce(GLFW.GLFW_KEY_2)) renderer.toggleLightMode(1); // Point
            if (input.keyPressedOnce(GLFW.GLFW_KEY_3)) renderer.toggleLightMode(2); // Rainbow Point


            float scroll = input.consumeScrollY();
            if (scroll != 0 && renderer.getLightMode() >= 1) { // 1 = Point, 2 = Rainbow
                renderer.addIntensity(scroll * 0.1f);
            }

            window.clear();
            renderer.render(scene, camera, window);
            ui.render(window, renderer);
            window.swapBuffers();
        }

        scene.destroy();
        renderer.destroy();
        window.destroy();
        ui.destroy();
    }
}