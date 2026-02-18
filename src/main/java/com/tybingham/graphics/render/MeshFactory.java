package com.tybingham.graphics.render;

public final class MeshFactory {
    private MeshFactory() {}

    public static Mesh makeColouredCube() {
        // Vertex format: position (3), colour (3), normal (3) - 9 floats
        // 24 vertices: 4 per face so each face has a constant normal
        float[] v = {
            // FRONT (+Z)
            -0.5f,-0.5f, 0.5f,   1f,0f,1f,   0f,0f,1f,
             0.5f,-0.5f, 0.5f,   0f,1f,1f,   0f,0f,1f,
             0.5f, 0.5f, 0.5f,   1f,1f,1f,   0f,0f,1f,
            -0.5f, 0.5f, 0.5f,   0.3f,0.7f,0.9f,  0f,0f,1f,

            // BACK (-Z)
             0.5f,-0.5f,-0.5f,   0f,1f,0f,   0f,0f,-1f,
            -0.5f,-0.5f,-0.5f,   1f,0f,0f,   0f,0f,-1f,
            -0.5f, 0.5f,-0.5f,   1f,1f,0f,   0f,0f,-1f,
             0.5f, 0.5f,-0.5f,   0f,0f,1f,   0f,0f,-1f,

            // LEFT (-X)
            -0.5f,-0.5f,-0.5f,   1f,0.2f,0.2f,  -1f,0f,0f,
            -0.5f,-0.5f, 0.5f,   1f,0.2f,1f,    -1f,0f,0f,
            -0.5f, 0.5f, 0.5f,   0.6f,0.8f,1f,  -1f,0f,0f,
            -0.5f, 0.5f,-0.5f,   1f,1f,0.2f,    -1f,0f,0f,

            // RIGHT (+X)
             0.5f,-0.5f, 0.5f,   0.2f,1f,0.2f,   1f,0f,0f,
             0.5f,-0.5f,-0.5f,   0.2f,1f,1f,     1f,0f,0f,
             0.5f, 0.5f,-0.5f,   0.2f,0.6f,1f,   1f,0f,0f,
             0.5f, 0.5f, 0.5f,   0.8f,1f,0.8f,   1f,0f,0f,

            // TOP (+Y)
            -0.5f, 0.5f, 0.5f,   1f,1f,1f,   0f,1f,0f,
             0.5f, 0.5f, 0.5f,   0.7f,1f,0.7f, 0f,1f,0f,
             0.5f, 0.5f,-0.5f,   0.7f,0.7f,1f, 0f,1f,0f,
            -0.5f, 0.5f,-0.5f,   1f,1f,0.3f, 0f,1f,0f,

            // BOTTOM (-Y)
            -0.5f,-0.5f,-0.5f,   1f,0.4f,0.4f,  0f,-1f,0f,
             0.5f,-0.5f,-0.5f,   0.4f,1f,0.4f,  0f,-1f,0f,
             0.5f,-0.5f, 0.5f,   0.4f,0.4f,1f,  0f,-1f,0f,
            -0.5f,-0.5f, 0.5f,   1f,0.4f,1f,    0f,-1f,0f,
        };

        int[] idx = {
            0,1,2, 2,3,0,       // Front
            4,5,6, 6,7,4,       // Back
            8,9,10, 10,11,8,    // Left
            12,13,14, 14,15,12, // Right
            16,17,18, 18,19,16, // Top
            20,21,22, 22,23,20  // Bottom
        };

        return new Mesh(v, idx);
    }
}
