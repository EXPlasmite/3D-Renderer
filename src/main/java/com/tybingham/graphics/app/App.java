package com.tybingham.graphics.app;

public class App {
    private Window window;

    public void run() {
        window = new Window("3D Renderer", 1280, 720);
        window.init();

        Time.init();
        while (!window.shouldClose()) {
            Time.update();

            window.update();  // poll input/events
            window.render();  // clear for now
        }

        window.destroy();
    }
}