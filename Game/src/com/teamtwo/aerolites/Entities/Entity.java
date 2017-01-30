package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Physics.RigidBody;
import com.teamtwo.engine.Utilities.Interfaces.EntityRenderable;
import com.teamtwo.engine.Utilities.Interfaces.Updateable;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * @author Matthew Threlfall
 */
public class Entity implements EntityRenderable, Updateable {
    protected RigidBody body;
    protected Color renderColour;
    protected boolean onScreen;
    protected Vector2f offScreenAllowance;

    public Entity(){
        renderColour = Color.WHITE;
        onScreen = true;
        offScreenAllowance = new Vector2f(0,0);
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
        checkOffScreen();
    }
    public RigidBody getBody(){
        return body;
    }

    protected void checkOffScreen(){
        if(body.getTransform().getPosition().x < -offScreenAllowance.x){
            float x, y;
            x = body.getTransform().getPosition().x + State.WORLD_SIZE.x;
            y = body.getTransform().getPosition().y;
            body.setTransform(new Vector2f(x, y), body.getTransform().getAngle());
        }
        else if(body.getTransform().getPosition().x > State.WORLD_SIZE.x + offScreenAllowance.x){
            float x, y;
            x = body.getTransform().getPosition().x - State.WORLD_SIZE.x + offScreenAllowance.x;
            y = body.getTransform().getPosition().y;
            body.setTransform(new Vector2f(x, y), body.getTransform().getAngle());
        }
        if(body.getTransform().getPosition().y < -offScreenAllowance.y){
            float x, y;
            x = body.getTransform().getPosition().x;
            y = body.getTransform().getPosition().y + State.WORLD_SIZE.y - offScreenAllowance.y;
            body.setTransform(new Vector2f(x, y), body.getTransform().getAngle());
        }
        else if(body.getTransform().getPosition().y > State.WORLD_SIZE.y + offScreenAllowance.y){
            float x, y;
            x = body.getTransform().getPosition().x;
            y = body.getTransform().getPosition().y - State.WORLD_SIZE.y + offScreenAllowance.y;
            body.setTransform(new Vector2f(x, y), body.getTransform().getAngle());
        }
    }
    protected void move(float forceX,float forceY){
        // Apply force to move the ship
        Vector2f force = body.getTransform().applyRotation( new Vector2f(forceX, forceY));
        body.applyForce(force);
    }

    public boolean isOnScreen() {
        return onScreen;
    }
}
