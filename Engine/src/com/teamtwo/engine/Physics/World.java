package com.teamtwo.engine.Physics;

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

    public World(Vector2f gravity) {
        this.gravity = gravity;
        bodies = new Vector<>();
    }

    public void update(float dt) {
        // TODO Solve Collisions -> Integrate forces -> Apply Impulses -> Integrate positions
        for(RigidBody b:bodies){
            b.applyForce(new Vector2f(gravity.x * b.getMass(), gravity.y + b.getMass()));
            //System.out.println(gravity.x*b.getMass());
            b.update(dt);
        }
    }


    public boolean removeBody(RigidBody body) {
        return bodies.remove(body);
    }

    public RigidBody createBody(BodyConfig config) {
        RigidBody rb = new RigidBody(config);
        bodies.add(rb);

        return rb;
    }

}
