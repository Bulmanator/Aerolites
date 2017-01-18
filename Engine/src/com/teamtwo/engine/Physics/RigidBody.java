package com.teamtwo.engine.Physics;

import org.jsfml.system.Vector2f;

public class RigidBody {

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

        density = 0;
        restitution = 0;

        mass = 0;
        invMass = 0;

        inertia = 0;
        invInertia = 0;

        velocity = Vector2f.ZERO;
        anglularVelocity = 0;
        force = Vector2f.ZERO;
    }

    /**
     * Gets the current Transform of the Body
     * @return The Transform
     */
    public Transform getTransform() { return transform; }
}
