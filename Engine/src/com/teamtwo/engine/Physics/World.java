package com.teamtwo.engine.Physics;

import com.teamtwo.engine.Physics.Collisions.Pair;
import com.teamtwo.engine.Utilities.Interfaces.Updateable;
import org.jsfml.system.Vector2f;

import java.util.Vector;

/**
 * A Class which represents the physics world and updates any body added to it
 */
public class World implements Updateable {
    /** The gravity of the World that should act on the bodies */
    private Vector2f gravity;

    /** A vector of all of the bodies currently registered to the World */
    private Vector<RigidBody> bodies;

    /** A vector which contains all pairs which require narrow phase collision checking */
    private Vector<Pair> pairs;

    /**
     * Constructs a new physics world with the gravity applied
     * @param gravity The gravity of the world
     */
    public World(Vector2f gravity) {
        this.gravity = gravity;
        bodies = new Vector<>();
        pairs = new Vector<>();
    }

    /**
     * Updates all rigid bodies which are present in the world and tests for collisions
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {

        // Evaluate pairs of bodies to test for collisions
        for(int i = 0; i < bodies.size(); i++) {
            RigidBody A = bodies.get(i);
            for(int j = i + 1; j < bodies.size(); j++) {
                RigidBody B = bodies.get(j);

                Pair pair = new Pair(A, B);

                if(pair.evaluate()) {
                    pairs.add(pair);
                }
            }
        }

        // Apply any pairs which did collide
        for(Pair pair : pairs) {
           pair.apply();
       }

       // Update body forces and positions
       for(RigidBody body : bodies) {
           body.applyForce(new Vector2f(gravity.x * body.getMass(), gravity.y * body.getMass()));
           body.update(dt);
           body.resetForces();
       }

       for(Pair pair : pairs) {
            pair.correctPosition();
       }

        // Clear all collision pairs
        pairs.clear();
    }


    /**
     * Removes a body from the world
     * @param body The body to remove
     * @return True if the body is successfully removed, otherwise false
     */
    public boolean removeBody(RigidBody body) {
        return bodies.remove(body);
    }

    /**
     * Creates a new body from the given configuration
     * @param config The configuration to make the body from
     * @return The body instance which was created
     */
    public RigidBody createBody(BodyConfig config) {
        RigidBody rb = new RigidBody(config);
        bodies.add(rb);

        return rb;
    }

}
