package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.RigidBody;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * @author Matthew Threlfall
 */
public class Asteroid extends Entity {

    private boolean onScreen;
    private boolean hasBeenOnScreen;

    /**
     * Constructs a new procedurally generated asteroid
     * @param world The world which is used to create the body
     */
    public Asteroid(World world) {
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
        onScreen = true;
    }

    @Override
    public void render(RenderWindow renderer) {
        /** Simply runs the body renderer from the entity class it extends from */
        super.render(renderer);
    }

    @Override
    public void update(float dt){
        checkOffScreen();
    }

    public Polygon getShape(){
        return body.getShape();
    }

    public RigidBody getBody() {
        return body;
    }

    private void checkOffScreen(){
        if(body.getTransform().getPosition().x < -100 || body.getTransform().getPosition().x > 1920+100){
            onScreen = false;
        }
        else if(body.getTransform().getPosition().y < -100 || body.getTransform().getPosition().y > 1080+100){
            onScreen = false;
        }
        else
        {
            onScreen = true;
        }
    }

    public boolean isOnScreen() {
        return onScreen;
    }
}
