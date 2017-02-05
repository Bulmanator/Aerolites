package com.teamtwo.aerolites.Entities.AI;

import com.teamtwo.aerolites.Entities.Entity;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * @Aurthor Matthew Threlfall
 */
public class SwarmerBase extends AI {

    public SwarmerBase(World world) {
        this.onScreen = true;
        BodyConfig config = new BodyConfig();


        int x = 0, y = 0, velocityX = 0, velocityY = 0;
        int screenSide = MathUtil.randomInt(0,4);
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

        config.angularVelocity = MathUtil.randomFloat(0, MathUtil.PI / 4f);
        config.position = new Vector2f(x,y);
        config.velocity = new Vector2f(velocityX,velocityY);
        renderColour = Color.WHITE;

        config.shape = new Polygon(MathUtil.randomFloat(40,45));

        body = world.createBody(config);
        body.setData(this);
    }

    @Override
    public void update(float dt){
        super.update(dt);
        if(playerDistance()<MathUtil.square(400)){
            setShooting(true);
        }
    }
    @Override
    public void render(RenderWindow window){
        ConvexShape bodyShape = new ConvexShape(body.getShape().getVertices());
        bodyShape.setPosition(body.getTransform().getPosition());
        bodyShape.setRotation(body.getTransform().getAngle() * MathUtil.RAD_TO_DEG);
        bodyShape.setFillColor(renderColour);
        bodyShape.setTexture(ContentManager.instance.getTexture("Asteroid"));
        window.draw(bodyShape);
    }
    public float playerDistance() {
        float x = entities.get(0).getBody().getTransform().getPosition().x;
        float y = entities.get(0).getBody().getTransform().getPosition().y;

        float xAI = getBody().getTransform().getPosition().x;
        float yAI = getBody().getTransform().getPosition().y;
        return MathUtil.square(x - xAI) + MathUtil.square(y - yAI);
    }

    @Override
    public Type getType() { return Type.SwamerBase; }
}
