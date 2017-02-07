package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Messages.Message;
import com.teamtwo.engine.Messages.Types.CollisionMessage;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.RigidBody;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * @author Matthew Threlfall
 */
public class Asteroid extends Entity {

    private boolean onScreen;
    private boolean expload;

    /**
     * Constructs a new procedurally generated asteroid
     * @param world The world which is used to create the body
     */
    public Asteroid(World world) {
        super();
        expload = false;
        onScreen = true;
        BodyConfig config = new BodyConfig();

        config.mask = CollisionMask.ASTEROID;
        config.category = CollisionMask.ALL;

        int screenSide = MathUtil.randomInt(0,4);
        int x = 0, y = 0, velocityX = 0, velocityY = 0;
        switch(screenSide) {
            case 0:
                x = MathUtil.randomInt(0, 1920);
                y = 0;
                velocityX = MathUtil.randomInt(-60,60);
                velocityY = MathUtil.randomInt(90,200);
                break;
            case 1:
                x = MathUtil.randomInt(0, 1920);
                y = 1080;
                velocityX = MathUtil.randomInt(-60,60);
                velocityY = MathUtil.randomInt(-200,-90);
                break;
            case 2:
                x = 0;
                y = MathUtil.randomInt(0, 1080);
                velocityX = MathUtil.randomInt(90,200);
                velocityY = MathUtil.randomInt(-60,60);
                break;
            case 3:
                x = 1920;
                y = MathUtil.randomInt(0, 1080);
                velocityX = MathUtil.randomInt(-200,-90);
                velocityY = MathUtil.randomInt(-60,60);
                break;
        }

        config.position = new Vector2f(x, y);
        config.shape = new Polygon(MathUtil.randomFloat(40,60));

        config.restitution = 0.3f;
        config.velocity = new Vector2f(velocityX,velocityY);
        config.angularVelocity = MathUtil.randomFloat(0, MathUtil.PI / 4f);

        config.density = 0.6f;
        body = world.createBody(config);
        body.setData(this);
        body.registerObserver(this, Message.Type.Collision);
        offScreenAllowance = new Vector2f(body.getShape().getRadius()*4,body.getShape().getRadius()*4);
    }

    public Asteroid(World world, Vector2f pos, Vector2f vel, float radius) {
        super();
        this.onScreen = true;
        this.expload = false;
        BodyConfig config = new BodyConfig();

        config.position = pos;
        config.shape = new Polygon(radius);

        config.restitution = 0.3f;
        config.velocity = vel;
        config.angularVelocity = MathUtil.randomFloat(0, MathUtil.PI / 4f);

        config.density = 0.6f;
        body = world.createBody(config);
        body.setData(this);
        body.registerObserver(this, Message.Type.Collision);
        offScreenAllowance = new Vector2f(body.getShape().getRadius()*4,body.getShape().getRadius()*4);
    }

    @Override
    public void render(RenderWindow renderer) {
        ConvexShape bodyShape = new ConvexShape(body.getShape().getVertices());
        bodyShape.setPosition(body.getTransform().getPosition());
        bodyShape.setRotation(body.getTransform().getAngle() * MathUtil.RAD_TO_DEG);
        bodyShape.setFillColor(renderColour);
        bodyShape.setTexture(ContentManager.instance.getTexture("Asteroid"));
        renderer.draw(bodyShape);

    }


    @Override
    public void receiveMessage(Message message) {
        if(message.getType() == Message.Type.Collision) {
            CollisionMessage cm = (CollisionMessage) message;
            Type typeA = (Type)cm.getBodyA().getData().getType();
            Type typeB = (Type)cm.getBodyB().getData().getType();
            expload = typeB == Type.Bullet || typeB == Type.EnemyBullet || typeA == Type.Bullet || typeA == Type.EnemyBullet;
        }
    }

    public Polygon getShape(){
        return body.getShape();
    }

    public RigidBody getBody() {
        return body;
    }

    @Override
    public void update(float dt){
        checkOffScreen();
    }

    @Override
    public void checkOffScreen(){
        if(body.getTransform().getPosition().x < -offScreenAllowance.x || body.getTransform().getPosition().x > State.WORLD_SIZE.x + offScreenAllowance.x){
            this.onScreen = false;
        }
        else
            this.onScreen = !(body.getTransform().getPosition().y < -offScreenAllowance.y
                    || body.getTransform().getPosition().y > State.WORLD_SIZE.y + offScreenAllowance.y);
    }

    @Override
    public boolean isOnScreen(){
        return onScreen;
    }

    @Override
    public Type getType() { return Type.Asteroid; }

    public boolean shouldExpload() {
        return expload;
    }
}
