package com.tybingham.graphics.render;

public final class MeshFactory {
    private MeshFactory() {}

    public static Mesh makeTexturedColoredCube() {
        // Vertex: Pos(3), Colour(3), Normal(3), UV(2) = 11 Floats
        float[] v = {
            // FRONT (+Z)
            -0.5f,-0.5f, 0.5f,  1f,1f,1f,  0f,0f,1f,  0f,0f,
             0.5f,-0.5f, 0.5f,  1f,1f,1f,  0f,0f,1f,  1f,0f,
             0.5f, 0.5f, 0.5f,  1f,1f,1f,  0f,0f,1f,  1f,1f,
            -0.5f, 0.5f, 0.5f,  1f,1f,1f,  0f,0f,1f,  0f,1f,

            // BACK (-Z)
             0.5f,-0.5f,-0.5f,  1f,1f,1f,  0f,0f,-1f, 0f,0f,
            -0.5f,-0.5f,-0.5f,  1f,1f,1f,  0f,0f,-1f, 1f,0f,
            -0.5f, 0.5f,-0.5f,  1f,1f,1f,  0f,0f,-1f, 1f,1f,
             0.5f, 0.5f,-0.5f,  1f,1f,1f,  0f,0f,-1f, 0f,1f,

            // LEFT (-X)
            -0.5f,-0.5f,-0.5f,  1f,1f,1f,  -1f,0f,0f, 0f,0f,
            -0.5f,-0.5f, 0.5f,  1f,1f,1f,  -1f,0f,0f, 1f,0f,
            -0.5f, 0.5f, 0.5f,  1f,1f,1f,  -1f,0f,0f, 1f,1f,
            -0.5f, 0.5f,-0.5f,  1f,1f,1f,  -1f,0f,0f, 0f,1f,

            // RIGHT (+X)
             0.5f,-0.5f, 0.5f,  1f,1f,1f,   1f,0f,0f, 0f,0f,
             0.5f,-0.5f,-0.5f,  1f,1f,1f,   1f,0f,0f, 1f,0f,
             0.5f, 0.5f,-0.5f,  1f,1f,1f,   1f,0f,0f, 1f,1f,
             0.5f, 0.5f, 0.5f,  1f,1f,1f,   1f,0f,0f, 0f,1f,

            // TOP (+Y)
            -0.5f, 0.5f, 0.5f,  1f,1f,1f,  0f,1f,0f,  0f,0f,
             0.5f, 0.5f, 0.5f,  1f,1f,1f,  0f,1f,0f,  1f,0f,
             0.5f, 0.5f,-0.5f,  1f,1f,1f,  0f,1f,0f,  1f,1f,
            -0.5f, 0.5f,-0.5f,  1f,1f,1f,  0f,1f,0f,  0f,1f,

            // BOTTOM (-Y)
            -0.5f,-0.5f,-0.5f,  1f,1f,1f,  0f,-1f,0f, 0f,0f,
             0.5f,-0.5f,-0.5f,  1f,1f,1f,  0f,-1f,0f, 1f,0f,
             0.5f,-0.5f, 0.5f,  1f,1f,1f,  0f,-1f,0f, 1f,1f,
            -0.5f,-0.5f, 0.5f,  1f,1f,1f,  0f,-1f,0f, 0f,1f,
        };

        int[] idx = {
            0,1,2, 2,3,0,
            4,5,6, 6,7,4,
            8,9,10, 10,11,8,
            12,13,14, 14,15,12,
            16,17,18, 18,19,16,
            20,21,22, 22,23,20
        };

        return new Mesh(v, idx);
    }
}
