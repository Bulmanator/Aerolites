package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Physics.RigidBody;
import com.teamtwo.engine.Utilities.Interfaces.EntityRenderable;
import com.teamtwo.engine.Utilities.Interfaces.Updateable;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.RenderWindow;

/**
 * @author Matthew Threlfall
 */
public class Entity implements EntityRenderable, Updateable {
    protected RigidBody body;
    protected Color renderColour;

    public Entity(){
        renderColour = Color.WHITE;
    }

    @Override
    public void render(RenderWindow renderer) {
        ConvexShape bodyShape = new ConvexShape(body.getShape().getVertices());
        bodyShape.setPosition(body.getTransform().getPosition());
        bodyShape.setRotation(body.getTransform().getAngle() * MathUtil.RAD_TO_DEG);
        bodyShape.setFillColor(renderColour);
        renderer.draw(bodyShape);
    }

    @Override
    public void update(float dt) {

    }
    public RigidBody getBody(){
        return body;
    }
}
