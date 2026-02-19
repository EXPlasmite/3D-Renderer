package com.tybingham.graphics.scene;

import com.tybingham.graphics.math.Vec3;
import com.tybingham.graphics.render.Mesh;
import com.tybingham.graphics.render.MeshFactory;
import com.tybingham.graphics.render.Texture;

import java.util.ArrayList;
import java.util.List;

public class DemoScene implements Scene {
    private final List<Object3D> objects = new ArrayList<>();
    private Mesh cubeMesh;
    private Texture brick;

    // Keep references for animation
    private Object3D centre;
    private Object3D left;
    private Object3D right;

    // Base values so we don’t drift over time
    private final Vec3 leftBasePos = new Vec3();
    private final Vec3 rightBasePos = new Vec3();
    private final Vec3 centreBaseScale = new Vec3();

    private float time = 0f;

    @Override
    public void init() {
        cubeMesh = MeshFactory.makeTexturedColoredCube();
        brick = new Texture("textures/brick.jpg");

        // 1) Textured centre cube
        centre = new Object3D(cubeMesh);
        centre.useTexture = true;
        centre.textureId = brick.getId();
        centre.transform.position.set(0f, 0f, 0f);
        objects.add(centre);

        // 2) Coloured left cube
        left = new Object3D(cubeMesh);
        left.useTexture = false;
        left.r = 1f; left.g = 0.2f; left.b = 0.2f;
        left.transform.position.set(-2f, 0f, 0f);
        objects.add(left);

        // 3) Coloured right cube
        right = new Object3D(cubeMesh);
        right.useTexture = false;
        right.r = 0.2f; right.g = 1f; right.b = 0.2f;
        right.transform.position.set(2f, 0f, 0f);
        right.transform.scale.set(0.75f, 0.75f, 0.75f);
        objects.add(right);

        // 4) Textured top cube
        Object3D top = new Object3D(cubeMesh);
        top.useTexture = true;
        top.textureId = brick.getId();
        top.transform.position.set(0f, 1.5f, 0f);
        top.transform.scale.set(0.6f, 0.6f, 0.6f);
        objects.add(top);

        // 5) Textured bottom cube
        Object3D bottom = new Object3D(cubeMesh);
        bottom.useTexture = true;
        bottom.textureId = brick.getId();
        bottom.transform.position.set(0f, -1.5f, 0f);
        objects.add(bottom);

        // Store base values for animation
        leftBasePos.set(left.transform.position.x, left.transform.position.y, left.transform.position.z);
        rightBasePos.set(right.transform.position.x, right.transform.position.y, right.transform.position.z);
        centreBaseScale.set(centre.transform.scale.x, centre.transform.scale.y, centre.transform.scale.z);
    }

    @Override
    public void update(float dt) {
        time += dt;

        // Rotate everything EXCEPT centre (centre pulses instead)
        for (int i = 0; i < objects.size(); i++) {
            Object3D obj = objects.get(i);
            if (obj == centre) continue;
            obj.transform.rotation.y += dt * (0.6f + i * 0.2f);
        }

        // Left + right bob up/down
        float bobSpeed = 1.5f;
        float bobAmp = 0.6f;

        left.transform.position.y = leftBasePos.y + (float)Math.sin(time * bobSpeed) * bobAmp;
        right.transform.position.y = rightBasePos.y + (float)Math.sin(time * bobSpeed + Math.PI) * bobAmp;

        // Centre pulses scale
        float pulseSpeed = 2.0f;
        float pulseAmp = 0.25f;
        float s = 1.0f + (float)Math.sin(time * pulseSpeed) * pulseAmp;

        centre.transform.scale.set(
                centreBaseScale.x * s,
                centreBaseScale.y * s,
                centreBaseScale.z * s
        );
    }

    public List<Object3D> getObjects() { return objects; }

    @Override
    public void destroy() {
        if (brick != null) brick.destroy();
        if (cubeMesh != null) cubeMesh.destroy();
    }
}
