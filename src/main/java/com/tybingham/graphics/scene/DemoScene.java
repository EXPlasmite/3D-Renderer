package com.tybingham.graphics.scene;

import com.tybingham.graphics.render.Mesh;
import com.tybingham.graphics.render.MeshFactory;

import java.util.ArrayList;
import java.util.List;

public class DemoScene implements Scene {
    private final List<Object3D> objects = new ArrayList<>();
    private Mesh cubeMesh;

    @Override
    public void init() {
        cubeMesh = MeshFactory.makeColouredCube();

        Object3D a = new Object3D(cubeMesh);
        a.transform.position.set(-2f, 0f, 0f);
        objects.add(a);

        Object3D b = new Object3D(cubeMesh);
        b.transform.position.set(2f, 0f, 0f);
        b.transform.scale.set(0.5f, 0.5f, 0.5f);
        objects.add(b);

        Object3D c = new Object3D(cubeMesh);
        c.transform.position.set(0f, 1.2f, 0f);
        c.transform.scale.set(0.75f, 0.75f, 0.75f);
        objects.add(c);

        Object3D d = new Object3D(cubeMesh);
        d.transform.position.set(0f, -1.2f, 0f);
        objects.add(d);

        Object3D e = new Object3D(cubeMesh);
        e.transform.position.set(0f, 0f, -2f);
        objects.add(e);
    }

    @Override
    public void update(float dt) {
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).transform.rotation.y += dt * (0.6f + i * 0.2f);
        }
    }

    public List<Object3D> getObjects() {
        return objects;
    }

    @Override
    public void destroy() {
        if (cubeMesh != null) cubeMesh.destroy();
    }
}