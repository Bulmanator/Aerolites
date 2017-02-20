package com.teamtwo.aerolites.Entities.AI;

import com.teamtwo.aerolites.Entities.CollisionMask;
import com.teamtwo.engine.Messages.Message;
import com.teamtwo.engine.Messages.Types.CollisionMessage;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;

/**
 * @author Matthew Threlfall
 */
public class PascalBoss extends AI {

    private float lives;
    private boolean inPlace;
    private float angle;
    private Vector2f position;
    private int direction;
    private int fireFace;
    private float shootCoolDown;
    private float shootTimer;
    private float lastHit;
    private Color defaultColor;
    private int barrage;
    private boolean second;

    private int totalLives;



    private ArrayList<Vector2f> bulletPoints;
    private ArrayList<Float> bulletAngles;

    public PascalBoss(World world, int lives, boolean second){

        this.second = second;

        this.lives = lives;
        totalLives = lives;
        onScreen = true;
        inPlace = false;
        offScreenAllowance = new Vector2f(60,60);

        BodyConfig config = new BodyConfig();
        if(second) {
            config.position = new Vector2f(State.WORLD_SIZE.x/2 - 80, 0);
            renderColour = new Color(255,153,0);
            defaultColor = renderColour;
            direction = -1;
            angle = 0;
        }
        else {
            config.position = new Vector2f(State.WORLD_SIZE.x/2 + 80, 0);
            renderColour = new Color(36,25,178);
            direction = 1;
            defaultColor = renderColour;
            angle = 180 * MathUtil.DEG_TO_RAD;
        }

        position = new Vector2f(config.position.x,State.WORLD_SIZE.y/2);

        Vector2f[] vertices = new Vector2f[3];
        vertices[0] = new Vector2f(0,0);
        vertices[1] = new Vector2f(250, 0);
        vertices[2] = new Vector2f(125, 217);

        config.shape = new Polygon(vertices);

        config.category = CollisionMask.PASCALBOSS;
        config.mask = CollisionMask.ALL & (~CollisionMask.ENEMY_BULLET);

        body = world.createBody(config);
        body.setData(this);
        body.registerObserver(this, Message.Type.Collision);

        body.setTransform(config.position,angle);
        bulletPoints = new ArrayList<>();
        bulletAngles = new ArrayList<>();
        fireFace = MathUtil.randomInt(0,3);
        shootCoolDown = 3f;
        shootTimer = 0;
        lastHit = 2;
        barrage = 0;
    }

    public void update(float dt) {
        lastHit+=dt;
        if(lastHit<0.1f){
            renderColour = Color.WHITE;
        }
        else
            renderColour = defaultColor;

        if(lives < 0){
            onScreen = false;
            alive = false;
        }

        if(inPlace){
            body.setVelocity(new Vector2f(0,0));
            body.setTransform(new Vector2f(position.x, position.y), angle);
            move(dt);
            attack(dt);
        }
        else if(body.getTransform().getPosition().y < State.WORLD_SIZE.y/2){
            body.applyForce(new Vector2f(0, 3000000));
        }
        else if(!inPlace) {
            body.setVelocity(new Vector2f(body.getVelocity().x,0));
            body.applyForce(new Vector2f(3000000*direction, 0));
            if(second) {
                if(body.getTransform().getPosition().x < State.WORLD_SIZE.x/6*1.5) {
                    position = new Vector2f(State.WORLD_SIZE.x/6*1.5f, position.y);
                    inPlace = true;
                }
            }
            else {
                if(body.getTransform().getPosition().x > State.WORLD_SIZE.x/6*4.5) {
                    position = new Vector2f(State.WORLD_SIZE.x/6*4.5f, position.y);
                    inPlace = true;
                }
            }
        }
    }

    private void move(float dt){
        float turnSpeed = 30 * MathUtil.DEG_TO_RAD * dt;
        float x = State.WORLD_SIZE.x/2 + (position.x-State.WORLD_SIZE.x/2)*MathUtil.cos(turnSpeed) - (position.y-State.WORLD_SIZE.y/2)*MathUtil.sin(turnSpeed);
        float y = State.WORLD_SIZE.y/2 + (position.x-State.WORLD_SIZE.x/2)*MathUtil.sin(turnSpeed) + (position.y-State.WORLD_SIZE.y/2)*MathUtil.cos(turnSpeed);
        position = new Vector2f(x, y);
        body.setVelocity(new Vector2f(0,0));
        angle += direction*30*dt *MathUtil.DEG_TO_RAD;
    }
    private void attack(float dt){
        shootTimer += dt;
        bulletAngles.clear();
        bulletPoints.clear();
        addFirePoints(fireFace);
        if(shootTimer>shootCoolDown) {
            shooting = true;
            if(shootCoolDown == 3)
                shootCoolDown = 0.1f;
            else if(barrage < 3){
                barrage++;
            }
            else {
                shootCoolDown = 3;
                barrage = 0;
                fireFace = MathUtil.randomInt(0, 3);
            }

            shootTimer = 0;
        }

    }


    @Override
    public void receiveMessage(Message message) {
        if (message.getType() == Message.Type.Collision) {
            CollisionMessage cm = (CollisionMessage) message;
            boolean damange = cm.getBodyA().getData().getType() == Type.Bullet || cm.getBodyB().getData().getType() == Type.Bullet;
            if(damange) {
                lives--;
                renderColour = Color.WHITE;
                lastHit = 0;
            }
        }
    }

    @Override
    public void render(RenderWindow window){
        super.render(window);
        if(shootCoolDown>shootTimer)
        {
            RectangleShape mark;
            for(Vector2f v: bulletPoints) {
                mark = new RectangleShape();
                mark.setFillColor(new Color(255,0,0));
                mark.setSize(new Vector2f(3,4));
                mark.setPosition(v);
                window.draw(mark);
            }
        }
        CircleShape health = new CircleShape(120, 3);
        health.setOrigin(120,120);
        Color fill;
        if(second){
            fill = new Color(255,(int)MathUtil.lerp(153,0,1-(lives/totalLives)),0);
        }
        else
            fill = new Color((int)MathUtil.lerp(36,255,1-(lives/totalLives)),(int)MathUtil.lerp(25,0,1-(lives/totalLives)),(int)MathUtil.lerp(178,0,1-(lives/totalLives)));
        health.setFillColor(fill);
        health.setPosition(body.getTransform().getPosition());
        health.setRotation((body.getTransform().getAngle()+MathUtil.PI)*MathUtil.RAD_TO_DEG);
        window.draw(health);
    }

    private void addFirePoints(int face) {
        Vector2f pointOne;
        Vector2f pointTwo;
        switch (face) {
            case 2:
                pointOne = getBody().getShape().getTransformed()[0];
                pointTwo = getBody().getShape().getTransformed()[2];
                break;
            default:
                pointOne = getBody().getShape().getTransformed()[face];
                pointTwo = getBody().getShape().getTransformed()[face + 1];
                break;
        }

        float xAdd = 40 * MathUtil.sin(angle + (face) * 120 * MathUtil.DEG_TO_RAD);
        float yAdd = -40 * MathUtil.cos(angle + (face) * 120 * MathUtil.DEG_TO_RAD);

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

        for(int i = 0; i < 5; i++)
            bulletAngles.add((face)*120*MathUtil.DEG_TO_RAD);

    }

    @Override
    public Type getType() {
        return Type.PascalBoss;
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
