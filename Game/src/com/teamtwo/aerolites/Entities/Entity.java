package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Messages.Message;
import com.teamtwo.engine.Messages.Observer;
import com.teamtwo.engine.Physics.RigidBody;
import com.teamtwo.engine.Utilities.Interfaces.EntityRenderable;
import com.teamtwo.engine.Utilities.Interfaces.Typeable;
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
public abstract class Entity implements EntityRenderable, Updateable, Observer, Typeable<Entity.Type> {

    public enum Type {
        Player,
        Asteroid,
        Bullet,
        EnemyBullet,
        StandardAI,
        Swamer,
        SwamerBase,
        Hexaboss,
        PascalBoss
    }

    protected RigidBody body;
    protected Color renderColour;
    protected boolean onScreen;
    protected boolean alive;
    protected Vector2f offScreenAllowance;
    private float maxSpeed = 200;

    protected Entity() {
        renderColour = Color.WHITE;
        onScreen = true;
        alive = true;
        offScreenAllowance = new Vector2f(0, 0);
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
        limitSpeed();
    }

    protected void checkOffScreen(){
        float x, y;
        if(body.getTransform().getPosition().x < -offScreenAllowance.x) {
            x = body.getTransform().getPosition().x + State.WORLD_SIZE.x;
            y = body.getTransform().getPosition().y;
            body.setTransform(new Vector2f(x, y), body.getTransform().getAngle());
        }
        else if(body.getTransform().getPosition().x > State.WORLD_SIZE.x + offScreenAllowance.x) {
            x = body.getTransform().getPosition().x - State.WORLD_SIZE.x;
            y = body.getTransform().getPosition().y;
            body.setTransform(new Vector2f(x, y), body.getTransform().getAngle());
        }
        if(body.getTransform().getPosition().y < -offScreenAllowance.y){
            x = body.getTransform().getPosition().x;
            y = body.getTransform().getPosition().y + State.WORLD_SIZE.y;
            body.setTransform(new Vector2f(x, y), body.getTransform().getAngle());
        }
        else if(body.getTransform().getPosition().y > State.WORLD_SIZE.y + offScreenAllowance.y) {
            x = body.getTransform().getPosition().x;
            y = body.getTransform().getPosition().y - State.WORLD_SIZE.y;
            body.setTransform(new Vector2f(x, y), body.getTransform().getAngle());
        }
    }

    protected void move(float forceX, float forceY) {
        // Apply force to move the ship
        Vector2f force = body.getTransform().applyRotation(new Vector2f(forceX, forceY));
        body.applyForce(force);
    }

    public void limitSpeed() {
        float x = body.getVelocity().x;
        float y = body.getVelocity().y;
        x = MathUtil.clamp(x, -maxSpeed, maxSpeed);
        y = MathUtil.clamp(y, -maxSpeed, maxSpeed);
        body.setVelocity(new Vector2f(x, y));
    }

    @Override
    public abstract void receiveMessage(Message message);

    @Override
    public abstract Type getType();

    public boolean isOnScreen() {
        return onScreen;
    }
    public RigidBody getBody(){
        return body;
    }
    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float speed){
        maxSpeed = speed;
    }
    public boolean isAlive(){ return alive; }

    public void setOnScreen(boolean onscreen) {
        onScreen = onscreen;
    }
}
