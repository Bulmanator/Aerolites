package com.teamtwo.aerolites.Entities.AI;

import com.teamtwo.aerolites.Entities.CollisionMask;
import com.teamtwo.aerolites.Entities.Entity;
import com.teamtwo.engine.Messages.Message;
import com.teamtwo.engine.Messages.Types.CollisionMessage;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;


/**
 * @author Matthew Threlfall
 */
public class Hexaboss extends AI {
    public enum attackPattern{
        spinOne,
        spinTwo,
        alternate
    }

    private float angle;
    private float cooldown;
    private float timeBetweenShots;

    private float lives;
    private attackPattern pattern;
    private float attackTime;
    private float timeRunning;

    private ArrayList<Vector2f> bulletPoints;
    private ArrayList<Float> bulletAngles;

    public Hexaboss(World world){

        BodyConfig config = new BodyConfig();
        this.onScreen = true;

        config.mask = CollisionMask.HEXABOSS;
        config.category = CollisionMask.ALL & (~CollisionMask.ENEMY_BULLET);

        Vector2f[] vertices = new Vector2f[6];
        Vector2f sizes = new Vector2f(16, 9);
        vertices[0] = new Vector2f(-8*sizes.x, -25*sizes.y);
        vertices[1] = new Vector2f(8*sizes.x, -25*sizes.y);
        vertices[2] = new Vector2f(16*sizes.x, 0);
        vertices[3] = new Vector2f(8*sizes.x, 25*sizes.y);
        vertices[4] = new Vector2f(-8*sizes.x, 25*sizes.y);
        vertices[5] = new Vector2f(-16*sizes.x, 0);

        config.shape = new Polygon(vertices);
        config.position = new Vector2f(State.WORLD_SIZE.x/2,-250);
        this.offScreenAllowance = new Vector2f(250,250);

        renderColour = Color.CYAN;

        body = world.createBody(config);
        body.setData(this);

        timeBetweenShots = 5f;
        cooldown = 0;
        lives = 180;

        attackTime = 2;
        timeRunning = 0;
        pattern = attackPattern.spinTwo;

        body.registerObserver(this, Message.Type.Collision);
        bulletPoints = new ArrayList<>();
        bulletAngles = new ArrayList<>();
    }
    @Override
    public void update(float dt){
        cooldown += dt;
        renderColour = new Color((int)(255-(lives*1.41f)),(int)(lives*1.41f),(int)(lives*1.41f));
        if(lives<0){
            onScreen = false;
        }
        if(body.getTransform().getPosition().y>State.WORLD_SIZE.y/2){
            body.setVelocity(new Vector2f(0,0));
            body.setTransform(new Vector2f(State.WORLD_SIZE.x/2, State.WORLD_SIZE.y/2),angle);
            pickPattern(dt);
            attack(dt);
        }
        else
            body.applyForce(new Vector2f(0,5000000));
    }

    public void pickPattern(float dt){
        timeRunning += dt;
        if( timeRunning> attackTime)
        {
            timeRunning = 0;
            int option = MathUtil.randomInt(0,3);
            switch (option){
                case 0:
                    pattern = attackPattern.alternate;
                    break;
                case 1:
                    pattern = attackPattern.spinTwo;
                    break;
                case 2:
                    pattern = attackPattern.spinOne;
                    break;
                case 3:
                    pattern = attackPattern.alternate;
                    break;

            }
        }
    }

    public void attack(float dt){
        switch (pattern) {
            case spinOne:
                timeBetweenShots = 1;
                bulletPoints.clear();
                bulletAngles.clear();
                addFirePoints(0);
                if(cooldown>timeBetweenShots){
                    shooting = true;
                    cooldown = 0;
                }
                angle += (MathUtil.PI/2)*dt;
                break;
            case spinTwo:
                timeBetweenShots = 1;
                bulletPoints.clear();
                bulletAngles.clear();
                addFirePoints(0);
                addFirePoints(2);
                addFirePoints(4);
                if(cooldown>timeBetweenShots){
                    shooting = true;
                    cooldown = 0;
                }
                angle -= (MathUtil.PI/4)*dt;
                break;
            case alternate:
                body.setAngularVelocity(0);
                bulletPoints.clear();
                bulletAngles.clear();
                addFirePoints(0);
                addFirePoints(1);
                addFirePoints(2);
                addFirePoints(3);
                addFirePoints(4);
                addFirePoints(5);

                angle = 0;
                if(cooldown>timeBetweenShots){
                    shooting = true;
                    cooldown = 0;
                }
        }
    }

    @Override
    public Type getType() {
        return Type.Hexaboss;
    }

    @Override
    public void receiveMessage(Message message) {
        if (message.getType() == Message.Type.Collision) {
            CollisionMessage cm = (CollisionMessage) message;
            boolean hit = cm.getBodyA().getData().getType() == Type.Bullet ||  cm.getBodyB().getData().getType() == Type.Bullet;
            if(hit) {
                lives--;
            }
        }
    }
    private void addFirePoints(int face){
        Vector2f pointOne;
        Vector2f pointTwo;
        switch (face) {
            case 5:
                pointOne = getBody().getShape().getTransformed()[0];
                pointTwo = getBody().getShape().getTransformed()[5];
                break;
            default:
                pointOne = getBody().getShape().getTransformed()[face];
                pointTwo = getBody().getShape().getTransformed()[face+1];
                break;
        }

        float xAdd = 40*MathUtil.sin((face)*60*MathUtil.DEG_TO_RAD);
        float yAdd = -40*MathUtil.cos((face)*60*MathUtil.DEG_TO_RAD);

        pointOne = new Vector2f(pointOne.x+xAdd, pointOne.y+yAdd);
        pointTwo = new Vector2f(pointTwo.x+xAdd, pointTwo.y+yAdd);
        bulletPoints.add(pointOne);
        bulletPoints.add(pointTwo);

        Vector2f mid = MathUtil.midPoint(pointOne, pointTwo);
        bulletPoints.add(mid);

        Vector2f mid2_1 = MathUtil.midPoint(mid, pointTwo);
        bulletPoints.add(mid2_1);

        Vector2f mid2_2 = MathUtil.midPoint(mid, pointOne);
        bulletPoints.add(mid2_2);

        Vector2f mid3_1 = MathUtil.midPoint(mid2_1, pointTwo);
        bulletPoints.add(mid3_1);

        Vector2f mid3_2 = MathUtil.midPoint(mid, mid2_1);
        bulletPoints.add(mid3_2);

        Vector2f mid3_3 = MathUtil.midPoint(mid, mid2_2);
        bulletPoints.add(mid3_3);

        Vector2f mid3_4 = MathUtil.midPoint(mid2_2, pointOne);
        bulletPoints.add(mid3_4);
        for(int i = 0; i < 9; i++)
            bulletAngles.add((face)*60*MathUtil.DEG_TO_RAD);

    }


    public ArrayList<Vector2f> getBulletPoints() {
        return bulletPoints;
    }

    public ArrayList<Float> getBulletAngles() {
        return bulletAngles;
    }

    public void setShooting(boolean shooting){
        this.shooting = shooting;
    }
}
