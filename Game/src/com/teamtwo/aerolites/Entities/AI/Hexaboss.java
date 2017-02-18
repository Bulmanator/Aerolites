package com.teamtwo.aerolites.Entities.AI;

import com.teamtwo.aerolites.Entities.CollisionMask;
import com.teamtwo.engine.Messages.Message;
import com.teamtwo.engine.Messages.Types.CollisionMessage;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;

import static com.teamtwo.aerolites.Entities.AI.Hexaboss.AttackPattern.Wait;


/**
 * @author Matthew Threlfall
 */
public class Hexaboss extends AI {

    public enum AttackPattern{
        SpinOne,
        SpinTwo,
        Triforce,
        StandOne,
        Wait
    }

    private float angle;
    private float cooldown;
    private float timeBetweenShots;

    private float lives;
    private float totalLives;

    private AttackPattern pattern;
    //music stuff
    private boolean inPlace;
    private float fadeout;

    private attackPattern pattern;
    private float attackTime;
    private float timeRunning;
    private boolean waitNeeded;
    private float warningTime;
    private float warnTimer;
    private float waitTurnSpeed;


    private ArrayList<Vector2f> bulletPoints;
    private ArrayList<Float> bulletAngles;

    public Hexaboss(World world, int lives) {

        this.lives = lives;
        totalLives = lives;

        this.onScreen = true;
        waitNeeded = false;
        inPlace = false;
        fadeout = 10;

        BodyConfig config = new BodyConfig();

        config.category = CollisionMask.HEXABOSS;
        config.mask = CollisionMask.ALL & (~CollisionMask.ENEMY_BULLET);

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

        body = world.createBody(config);
        body.setData(this);
        body.registerObserver(this, Message.Type.Collision);

        this.offScreenAllowance = new Vector2f(250,250);
        renderColour = Color.CYAN;

        timeBetweenShots = 5f;
        cooldown = 0;

        warningTime = 2;
        warnTimer = 9;
        attackTime = 0;
        timeRunning = 0;
        pattern = AttackPattern.Wait;

        bulletPoints = new ArrayList<>();
        bulletAngles = new ArrayList<>();
    }
    @Override
    public void update(float dt){
        cooldown += dt;

        renderColour = new Color((int)MathUtil.lerp(0,255,1-(lives/totalLives)),(int)MathUtil.lerp(255,0,1-(lives/totalLives)),(int)MathUtil.lerp(255,0,1-(lives/totalLives)));
        if(lives<0){
            onScreen = false;
            alive = false;
        }
        if(body.getTransform().getPosition().y>State.WORLD_SIZE.y/2){
            if(!inPlace)
            {
                inPlace = true;
                ContentManager.instance.getMusic("hexagon").play();
                ContentManager.instance.getMusic("hexagon").setVolume(10f);
                ContentManager.instance.getMusic("hexagon").setLoop(true);
                ContentManager.instance.getMusic("playMusic").stop();
            }
            body.setVelocity(new Vector2f(0,0));
            body.setTransform(new Vector2f(State.WORLD_SIZE.x/2, State.WORLD_SIZE.y/2),angle);
            pickPattern(dt);
            attack(dt);
        }
        else {
            body.applyForce(new Vector2f(0, 5000000));
            ContentManager.instance.getMusic("playMusic").setVolume(fadeout);
            fadeout -= 0.3f;
        }
    }

    public void pickPattern(float dt){
        timeRunning += dt;
        if(timeRunning> attackTime+warningTime)
        {
            timeRunning = 0;
            if(waitNeeded){
                pattern = Wait;
                waitNeeded = false;
                waitTurnSpeed = (MathUtil.PI/8)*dt*MathUtil.randomInt(-4,4);
                attackTime = 0.5f;
            }
            else {
                waitNeeded = true;
                attackTime = 2;
                int option = MathUtil.randomInt(0, 4);
                switch (option) {
                    case 0:
                        warnTimer = 0;
                        warningTime = 2;
                        timeBetweenShots = 0.18f;
                        attackTime = 6;
                        pattern = attackPattern.spinTwo;
                        break;
                    case 1:
                        warnTimer = 0;
                        warningTime = 2;
                        timeBetweenShots = 0.25f;
                        attackTime = 6;
                        pattern = attackPattern.spinOne;
                        break;
                    case 2:
                        warnTimer = 0;
                        warningTime = 2;
                        timeBetweenShots = 0.18f;
                        attackTime = 3;
                        pattern = attackPattern.triforce;
                        break;
                    case 3:
                        warnTimer = 0;
                        warningTime = 1;
                        timeBetweenShots = 0.25f;
                        attackTime = 9;
                        pattern = attackPattern.standOne;
                        break;

                }
            }
        }
    }

