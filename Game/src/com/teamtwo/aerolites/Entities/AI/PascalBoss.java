package com.teamtwo.aerolites.Entities.AI;

import com.teamtwo.aerolites.Entities.CollisionMask;
import com.teamtwo.engine.Graphics.Particles.ParticleConfig;
import com.teamtwo.engine.Graphics.Particles.ParticleEmitter;
import com.teamtwo.engine.Messages.Message;
import com.teamtwo.engine.Messages.Types.CollisionMessage;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;

/**
 * @author Matthew Threlfall
 */
public class PascalBoss extends AI {

    private static final Vector2f[] vertices = new Vector2f[] {
            Vector2f.ZERO, new Vector2f(250, 0), new Vector2f(125, 217)
    };

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
    private float fadeout;

    private ParticleEmitter damage;

    private ArrayList<Vector2f> bulletPoints;
    private ArrayList<Float> bulletAngles;

    public PascalBoss(World world, int lives, boolean second) {

        this.second = second;
        maxSpeed = 300f;

        this.lives = lives;
        totalLives = lives;
        onScreen = true;
        inPlace = false;
        offScreenAllowance = new Vector2f(170,170);

        BodyConfig config = new BodyConfig();
        ParticleConfig pConfig = new ParticleConfig();

        pConfig.minAngle = 0;
        pConfig.maxAngle = 360;
        pConfig.speed = 20;
        pConfig.rotationalSpeed = 80;
        pConfig.pointCount = 3;
        pConfig.fadeOut = true;
        pConfig.startSize = 20;
        pConfig.endSize = 12;
        pConfig.minLifetime = 1.5f;
        pConfig.maxLifetime = 3;

        Color colour;
        if(second) {
            config.position = new Vector2f(State.WORLD_SIZE.x/2 - 80, -150);
            pConfig.position = config.position;
            colour = new Color(255, 153, 0);
            pConfig.colours[0] = colour;
            defaultColor = colour;
            direction = -1;
            angle = 0;
        }
        else {
            config.position = new Vector2f(State.WORLD_SIZE.x/2 + 80, -150);
            pConfig.position = config.position;
            colour = new Color(36, 25, 178);
            pConfig.colours[0] = colour;
            direction = 1;
            defaultColor = colour;
            angle = 180 * MathUtil.DEG_TO_RAD;
        }

        damage = new ParticleEmitter(pConfig,150,200);

        position = new Vector2f(config.position.x, State.WORLD_SIZE.y / 2);

        config.shape = new Polygon(vertices);

        config.category = CollisionMask.PASCALBOSS;
        config.mask = CollisionMask.ALL & (~CollisionMask.ENEMY_BULLET);

        body = world.createBody(config);
        body.setData(this);
        body.registerObserver(this, Message.Type.Collision);

        body.setTransform(config.position, angle);

        display = new ConvexShape(body.getShape().getVertices());

        display.setOutlineThickness(-8f);
        display.setOutlineColor(colour);
        display.setFillColor(colour);

        bulletPoints = new ArrayList<>();
        bulletAngles = new ArrayList<>();
        fireFace = MathUtil.randomInt(0, 3);
        shootCoolDown = 3f;
        shootTimer = 0;
        lastHit = 2;
        barrage = 0;
        fadeout  = 100;
    }

    public void update(float dt) {

        lastHit += dt;

        updateParticles();
        damage.update(dt);

        if(lastHit < 0.1f) {
            display.setFillColor(Color.WHITE);
        }
        else {
            display.setOutlineColor(defaultColor);
        }

        if(lives < 0) {
            onScreen = false;
            alive = false;
        }

        if(inPlace) {
            body.setVelocity(Vector2f.ZERO);
            body.setTransform(new Vector2f(position.x, position.y), angle);
            move(dt);
            attack(dt);
        }
        else if(body.getTransform().getPosition().y < State.WORLD_SIZE.y / 2){
            body.applyForce(new Vector2f(0, 10000000));
            limitSpeed();
            fadeout -= 30 * dt;
            ContentManager.instance.getMusic("PlayMusic").setVolume(fadeout);
        }
        else if(!inPlace) {
            body.setVelocity(new Vector2f(body.getVelocity().x, 0));
            limitSpeed();
            body.applyForce(new Vector2f(10000000 * direction, 0));
            fadeout -= 30 * dt;
            ContentManager.instance.getMusic("PlayMusic").setVolume(fadeout);
            if(second) {
                if(body.getTransform().getPosition().x < State.WORLD_SIZE.x / 6 * 1.5f) {
                    position = new Vector2f(State.WORLD_SIZE.x / 6 * 1.5f, position.y);
                    inPlace = true;
                    ContentManager.instance.getMusic("PlayMusic").stop();
                    //ContentManager.instance.getMusic("Pascal").play();
                }
            }
            else {
                if(body.getTransform().getPosition().x > State.WORLD_SIZE.x / 6 * 4.5f) {
                    position = new Vector2f(State.WORLD_SIZE.x / 6 * 4.5f, position.y);
                    inPlace = true;
                    ContentManager.instance.getMusic("PlayMusic").stop();
                    //ContentManager.instance.getMusic("Pascal").play();
                }
            }
        }
    }

