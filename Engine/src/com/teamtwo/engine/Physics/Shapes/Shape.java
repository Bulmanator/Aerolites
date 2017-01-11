package com.teamtwo.engine.Physics.Shapes;

import com.teamtwo.engine.Utilities.Interfaces.EntityRenderable;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

public abstract class Shape implements EntityRenderable {

    /** The radius of the shape used for random polygons */
    protected float radius;
    /** The current rotation of the shape */
    protected float angle;
    /** The centre of the shape */
    protected Vector2f centre;

    /** Initialises a default shape */
    public Shape() {
        radius = 0;
        angle = 0;
        centre = new Vector2f(0, 0);
    }

    /**
     * Rotates the shape by the given angle
     * @param degrees The amount to rotate by, in degrees
     */
    public abstract void rotate(float degrees);

    /**
     * Draws the shape to the screen
     * @param renderer The {@link RenderWindow} to draw the entity to
     */
    public abstract void render(RenderWindow renderer);

    /**
     * Moves the shape by the given parameters
     * @param x The amount in the x direction to move
     * @param y The amount in the y direction to move
     */
    public void translate(float x, float y) { translate(new Vector2f(x, y)); }

    /**
     * Moves the shape by the given distance
     * @param distance The distance to move the shape
     */
    public void translate(Vector2f distance) { centre = Vector2f.add(centre, distance); }

    /**
     * Gets the radius of the shape
     * @return The radius
     */
    public float getRadius() { return radius; }

    /**
     * Gets the angle of the shape
     * @return The current rotation
     */
    public float getAngle() { return angle; }

    /**
     * Gets the centre of the shape
     * @return The centre
     */
    public Vector2f getCentre() { return centre; }

    /**
     * Sets the centre to the given parameters
     * @param x The x coordinate of the centre
     * @param y The y coordinate of the centre
     */
    public void setCentre(float x, float y) { setCentre(new Vector2f(x, y)); }

    /**
     * Sets the centre to the given position
     * @param centre The position to set the centre to
     */
    public void setCentre(Vector2f centre) {
        this.centre = centre;
    }
}
