package com.tybingham.graphics.scene;

import com.tybingham.graphics.render.Mesh;

public class Object3D {
    public final Transform transform = new Transform();
    public Mesh mesh;

    // Per-object colour uniform
    public float r = 1f, g = 1f, b = 1f;

    // Texture support
    public int textureId = 0;

    public Object3D(Mesh mesh) {
        this.mesh = mesh;
    }
}