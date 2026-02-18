package com.tybingham.graphics.scene;

import com.tybingham.graphics.math.Mat4;
import com.tybingham.graphics.math.Vec3;

public class Transform {
    public final Vec3 position = new Vec3(0,0,0);
    public final Vec3 rotation = new Vec3(0,0,0); // Radians
    public final Vec3 scale    = new Vec3(1,1,1);

    public Mat4 modelMatrix() {
        Mat4 t  = Mat4.translation(position);
        Mat4 rx = Mat4.rotateX(rotation.x);
        Mat4 ry = Mat4.rotateY(rotation.y);
        Mat4 rz = Mat4.rotateZ(rotation.z);
        Mat4 s  = Mat4.scale(scale);

        // Model = T * Rz * Ry * Rx * S
        return Mat4.mul(t, Mat4.mul(rz, Mat4.mul(ry, Mat4.mul(rx, s))));
    }
}