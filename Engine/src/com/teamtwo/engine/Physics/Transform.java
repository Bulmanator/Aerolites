package com.teamtwo.engine.Physics;

import org.jsfml.system.Vector2f;

/**
 * A simple Transform class which can hold the position, rotation and scale of an object
 * @author James Bulman
 */
public class Transform {

    /** The position of the object on the screen */
    private Vector2f position;
    /** The angle of the object, in ? */
    private float angle;
    /** The relative scale factor for the object */
    private Vector2f scale;

    /**
     * Creates a default Transform, at (0, 0) with a rotation of 0 and scale of (1, 1)
     */
    public Transform() {
        position = Vector2f.ZERO;
        angle = 0;
        scale = Vector2f.ZERO;
    }

    /**
     * Creates a Transform from the position, rotation and scale specified
     * @param position The position for the Transform to be placed at
     * @param angle The angle for the Transform to be rotated to
     * @param scale The scale for the Transform to be set to
     */
    public Transform(Vector2f position, float angle, Vector2f scale) {
        this.position = position;
        this.angle = angle;
        this.scale = scale;
    }

    /**
     * Gets the current position of the Transform
     * @return The position of the Transform
     */
    public Vector2f getPosition() { return position; }

    /**
     * Gets the current rotation of the Transform
     * @return The current rotation of the Transform, in ?
     */
    public float getAngle() { return angle; }

    /**
     * Gets the current relative scale of the Transform
     * @return The scale of the Transform
     */
    public Vector2f getScale() { return scale; }

    /**
     * Moves the Transform to the given position
     * @param position The new position to move the Transform to
     */
    void setPosition(Vector2f position) { this.position = position; }

    /**
     * Rotates the Transform to the given angle
     * @param angle The angle to set the rotation to, in ?
     */
    void setAngle(float angle) { this.angle = angle; }

    /**
     * Sets the relative scale to the specified value
     * @param scale The scale to set the Transform to
     */
    void setScale(Vector2f scale) { this.scale = scale; }
}
