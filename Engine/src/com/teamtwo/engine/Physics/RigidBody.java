package com.teamtwo.engine.Physics;

import com.teamtwo.engine.Utilities.Interfaces.Updateable;
import org.jsfml.system.Vector2f;

public class RigidBody implements Updateable{

    // Positional
    // TODO Shape -> private Polygon shape;
    private Transform transform;

    // Material
    private float density;
    private float restitution;

    // Mass Data
    private float mass;
    private float invMass;

    private float inertia;
    private float invInertia;

    // Movement
    private Vector2f velocity;
    private float anglularVelocity;
    private Vector2f force;

    RigidBody(BodyConfig config) {
        transform = new Transform();

        density = config.density;
        restitution = config.restitution;

        mass = config.mass;
        invMass= mass==0?0:1/mass;

        inertia = config.inertia;
        invInertia = inertia==0?0:1/inertia;

        velocity = config.velocity;
        anglularVelocity = config.angularVelocity;
        force = Vector2f.ZERO;
    }

    @Override
    public void update(float dt) {
        //find acceleration in both axis
        float accelerationX = force.x*invMass;
        float accelerationY = force.y*invMass;
        //work out the new velocities using the acceleration
        velocity = new Vector2f(velocity.x + (accelerationX * dt), velocity.y + (accelerationY * dt));
        //find the new x and y
        float newX = transform.getPosition().x + (velocity.x * dt);
        float newY = transform.getPosition().y+ (velocity.y * dt);
        Vector2f newPos = new Vector2f(newX, newY);

        transform.setPosition(newPos);
    }

    /**
     * Gets the current Transform of the Body
     * @return The Transform
     */
    public Transform getTransform() { return transform; }

    public float getMass() { return mass; }

    public void applyForce(Vector2f newForce){
        force = new Vector2f(force.x+newForce.x, force.y+newForce.y);
    }
}