    public void attack(float dt){
        switch (pattern) {
            case SpinOne:
                warnTimer+=dt;

                bulletPoints.clear();
                bulletAngles.clear();
                addFirePoints(0);
                addFirePoints(1);
                addFirePoints(3);
                addFirePoints(4);

                if(cooldown>timeBetweenShots && warningTime < warnTimer){
                    shooting = true;
                    cooldown = 0;
                }
                angle += (MathUtil.PI/2)*dt*MathUtil.sin(timeRunning*5);
                break;
            case SpinTwo:

                warnTimer+=dt;

                bulletPoints.clear();
                bulletAngles.clear();
                addFirePoints(0);
                addFirePoints(1);
                addFirePoints(2);


                if(cooldown>timeBetweenShots && warningTime < warnTimer){
                    shooting = true;
                    cooldown = 0;
                }
                angle -= (MathUtil.PI/3)*dt;
                break;
            case triforce:
                angle += (MathUtil.PI/3)*dt;
                warnTimer += dt;

                bulletPoints.clear();
                bulletAngles.clear();
                addFirePoints(0);
                addFirePoints(2);
                addFirePoints(4);


                if(cooldown>timeBetweenShots && warningTime < warnTimer){
                    shooting = true;
                    cooldown = 0;
                }
                angle -= (MathUtil.PI/4)*dt;
                break;
            case standOne:
                body.setAngularVelocity(0);
                if(timeRunning>6){
                    bulletPoints.clear();
                    bulletAngles.clear();
                    addFirePoints(0);
                    addFirePoints(1);
                    addFirePoints(3);
                    addFirePoints(4);
                    warningTime = 3;

                }
                else if(timeRunning > 3) {
                    bulletPoints.clear();
                    bulletAngles.clear();
                    addFirePoints(1);
                    addFirePoints(2);
                    addFirePoints(4);
                    addFirePoints(5);
                    warningTime = 2;

                }
                else if(timeRunning < 3){
                    bulletPoints.clear();
                    bulletAngles.clear();
                    addFirePoints(2);
                    addFirePoints(3);
                    addFirePoints(5);
                    addFirePoints(0);
                    warningTime = 1;
                }
                if(warningTime > warnTimer)
                    warnTimer+=dt;
                else if(cooldown>timeBetweenShots){
                    shooting = true;
                    cooldown = 0;
                }

                break;
            case Wait:
                angle -= waitTurnSpeed;
                break;
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
            boolean hit = cm.getBodyA().getData().getType() == Type.Bullet || cm.getBodyB().getData().getType() == Type.Bullet;
            if(hit) {
                lives--;
            }
        }
    }
    private void addFirePoints(int face) {
        Vector2f pointOne;
        Vector2f pointTwo;
        switch (face) {
            case 5:
                pointOne = getBody().getShape().getTransformed()[0];
                pointTwo = getBody().getShape().getTransformed()[5];
                break;
            default:
                pointOne = getBody().getShape().getTransformed()[face];
                pointTwo = getBody().getShape().getTransformed()[face + 1];
                break;
        }

        float xAdd = 40 * MathUtil.sin(angle + (face) * 60 * MathUtil.DEG_TO_RAD);
        float yAdd = -40 * MathUtil.cos(angle + (face) * 60 * MathUtil.DEG_TO_RAD);

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
    @Override
    public void render(RenderWindow window){
        super.render(window);
        if(warningTime>warnTimer)
        {
            RectangleShape warning;
            for(Vector2f v: bulletPoints) {
                warning = new RectangleShape();
                warning.setFillColor(new Color(255,0,0));
                warning.setSize(new Vector2f(4,4));
                warning.setPosition(v);
                window.draw(warning);
            }
        }
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

    public void setLives(int players){
        lives = 360*players;
        totalLives = 360*players;
    }

    public boolean isAlive() { return alive; }
}
