package com.teamtwo.engine.Physics;

import org.jsfml.system.Vector2f;

public class Matrix2f {

    private float m00, m01;
    private float m10, m11;

    public Matrix2f() {
        m00 = 1;
        m01 = 0;
        m10 = 0;
        m00 = 0;
    }

    public Matrix2f(float[] values) {
        if(values.length != 4) throw new IllegalArgumentException("Error: There should only be 4 values");

        set(values);
    }

    public void set(float[] values) {
        m00 = values[0];
        m01 = values[1];
        m10 = values[2];
        m11 = values[3];
    }

    public void rotate(float radians) {
        float sin = (float)Math.sin(radians);
        float cos = (float)Math.cos(radians);

        m00 = cos;
        m01 = -sin;
        m10 = sin;
        m00 = cos;
    }

    public Vector2f rotateVector(Vector2f location) {
        float x = (m00 * location.x) + (m01 * location.y);
        float y = (m10 * location.x) + (m11 * location.y);

        return new Vector2f(x, y);
    }
}