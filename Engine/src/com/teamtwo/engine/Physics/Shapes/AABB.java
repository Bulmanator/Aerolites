package com.teamtwo.engine.Physics.Shapes;

import org.jsfml.system.Vector2f;

/**
 * A class which represents an Axis-Aligned Bounding Box
 */
public class AABB {

    /** The position of the centre of the AABB */
    private Vector2f centre;
    /** The half width and height of the AABB */
    private Vector2f halfSize;

    /**
     * Creates an empty AABB
     */
    public AABB() { this(0, 0, 0, 0); }

    /**
     * Creates an AABB from the given parameters
     * @param x The x coordinate of the centre
     * @param y The y coordinate of the centre
     * @param hw The half width of the centre
     * @param hh The half height of the centre
     */
    public AABB(float x, float y, float hw, float hh) {
        this(new Vector2f(x, y), new Vector2f(hw, hh));
    }

    /**
     * Creates an AABB from the given parameters
     * @param centre The centre of the AABB
     * @param halfSize The half width and height of the AABB
     */
    public AABB(Vector2f centre, Vector2f halfSize) {
        this.centre = centre;
        this.halfSize = halfSize;
    }

    /**
     * Gets the centre of the AABB
     * @return The centre
     */
    public Vector2f getCentre() { return centre; }

    /**
     * Gets the half size of the AABB
     * @return The half size
     */
    public Vector2f getHalfSize() { return halfSize; }

    /**
     * Sets the centre to the given position
     * @param x The x coordinate of the centre
     * @param y The y coordinate of the centre
     */
    public void setCentre(float x, float y) { setCentre(new Vector2f(x, y)); }

    /**
     * Sets the centre to the given position
     * @param centre The new centre to set
     */
    public void setCentre(Vector2f centre) { this.centre = centre; }

    /**
     * Sets the half size to the given parameters
     * @param hw The half width of the AABB
     * @param hh The half height of the AABB
     */
    public void setHalfSize(float hw, float hh) { setHalfSize(new Vector2f(hw, hh)); }

    /**
     * Sets the half size to the given parameter
     * @param halfSize The half size to set
     */
    public void setHalfSize(Vector2f halfSize) { this.halfSize = halfSize; }
}
