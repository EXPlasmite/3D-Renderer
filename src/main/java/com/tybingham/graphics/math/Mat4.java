package com.tybingham.graphics.math;

public class Mat4 {
    // Column-major 4x4 (OpenGL-friendly)
    public final float[] m = new float[16];

    public Mat4() {}

    public static Mat4 identity() {
        Mat4 r = new Mat4();
        r.m[0] = r.m[5] = r.m[10] = r.m[15] = 1f;
        return r;
    }

    public static Mat4 perspective(float fovRadians, float aspect, float near, float far) {
        Mat4 r = new Mat4();
        float f = 1f / (float)Math.tan(fovRadians / 2f);

        r.m[0] = f / aspect;
        r.m[5] = f;
        r.m[10] = (far + near) / (near - far);
        r.m[11] = -1f;
        r.m[14] = (2f * far * near) / (near - far);
        return r;
    }

    public static Mat4 rotateY(float radians) {
        Mat4 r = identity();
        float c = (float)Math.cos(radians);
        float s = (float)Math.sin(radians);

        r.m[0] = c;
        r.m[2] = s;
        r.m[8] = -s;
        r.m[10] = c;
        return r;
    }

    public static Mat4 lookAt(Vec3 eye, Vec3 target, Vec3 up) {
        Vec3 f = target.sub(eye).normalize();
        Vec3 s = Vec3.cross(f, up.normalize()).normalize();
        Vec3 u = Vec3.cross(s, f);

        Mat4 r = identity();
        r.m[0] = s.x; r.m[4] = s.y; r.m[8]  = s.z;
        r.m[1] = u.x; r.m[5] = u.y; r.m[9]  = u.z;
        r.m[2] = -f.x; r.m[6] = -f.y; r.m[10] = -f.z;

        r.m[12] = -Vec3.dot(s, eye);
        r.m[13] = -Vec3.dot(u, eye);
        r.m[14] =  Vec3.dot(f, eye);
        return r;
    }

    public static Mat4 mul(Mat4 a, Mat4 b) {
        Mat4 r = new Mat4();
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                float sum = 0f;
                for (int i = 0; i < 4; i++) {
                    sum += a.m[i * 4 + row] * b.m[col * 4 + i];
                }
                r.m[col * 4 + row] = sum;
            }
        }
        return r;
    }
}