    private void move(float dt){
        float turnSpeed = 30 * MathUtil.DEG_TO_RAD * dt;
        float x = State.WORLD_SIZE.x / 2f + (position.x - State.WORLD_SIZE.x / 2f) * MathUtil.cos(turnSpeed)
                - (position.y - State.WORLD_SIZE.y / 2f) * MathUtil.sin(turnSpeed);

        float y = State.WORLD_SIZE.y / 2f + (position.x - State.WORLD_SIZE.x / 2f) * MathUtil.sin(turnSpeed)
                + (position.y - State.WORLD_SIZE.y / 2f) * MathUtil.cos(turnSpeed);

        position = new Vector2f(x, y);
        body.setVelocity(Vector2f.ZERO);
        angle += direction * 30 * dt * MathUtil.DEG_TO_RAD;
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
                display.setOutlineColor(Color.WHITE);
                lastHit = 0;
            }
        }
    }

    @Override
    public void render(RenderWindow renderer) {
        damage.render(renderer);

        if(shootCoolDown > shootTimer) {
            RectangleShape mark;
            for(Vector2f v : bulletPoints) {
                mark = new RectangleShape();
                mark.setFillColor(new Color(255,0,0));
                mark.setSize(new Vector2f(3,4));
                mark.setPosition(v);
                renderer.draw(mark);
            }
        }

        CircleShape health = new CircleShape(120, 3);
        health.setOrigin(120, 120);

        float lifeRatio = 1 - (lives / (float) totalLives);
        Color fill;
        if(second) {
            fill = new Color(255, (int) MathUtil.lerp(153, 0, lifeRatio), 0);
        }
        else {
            fill = new Color((int) MathUtil.lerp(36, 255, lifeRatio),
                    (int) MathUtil.lerp(25, 0, lifeRatio), (int) MathUtil.lerp(178, 0, lifeRatio));
        }

        display.setFillColor(fill);
        super.render(renderer);
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
            bulletAngles.add((face) * 120 * MathUtil.DEG_TO_RAD);
    }

    private void updateParticles() {
        damage.getConfig().position = body.getTransform().getPosition();

        float lifeRatio = 1 - (lives / (float) totalLives);
        float invLifeRatio = (totalLives / (float) lives);

        damage.getConfig().endSize = MathUtil.lerp(6, 1, lifeRatio);
        damage.getConfig().speed = MathUtil.clamp(20 * 0.5f * invLifeRatio, 100, 200);
        damage.getConfig().maxLifetime = MathUtil.lerp(3, 1.5f, lifeRatio);
        damage.getConfig().minLifetime = MathUtil.lerp(1.5f, 0.3f, lifeRatio);
        damage.setEmissionRate(300 * invLifeRatio * invLifeRatio);

        int r, g, b;
        if(!second) {
            r = 255;
            g = (int) MathUtil.lerp(153, 0, lifeRatio);
            b = 0;
        }
        else {
            r = (int) MathUtil.lerp(36, 255, lifeRatio);
            g = (int) MathUtil.lerp(25, 0, lifeRatio);
            b = (int) MathUtil.lerp(178, 0, lifeRatio);
        }
        damage.getConfig().colours[1] = new Color(r, g, b);
    }

    @Override
    public Type getType() {
        return Type.PascalBoss;
    }

    public ArrayList<Vector2f> getBulletPoints() { return bulletPoints; }

    public ArrayList<Float> getBulletAngles() { return bulletAngles; }
    public void setShooting(boolean shooting) { this.shooting = shooting; }
}
