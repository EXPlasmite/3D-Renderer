package com.tybingham.graphics.render;

import com.tybingham.graphics.math.Mat4;
import com.tybingham.graphics.math.Vec3;

public class Camera {
    private final Vec3 position = new Vec3(0,0,3);

    public void setPosition(float x, float y, float z) {
        position.x = x; position.y = y; position.z = z;
    }

    public Vec3 getPosition() {
        return position;
    }

    public Mat4 getViewMatrix() {
        // look towards origin for now
        return Mat4.lookAt(position, new Vec3(0,0,0), new Vec3(0,1,0));
    }
}