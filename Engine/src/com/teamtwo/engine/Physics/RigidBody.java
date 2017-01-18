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
        invMass = 1/config.mass;

        inertia = config.inertia;
        invInertia = 1/config.inertia;

        velocity = config.velocity;
        anglularVelocity = config.angularVelocity;
        force = Vector2f.ZERO;
    }

    @Override
    public void update(float dt) {
        float accelerationX = force.x*invMass;
        float accelerationY = force.y*invMass;
        velocity = new Vector2f(velocity.x + (accelerationX * dt), velocity.y + (accelerationY * dt));

        transform.setPosition(new Vector2f(transform.getPosition().x + (velocity.x * dt), transform.getPosition().y+ (velocity.y * dt)));
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
