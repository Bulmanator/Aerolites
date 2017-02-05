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

    /**
     * Constructs a new procedurally generated asteroid
     * @param world The world which is used to create the body
     */
    public Asteroid(World world) {
        this.onScreen = true;
        BodyConfig config = new BodyConfig();

        int screenSide = MathUtil.randomInt(0,4);
        int x = 0, y = 0, velocityX = 0, velocityY = 0;
        switch(screenSide) {
            case 0:
                x = MathUtil.randomInt(0, 1920);
                y = 0;
                velocityX = MathUtil.randomInt(-30,30);
                velocityY = MathUtil.randomInt(10,100);
                break;
            case 1:
                x = MathUtil.randomInt(0, 1920);
                y = 1080;
                velocityX = MathUtil.randomInt(-30,30);
                velocityY = MathUtil.randomInt(-100,-10);
                break;
            case 2:
                x = 0;
                y = MathUtil.randomInt(0, 1080);
                velocityX = MathUtil.randomInt(10,100);
                velocityY = MathUtil.randomInt(-30,30);
                break;
            case 3:
                x = 1920;
                y = MathUtil.randomInt(0, 1080);
                velocityX = MathUtil.randomInt(-100,-10);
                velocityY = MathUtil.randomInt(-30,30);
                break;
            default:
                System.out.println("WHAT?!");
        }


        config.position = new Vector2f(x,y);
        config.shape = new Polygon();

        config.restitution = 0.3f;
        config.velocity = new Vector2f(velocityX,velocityY);
        config.angularVelocity = MathUtil.randomFloat(0, MathUtil.PI / 4f);

        config.density = 0.6f;
        body = world.createBody(config);
        body.setData(this);
        body.registerObserver(this, Message.Type.Collision);
        offScreenAllowance = new Vector2f(body.getShape().getRadius()*4,body.getShape().getRadius()*4);
    }

    @Override
    public void render(RenderWindow renderer) {
        /** Simply runs the body renderer from the entity class it extends from */
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
            System.out.println(cm.getBodyA().getData().getType() + " collided with " + cm.getBodyB().getData().getType());
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
}
