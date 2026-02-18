package com.tybingham.graphics.render;

public final class MeshFactory {
    private MeshFactory() {}

    public static Mesh makeColoredCube() {
        float[] vertices = {
                // Positions          // Colours
                -0.5f,-0.5f,-0.5f,    1f,0f,0f,
                 0.5f,-0.5f,-0.5f,    0f,1f,0f,
                 0.5f, 0.5f,-0.5f,    0f,0f,1f,
                -0.5f, 0.5f,-0.5f,    1f,1f,0f,

                -0.5f,-0.5f, 0.5f,    1f,0f,1f,
                 0.5f,-0.5f, 0.5f,    0f,1f,1f,
                 0.5f, 0.5f, 0.5f,    1f,1f,1f,
                -0.5f, 0.5f, 0.5f,    0.3f,0.7f,0.9f
        };

        int[] indices = {
                0,1,2, 2,3,0,   // Back
                4,5,6, 6,7,4,   // Front
                4,7,3, 3,0,4,   // Left
                1,5,6, 6,2,1,   // Right
                4,5,1, 1,0,4,   // Bottom
                3,2,6, 6,7,3    // Top
        };

        return new Mesh(vertices, indices);
    }
}