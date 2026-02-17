package com.tybingham.graphics.scene;

public class DemoScene implements Scene {
    private float angle = 0f;

    @Override
    public void init() {
        angle = 0f;
    }

    @Override
    public void update(float dt) {
        angle += dt; // Radians / sec-ish
    }

    @Override
    public void destroy() {}

    public float getAngle() {
        return angle;
    }
}