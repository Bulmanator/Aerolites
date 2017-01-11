package com.teamtwo.engine.Physics;

import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.system.Vector2f;

public class Transform {

    /** The position of the transform */
    private Vector2f position;
    /** The angle of the transform, in radians */
    private float rotation;
    /** The scale of the transform */
    private Vector2f scale;

    public Transform() {
        position = new Vector2f(0, 0);
        rotation = 0;
        scale = new Vector2f(1, 1);
    }

    public Transform(Vector2f position, float radians, Vector2f scale) {
        this.position = position;
        rotation = radians;
        this.scale = scale;
    }

    public Vector2f getPosition() { return position; }
    public float getRotation() { return rotation; }
    public Vector2f getScale() { return scale; }

    public void translate(float x, float y) { translate(new Vector2f(x, y)); }
    public void translate(Vector2f distance) {
        position = Vector2f.add(position, distance);
    }

    public void setPosition(float x, float y) { setPosition(new Vector2f(x, y)); }
    public void setPosition(Vector2f position) { this.position = position; }

    public void rotate(float radians) {
        rotation += radians;
        if(rotation > MathUtil.PI2) {
            rotation -= MathUtil.PI2;
        }
        else if(rotation < 0) {
            rotation += MathUtil.PI2;
        }
    }

    public void setRotation(float radians) {
        rotation = radians;
        if(rotation > MathUtil.PI2) {
            int times = (int)(rotation % MathUtil.PI2);
            rotation -= (MathUtil.PI2 * times);
        }
        else if(rotation < 0) {
            int times = (int)(rotation % MathUtil.PI2);
            rotation += (MathUtil.PI2 * times);
        }
    }

    public void setScale(float sx, float sy) { setScale(new Vector2f(sx, sy)); }
    public void setScale(Vector2f scale) { this.scale = scale; }
}
