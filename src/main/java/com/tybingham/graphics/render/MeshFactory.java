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
                0,1,2, 2,3,0,   // back
                4,5,6, 6,7,4,   // front
                4,7,3, 3,0,4,   // left
                1,5,6, 6,2,1,   // right
                4,5,1, 1,0,4,   // bottom
                3,2,6, 6,7,3    // top
        };

        return new Mesh(vertices, indices);
    }
}