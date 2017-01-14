package com.teamtwo.engine.Graphics.Particles;

import com.teamtwo.engine.Utilities.Interfaces.EntityRenderable;
import com.teamtwo.engine.Utilities.Interfaces.Initialisable;
import com.teamtwo.engine.Utilities.Interfaces.Updateable;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

/**
 * Represents a single particle inside the particle system
 */
public class Particle implements Initialisable<Particle.Configuration>, Updateable, EntityRenderable {

    /**
     * A class used to configure a particle
     */
    public static class Configuration {

        /** The starting position of the particle, default = (0, 0) */
        public Vector2f position;
        /** The speed of the particle, default = 0 */
        public float speed;
        /** The minimum angle to generate between, in degrees, default = 0 */
        public float minAngle;
        /** The maximum angle to generate between, in degrees, default = 0 */
        public float maxAngle;
        /** The rotational speed of the particles, in degrees per second, default = 0 */
        public float rotationalSpeed;

        /** The point count of the display shape, use 0 for circle, default = 0 */
        public int pointCount;
        /** The texture to apply to the particle, will not be used if null, default = null */
        public Texture texture;
        /** Whether the particle will fade out or disappear, default = false */
        public boolean fadeOut;

        /** The colours transition through, up to 8 colours are supported */
        public final Color[] colours;

        /** The beginning size of the particle, default = 5 */
        public float startSize;
        /** The ending size of the particle, default = 5 */
        public float endSize;

        /** The minimum length of a particles life, in seconds, default = 0.1 */
        public float minLifetime;
        /** The maximum length of a particles life, in seconds, default = 1 */
        public float maxLifetime;


        /**
         * Instantiates a default particle configuration
         */
        public Configuration() {
            // Positional
            position = new Vector2f(0, 0);
            speed = 0;
            minAngle = 0;
            maxAngle = 0;
            rotationalSpeed = 0;

            // Display
            pointCount = 0;
            texture = null;
            fadeOut = false;

            // Colour
            colours = new Color[8];
            colours[0] = Color.WHITE;

            // Size
            startSize = 5;
            endSize = 5;

            // Lifetime
            minLifetime = 0.1f;
            maxLifetime = 1;
        }

        /**
         * Constructs a new Configuration, copying the values from the one given
         * @param config The {@link Configuration} to copy values from
         */
        Configuration(Configuration config) {
            // Positional
            position = new Vector2f(config.position.x, config.position.y);
            speed = config.speed;
            minAngle = config.minAngle;
            maxAngle = config.maxAngle;
            rotationalSpeed = config.rotationalSpeed;

            // Display
            pointCount = config.pointCount;
            texture = config.texture;
            fadeOut = config.fadeOut;

            // Colour
            colours = new Color[8];
            for(int i = 0; i < config.colours.length; i++) {
                colours[i] = config.colours[i] == null ? null : new Color(config.colours[i], 255);
            }

            // Size
            startSize = config.startSize;
            endSize = config.endSize;

            // Lifetime
            minLifetime = config.minLifetime;
            maxLifetime = config.maxLifetime;
        }
    }

    /** The position of the particle */
    private Vector2f position;
    /** The velocity of the particle */
    private Vector2f velocity;
    /** The rotational speed of the particle */
    private float rotationalSpeed;

    /** The shape which is drawn to the screen */
    private CircleShape display;
    /** Whether the particle will fade out or disappear */
    private boolean fadeOut;

    /** The colours to transition through */
    private Color[] colours;
    /** The time each colour is present for */
    private float colourTime;

    /** The size of the particle at the beginning of its lifetime */
    private float startSize;
    /** The size of the particle at the end of its life */
    private float endSize;

    /** How much time has passed since the particle was initialised, in seconds */
    private float accumulator;
    /** How long the particle will live for, in seconds */
    private float lifetime;
    /** Whether or not the particle is alive */
    private boolean alive;

    /**
     * Constructs a Particle, Use {@link Particle#initialise(Configuration)} to configure the particle
     */
    Particle() {
        // Positional
        position = new Vector2f(0, 0);
        velocity = new Vector2f(0, 0);
        rotationalSpeed = 0;

        // Display
        display = new CircleShape(5f);
        fadeOut = false;

        // Colour
        colours = new Color[1];

        // Size
        startSize = 5f;
        endSize = 5f;

        // Lifetime
        accumulator = 0;
        lifetime = 0;
        alive = false;
    }

