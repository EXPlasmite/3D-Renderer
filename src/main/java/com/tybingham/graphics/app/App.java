package com.tybingham.graphics.app;

import com.tybingham.graphics.render.Camera;
import com.tybingham.graphics.render.Renderer;
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

        while (!window.shouldClose()) {
            Time.update();
            float dt = Time.deltaTime();

            window.update();

            // Update camera
            camera.update(dt, window.getInput());

            scene.update(dt);

            window.clear();
            renderer.render(scene, camera, window);
            window.swapBuffers();
        }

        scene.destroy();
        renderer.destroy();
        window.destroy();
    }
}