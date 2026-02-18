package com.tybingham.graphics.render;

import com.tybingham.graphics.app.Input;
import com.tybingham.graphics.math.Mat4;
import com.tybingham.graphics.math.Vec3;
import org.lwjgl.glfw.GLFW;

public class Camera {
    private final Vec3 position = new Vec3(0,0,3);

    // FPS-style angles
    private float yaw = -90f;   // Looking down -Z initially
    private float pitch = 0f;

    // Tunables
    private float moveSpeed = 3.5f;
    private float mouseSensitivity = 0.12f;

    public void setPosition(float x, float y, float z) {
        position.set(x,y,z);
    }

    public void reset() {
        position.set(0, 0, 3);
        yaw = -90f;
        pitch = 0f;
    }

    public Vec3 getPosition() { return position; }

    public Mat4 getViewMatrix() {
        Vec3 forward = getForward();
        Vec3 target = position.add(forward);
        return Mat4.lookAt(position, target, new Vec3(0,1,0));
    }

    public void update(float dt, Input input) {

        if (input.keyPressedOnce(GLFW.GLFW_KEY_R)) {
            reset();
            input.resetMouse(); // Prevents camera jump after reset
        }

        // Mouse look
        float dx = input.consumeMouseDX();
        float dy = input.consumeMouseDY();

        yaw += dx * mouseSensitivity;
        pitch += dy * mouseSensitivity;

        // Clamp pitch to avoid flip
        if (pitch > 89f) pitch = 89f;
        if (pitch < -89f) pitch = -89f;

        // Movement speed
        float speed = moveSpeed;
        if (input.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
            speed *= 2.0f;
        }

        Vec3 forward = getForward();
        Vec3 right = Vec3.cross(forward, new Vec3(0,1,0)).normalize();
        Vec3 up = new Vec3(0,1,0);

        // WASD
        if (input.keyDown(GLFW.GLFW_KEY_W)) position.set(
                position.x + forward.x * speed * dt,
                position.y + forward.y * speed * dt,
                position.z + forward.z * speed * dt
        );
        if (input.keyDown(GLFW.GLFW_KEY_S)) position.set(
                position.x - forward.x * speed * dt,
                position.y - forward.y * speed * dt,
                position.z - forward.z * speed * dt
        );
        if (input.keyDown(GLFW.GLFW_KEY_D)) position.set(
                position.x + right.x * speed * dt,
                position.y + right.y * speed * dt,
                position.z + right.z * speed * dt
        );
        if (input.keyDown(GLFW.GLFW_KEY_A)) position.set(
                position.x - right.x * speed * dt,
                position.y - right.y * speed * dt,
                position.z - right.z * speed * dt
        );

        // Vertical movement
        if (input.keyDown(GLFW.GLFW_KEY_SPACE)) position.set(
                position.x, position.y + up.y * speed * dt, position.z
        );
        if (input.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) position.set(
                position.x, position.y - up.y * speed * dt, position.z
        );
    }

    private Vec3 getForward() {
        float yawRad = (float)Math.toRadians(yaw);
        float pitchRad = (float)Math.toRadians(pitch);

        float fx = (float)(Math.cos(yawRad) * Math.cos(pitchRad));
        float fy = (float)(Math.sin(pitchRad));
        float fz = (float)(Math.sin(yawRad) * Math.cos(pitchRad));

        return new Vec3(fx, fy, fz).normalize();
    }
}