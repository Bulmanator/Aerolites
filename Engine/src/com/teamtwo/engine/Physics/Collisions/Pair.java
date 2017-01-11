package com.teamtwo.engine.Physics.Collisions;

import com.teamtwo.engine.Physics.Body;
import org.jsfml.system.Vector2f;

public class Pair {

    private Body bodyA;
    private Body bodyB;

    private boolean colliding;
    private Vector2f normal;
    private float overlap;

    public Pair(Body a, Body b) {
        bodyA = a;
        bodyB = b;

        colliding = false;
        normal = new Vector2f(0, 0);
        overlap = 0;
    }

    public void testCollision() {
      //  Polygon shapeA = bodyA.getTransformed();
     //   Polygon shapeB = bodyB.getTransformed();
    }

    public void applyCollision() {}
}
