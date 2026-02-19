package com.tybingham.graphics.scene;

import com.tybingham.graphics.render.Mesh;
import com.tybingham.graphics.render.MeshFactory;
import com.tybingham.graphics.render.Texture;

import java.util.ArrayList;
import java.util.List;

public class DemoScene implements Scene {
    private final List<Object3D> objects = new ArrayList<>();
    private Mesh cubeMesh;
    private Texture brick;

    @Override
    public void init() {
        cubeMesh = MeshFactory.makeTexturedColoredCube();
        brick = new Texture("textures/brick.jpg");

        // 1) Textured centre cube
        Object3D centre = new Object3D(cubeMesh);
        centre.useTexture = true;
        centre.textureId = brick.getId();
        centre.transform.position.set(0f, 0f, 0f);
        objects.add(centre);

        // 2) Coloured left cube
        Object3D left = new Object3D(cubeMesh);
        left.useTexture = false;
        left.r = 1f; left.g = 0.2f; left.b = 0.2f;
        left.transform.position.set(-2f, 0f, 0f);
        objects.add(left);

        // 3) Coloured right cube
        Object3D right = new Object3D(cubeMesh);
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
    }

    @Override
    public void update(float dt) {
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).transform.rotation.y += dt * (0.6f + i * 0.2f);
        }
    }

    public List<Object3D> getObjects() { return objects; }

    @Override
    public void destroy() {
        if (brick != null) brick.destroy();
        if (cubeMesh != null) cubeMesh.destroy();
    }
}