package com.teamtwo.engine.Physics;

import com.teamtwo.engine.Physics.Shapes.Polygon;
import com.teamtwo.engine.Utilities.Interfaces.EntityRenderable;
import com.teamtwo.engine.Utilities.Interfaces.Updateable;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

public class Body implements Updateable, EntityRenderable {

    private Polygon shape;

    private Transform transform;

    private Vector2f velocity;
    private float mass, invMass;

    public Body() {}

    public void update(float dt) {

    }

    public void render(RenderWindow renderer) {}


    public Polygon getTransformed() {
        Vector2f[] verts = shape.getVertices();

        return null;
    }
}
