package com.teamtwo.engine.Physics.Collisions;

import com.teamtwo.engine.Physics.RigidBody;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.system.Vector2f;

public class Pair {

    /** The first body to test for collision */
    private RigidBody A;
    /** The second body to test for collision */
    private RigidBody B;

    /** The normalised direction which is colliding the least */
    private Vector2f axis;
    /** The amount the two bodies overlap each other */
    private float overlap;

    /** Whether or not the bodies are colliding */
    private boolean colliding;

    /**
     * Creates a new collision pair from the two bodies supplied
     * @param bodyA The first body to test collision on
     * @param bodyB The second body to test collision on
     */
    public Pair(RigidBody bodyA, RigidBody bodyB) {
        A = bodyA;
        B = bodyB;

        axis = null;
        overlap = 0;

        colliding = false;
    }

    /**
     * Evaluates whether the two bodies overlap
     * @return True if they are overlapping otherwise false
     */
    public boolean evaluate() {
        // TODO SAT Collision detection

        Vector2f[] vertsA = A.getShape().getTransformed();
        Vector2f[] vertsB = B.getShape().getTransformed();

        AABB aabbA = new AABB(vertsA);
        AABB aabbB = new AABB(vertsB);

        if(!AABB.overlaps(aabbA, aabbB)) return false;

        Vector2f[] normalsA = calculateNormals(vertsA);
        Vector2f[] normalsB = calculateNormals(vertsB);

        float minA, maxA;
        float minB, maxB;

        overlap = Float.MAX_VALUE;

        for(int i = 0; i < normalsA.length; i++) {
            minA = maxA = MathUtil.dot(normalsA[i], vertsA[0]);
            minB = maxB = MathUtil.dot(normalsA[i], vertsB[0]);

            for(int j = 1; j < vertsA.length; j++) {
                float o = MathUtil.dot(normalsA[i], vertsA[j]);

                if(o < minA) {
                    minA = o;
                }
                else if(o > maxA) {
                    maxA = o;
                }
            }

            for(int j = 1; j < vertsB.length; j++) {
                float o = MathUtil.dot(normalsA[i], vertsB[j]);

                if(o < minB) {
                    minB = o;
                }
                else if(o > maxB) {
                    maxB = o;
                }
            }

            if(minA > maxB || minB > maxA) {
                colliding = false;
                return false;
            }
            else {
                float o = Math.min(maxA, maxB) - Math.max(minA, minB);
                if(o < overlap) {
                    overlap = o;
                    axis = normalsA[i];
                }
            }
        }

        for(int i = 0; i < normalsB.length; i++) {
            minA = maxA = MathUtil.dot(normalsB[i], vertsA[0]);
            minB = maxB = MathUtil.dot(normalsB[i], vertsB[0]);

            for(int j = 1; j < vertsA.length; j++) {
                float o = MathUtil.dot(normalsB[i], vertsA[j]);

                if(o < minA) {
                    minA = o;
                }
                else if(o > maxA) {
                    maxA = o;
                }
            }

            for(int j = 1; j < vertsB.length; j++) {
                float o = MathUtil.dot(normalsB[i], vertsB[j]);

                if(o < minB) {
                    minB = o;
                }
                else if(o > maxB) {
                    maxB = o;
                }
            }

            if(minA > maxB || minB > maxA) {
                colliding = false;
                return false;
            }
            else {
                float o = Math.min(maxA, maxB) - Math.max(minA, minB);
                if(o < overlap) {
                    overlap = o;
                    axis = normalsB[i];
                }
            }
        }

        colliding = true;
        return true;
    }

    public void apply() {
        if(!colliding) return;

        /*Vector2f centre = Vector2f.sub(A.getTransform().getPosition(), B.getTransform().getPosition());
        if(MathUtil.dot(centre, axis) > 0) {
            RigidBody tmp = A;
            A = B;
            B = tmp;
        } */

        // Choose which restitution value to use
        float e = Math.min(A.getRestitution(), B.getRestitution());

        // Calculate the relative velocity between both bodies
        Vector2f rv = Vector2f.sub(B.getVelocity(), A.getVelocity());

        if(MathUtil.dot(rv, axis) > 0) return;

        float impulseMag = -(1 + e) * (MathUtil.dot(rv, axis));
        impulseMag /= (A.getInvMass() + B.getInvMass());

        Vector2f impulse = new Vector2f(axis.x * impulseMag, axis.y * impulseMag);

        float totalMass = A.getMass() + B.getMass();
        float ratio = A.getMass() / totalMass;

        Vector2f applied = new Vector2f(-impulse.x * ratio, -impulse.y * ratio);
        A.applyImpulse(applied);

        ratio = B.getMass() / totalMass;
        applied = new Vector2f(impulse.x * ratio, impulse.y * ratio);
        B.applyImpulse(applied);

       // System.out.println("Vel A: " + A.getVelocity());
       // System.out.println("Vel B: " + B.getVelocity());
    }

    public void correctPosition() {
        float percent = 0.2f;
        float correctionVal = (overlap / (A.getInvMass() + B.getInvMass())) * percent;

        Vector2f correction = new Vector2f(correctionVal * axis.x, correctionVal * axis.y);

        Vector2f positionA = Vector2f.sub(A.getTransform().getPosition(),
                new Vector2f(correction.x * A.getInvMass(), correction.y * A.getInvMass()));

        Vector2f positionB = Vector2f.add(B.getTransform().getPosition(),
                new Vector2f(correction.x * B.getInvMass(), correction.y * B.getInvMass()));

        A.setTransform(positionA, A.getTransform().getAngle());
        B.setTransform(positionB, B.getTransform().getAngle());
    }

    /**
     * Generates the edge normals from the given vertices which make up a polygon
     * @param vertices The vertices which make up the polygon
     * @return The edge normals
     */
    private static Vector2f[] calculateNormals(Vector2f[] vertices) {
        Vector2f[] normals = new Vector2f[vertices.length];
        for(int i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
            Vector2f nor = Vector2f.sub(vertices[i], vertices[j]);
            normals[i] = MathUtil.normalise(new Vector2f(nor.y, -nor.x));
        }

        return normals;
    }
}
