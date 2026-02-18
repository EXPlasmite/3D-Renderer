package com.tybingham.graphics.math;

public class Vec3 {
    public float x, y, z;

    public Vec3() { this(0,0,0); }
    public Vec3(float x, float y, float z) { this.x=x; this.y=y; this.z=z; }

    public Vec3 add(Vec3 o) { return new Vec3(x + o.x, y + o.y, z + o.z); }
    public Vec3 mul(float s) { return new Vec3(x * s, y * s, z * s); }

    public Vec3 set(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
        return this;
    }

    public Vec3 sub(Vec3 o) { return new Vec3(x - o.x, y - o.y, z - o.z); }

    public Vec3 normalize() {
        float len = (float)Math.sqrt(x*x + y*y + z*z);
        if (len == 0) return new Vec3(0,0,0);
        return new Vec3(x/len, y/len, z/len);
    }

    public static Vec3 cross(Vec3 a, Vec3 b) {
        return new Vec3(
                a.y*b.z - a.z*b.y,
                a.z*b.x - a.x*b.z,
                a.x*b.y - a.y*b.x
        );
    }

    public static float dot(Vec3 a, Vec3 b) {
        return a.x*b.x + a.y*b.y + a.z*b.z;
    }
}