    /**
     * Initialises the particle from the given configuration
     * @param config The {@link Particle.Configuration} to initialise the particle with
     */
    public void initialise(Configuration config) {
        // Set the position
        position = new Vector2f(config.position.x, config.position.y);

        // Work out the (x, y) velocity using the angle and speed
        float angle = MathUtil.randomFloat(config.minAngle, config.maxAngle);
        angle *= MathUtil.DEG_TO_RAD;

        velocity = new Vector2f(
                config.speed * MathUtil.cos(angle),
                -config.speed * MathUtil.sin(angle)
        );

        // Set the rotational speed
        rotationalSpeed = config.rotationalSpeed;

        // Set the begin and end sizes
        startSize = config.startSize;
        endSize = config.endSize;

        // Work out what shape to use
        if(config.pointCount < 3 || config.pointCount > 16) {
            display = new CircleShape(startSize);
        }
        else {
            display = new CircleShape(startSize, config.pointCount);
        }

        display.setRotation(angle * MathUtil.RAD_TO_DEG);

        // Set the texture if one is present
        if(config.texture != null) display.setTexture(config.texture);

        int colourCount = 0;
        for(int i = 0; i < config.colours.length; i++) {
            if(config.colours[i] == null) break;
            colourCount++;
        }

        if(colourCount == 0) {
            colourCount = 1;
            colours = new Color[colourCount];
            colours[0] = Color.WHITE;
        }
        else {
            colours = new Color[colourCount];
            System.arraycopy(config.colours, 0, colours, 0, colourCount);
        }

        // Set display position
        display.setOrigin(startSize, startSize);
        display.setPosition(position);
        display.setFillColor(colours[0]);

        // Set whether to fade out or not
        fadeOut = config.fadeOut;

        // Reset the accumulator and set the lifetime
        accumulator = 0;
        lifetime = MathUtil.randomFloat(config.minLifetime, config.maxLifetime);
        lifetime = MathUtil.round(lifetime, 4);

        colourTime = lifetime / (colourCount - 1);

        // Mark the particle as alive
        alive = true;
    }

    /**
     * Updates the particle for a single frame
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {
        if(!alive) return;

        accumulator += dt;
        float timeLeft = lifetime - accumulator;

        if(timeLeft > 0) {

            float ratio = accumulator / lifetime;

            display.setRadius(MathUtil.lerp(startSize, endSize, ratio));
            display.setOrigin(display.getRadius(), display.getRadius());


            position = new Vector2f(
                position.x + (velocity.x * dt),
                position.y + (velocity.y * dt)
            );


            display.setPosition(position);
            display.rotate(rotationalSpeed * dt);

            int a = 255;
            if (fadeOut) a = (int) (255 * (timeLeft / lifetime));

            if(colours.length > 1) {

                Color start, end;

                int colourIndex = (int) (accumulator / colourTime);

                start = colours[colourIndex];
                end = colours[colourIndex + 1];

                float colRatio = (accumulator - (colourTime * colourIndex)) / colourTime;

                int r, g, b;

                r = (int) MathUtil.lerp(start.r, end.r, colRatio);
                g = (int) MathUtil.lerp(start.g, end.g, colRatio);
                b = (int) MathUtil.lerp(start.b, end.b, colRatio);

                display.setFillColor(new Color(r, g, b, a));
            }
            else {
                display.setFillColor(new Color(colours[0], a));
            }
        }
        else {
            alive = false;
        }
    }

    /**
     * Draws the particle to the screen
     * @param renderer The {@link RenderWindow} to draw the entity to
     */
    public void render(RenderWindow renderer) {
        if(!alive) return;
        renderer.draw(display);
    }

    /**
     * Whether or not the particle is alive
     * @return True if the particle is alive, otherwise False
     */
    public boolean isAlive() { return alive; }

    /**
     * Kills the particle to prevent it from being updated or rendered
     */
    void kill() { alive = false; }
}
