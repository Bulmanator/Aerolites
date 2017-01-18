package com.teamtwo.engine.Physics;

import org.jsfml.system.Vector2f;

public class BodyConfig {
    public Vector2f position = Vector2f.ZERO;

    public Vector2f velocity = Vector2f.ZERO;

    public float angularVelocity = 0;

    public float inertia = 0;

    public float restitution = 0;

    public float mass = 0;

    public float density = 0;

}
