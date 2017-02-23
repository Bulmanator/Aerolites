package com.teamtwo.aerolites.Entities.AI;

import com.teamtwo.aerolites.Entities.Bullet;
import com.teamtwo.aerolites.Entities.CollisionMask;
import com.teamtwo.aerolites.Entities.Entity;
import com.teamtwo.engine.Graphics.Particles.ParticleConfig;
import com.teamtwo.engine.Graphics.Particles.ParticleEmitter;
import com.teamtwo.engine.Messages.Message;
import com.teamtwo.engine.Messages.Types.CollisionMessage;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.Interfaces.Disposable;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.audio.Music;
import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;

import static com.teamtwo.aerolites.Entities.AI.Hexaboss.AttackPattern.Wait;


/**
 * @author Matthew Threlfall
 */
public class Hexaboss extends AI implements Disposable {

    public enum AttackPattern {
        SpinOne,
        SpinTwo,
        Triforce,
        StandOne,
        Wait
    }

    private static final Vector2f[] vertices = new Vector2f[] {
            new Vector2f(-8 * 16, -25 * 9), new Vector2f(8 * 16, -25 * 9),
            new Vector2f(16 * 16, 0), new Vector2f(8 * 16, 25 * 9),
            new Vector2f(-8 * 16, 25 * 9), new Vector2f(-16 * 16, 0)
    };

    private float angle;
    private float cooldown;
    private float timeBetweenShots;

    private float lives;
    private float totalLives;

    private ParticleEmitter damage;

    //music stuff
    private boolean inPlace;
    private float fadeout;

    private AttackPattern pattern;
    private float attackTime;
    private float timeRunning;
    private boolean waitNeeded;
    private float warningTime;
    private float warnTimer;
    private float waitTurnSpeed;
    private float lastHit;
    private int lastAttack;


    private ArrayList<Vector2f> bulletPoints;
    private ArrayList<Float> bulletAngles;

    private ArrayList<Bullet> bullets;

    public Hexaboss(World world, int lives) {

        this.lives = lives;
        totalLives = lives;

        this.onScreen = true;
        waitNeeded = false;
        inPlace = false;
        fadeout = 100;

        BodyConfig config = new BodyConfig();

        config.category = CollisionMask.HEXABOSS;
        config.mask = CollisionMask.ALL & (~CollisionMask.ENEMY_BULLET);

        config.shape = new Polygon(vertices);
        config.position = new Vector2f(State.WORLD_SIZE.x / 2, -250);

        body = world.createBody(config);
        body.setData(this);
        body.registerObserver(this, Message.Type.Collision);

        this.offScreenAllowance = new Vector2f(250, 250);

        display = new ConvexShape(body.getShape().getVertices());
        display.setFillColor(Color.CYAN);

        timeBetweenShots = 5f;
        cooldown = 0;
        lastHit = 0;

        warningTime = 0;
        warnTimer = 0;
        attackTime = 0;
        timeRunning = 0;
        pattern = AttackPattern.Wait;

        bulletPoints = new ArrayList<>();
        bulletAngles = new ArrayList<>();

        bullets = new ArrayList<>();

        lastAttack = 0;

        ParticleConfig pConfig = new ParticleConfig();

        pConfig.minAngle = 0;
        pConfig.maxAngle = 360;
        pConfig.speed = 100;
        pConfig.rotationalSpeed = 80;
        pConfig.pointCount = 6;
        pConfig.fadeOut = true;
        pConfig.startSize = 20;
        pConfig.endSize = 12;
        pConfig.minLifetime = 1.5f;
        pConfig.maxLifetime = 3;

        pConfig.colours[0] = Color.YELLOW;
        pConfig.colours[1] = Color.RED;

        pConfig.position = body.getTransform().getPosition();

        damage = new ParticleEmitter(pConfig,300,600);
    }
    private void updateParticles() {
        damage.getConfig().position = body.getTransform().getPosition();
        float lifeRatio = 1 - (lives / totalLives);
        float invLifeRatio = (totalLives / lives);

        damage.getConfig().endSize = MathUtil.lerp(12, 1, lifeRatio);
        damage.getConfig().speed = MathUtil.clamp(200 * 0.5f * invLifeRatio, 100, 300);
        damage.getConfig().maxLifetime = MathUtil.lerp(3,1.5f, lifeRatio);
        damage.getConfig().minLifetime = MathUtil.lerp(1.5f,0.3f, lifeRatio);
        damage.setEmissionRate(300 * invLifeRatio * invLifeRatio);

        int r = (int) MathUtil.lerp(0, 255, lifeRatio);
        int g = (int) MathUtil.lerp(255, 0, lifeRatio);
        int b = 0;

        damage.getConfig().colours[0] = new Color(r, g, b);

        b = (int) MathUtil.lerp(255, 0, lifeRatio);
        damage.getConfig().colours[1] = new Color(r, g, b);
    }

    @Override
    public void update(float dt) {
        cooldown += dt;
        lastHit+=dt;
        damage.update(dt);
        updateParticles();

        if(lastHit > 0.03f) {
            float value = 1 - (lives / totalLives);
            int r = (int) MathUtil.lerp(0, 255, value);
            int g = (int) MathUtil.lerp(255, 0, value);
            int b = (int) MathUtil.lerp(255, 0, value);
            display.setFillColor(new Color(r, g, b));
        }

        if(lives < 0) {
            onScreen = false;
            alive = false;
        }

        if(body.getTransform().getPosition().y > State.WORLD_SIZE.y / 2) {
            if(!inPlace) {
                inPlace = true;
                Music hexagon = ContentManager.instance.getMusic("Hexagon");
                hexagon.play();
                hexagon.setVolume(100f);
                hexagon.setLoop(true);
                ContentManager.instance.getMusic("PlayMusic").stop();
            }
            body.setVelocity(new Vector2f(0,0));
            body.setTransform(new Vector2f(State.WORLD_SIZE.x / 2, State.WORLD_SIZE.y / 2), angle);
            pickPattern(dt);
            attack(dt);
        }
        else {
            body.applyForce(new Vector2f(0, 5000000));
            ContentManager.instance.getMusic("PlayMusic").setVolume(fadeout);
            fadeout -= 35f * dt;
        }

        if(shooting) {
            for (int i = 0; i < bulletPoints.size(); i++) {
                Vector2f position = bulletPoints.get(i);
                float angle = bulletAngles.get(i);
                Bullet bullet = new Bullet(10f, position, Entity.Type.EnemyBullet,
                        body.getTransform().getAngle() + angle, body.getWorld());
                bullet.setMaxSpeed(250);
                bullets.add(bullet);
            }
            shooting = false;
        }

        for(int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if(bullet.isOnScreen()) {
                bullet.update(dt);
            }
            else {
                if(!bullets.remove(bullet)) {
                    throw new Error("Error: Failed to remove bullet");
                }

                body.getWorld().removeBody(bullet.getBody());

                i--;
            }
        }
    }

    private void pickPattern(float dt) {
        timeRunning += dt;
        if(timeRunning > attackTime + warningTime) {

            timeRunning = 0;

            if(waitNeeded) {
                pattern = Wait;
                waitNeeded = false;
                waitTurnSpeed = (MathUtil.PI/8)*dt*MathUtil.randomInt(-4,4);
                attackTime = 0.2f;
            }
            else {
                waitNeeded = true;
                attackTime = 2;
                int option = MathUtil.randomInt(0, 4);
                while(option == lastAttack)
                    option = MathUtil.randomInt(0, 4);
                lastAttack = option;
                switch (option) {
                    case 0:
                        warnTimer = 0;
                        warningTime = 1;
                        timeBetweenShots = 0.18f;
                        attackTime = 6;
                        pattern = AttackPattern.SpinTwo;
                        break;
                    case 1:
                        warnTimer = 0;
                        warningTime = 1;
                        timeBetweenShots = 0.25f;
                        attackTime = 6;
                        pattern = AttackPattern.SpinOne;
                        break;
                    case 2:
                        warnTimer = 0;
                        warningTime = 2;
                        timeBetweenShots = 0.18f;
                        attackTime = 6;
                        pattern = AttackPattern.Triforce;
                        break;
                    case 3:
                        warnTimer = 0;
                        warningTime = 0.6f;
                        timeBetweenShots = 0.25f;
                        attackTime = 9;
                        pattern = AttackPattern.StandOne;
                        break;

                }
            }
        }
    }

    public void attack(float dt){
        switch (pattern) {
            case SpinOne:
                warnTimer += dt;

                bulletPoints.clear();
                bulletAngles.clear();
                addFirePoints(0);
                addFirePoints(1);
                addFirePoints(3);
                addFirePoints(4);

                if(cooldown > timeBetweenShots && warningTime < warnTimer){
                    shooting = true;
                    cooldown = 0;
                }
                angle += (MathUtil.PI / 2f) * dt * MathUtil.sin(timeRunning * 5);
                break;
            case SpinTwo:

                warnTimer += dt;

                bulletPoints.clear();
                bulletAngles.clear();
                addFirePoints(0);
                addFirePoints(1);
                addFirePoints(2);


                if(cooldown>timeBetweenShots && warningTime < warnTimer){
                    shooting = true;
                    cooldown = 0;
                }
                angle -= (MathUtil.PI / 3f) * dt;
                break;
            case Triforce:
                warnTimer += dt;

                bulletPoints.clear();
                bulletAngles.clear();
                addFirePoints(0);
                addFirePoints(2);
                addFirePoints(4);


                if(cooldown > timeBetweenShots && warningTime < warnTimer) {
                    shooting = true;
                    cooldown = 0;
                }
                angle -= (MathUtil.PI / 2.5f) * dt;
                break;
            case StandOne:
                body.setAngularVelocity(0);
                if(timeRunning > 6) {
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
                else if(timeRunning < 3) {
                    bulletPoints.clear();
                    bulletAngles.clear();
                    addFirePoints(2);
                    addFirePoints(3);
                    addFirePoints(5);
                    addFirePoints(0);
                    warningTime = 1;
                }

                if(warningTime > warnTimer) {
                    warnTimer += dt;
                }
                else if(cooldown > timeBetweenShots){
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
            boolean hit = cm.getBodyA().getData().getType() == Type.Bullet
                    || cm.getBodyB().getData().getType() == Type.Bullet;

            if(hit) {
                lives--;
                display.setFillColor(Color.WHITE);
                lastHit = 0;
            }
        }
    }

    private void addFirePoints(int face) {

        Vector2f pointOne = body.getShape().getTransformed()[(face + 1) % 6];
        Vector2f pointTwo = body.getShape().getTransformed()[face];

        float xAdd = 40 * MathUtil.sin(angle + (face) * 60 * MathUtil.DEG_TO_RAD);
        float yAdd = -40 * MathUtil.cos(angle + (face) * 60 * MathUtil.DEG_TO_RAD);

        pointOne = new Vector2f(pointOne.x + xAdd, pointOne.y + yAdd);
        pointTwo = new Vector2f(pointTwo.x + xAdd, pointTwo.y + yAdd);
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
            bulletAngles.add((face) * 60 * MathUtil.DEG_TO_RAD);

    }
    @Override
    public void render(RenderWindow renderer) {
        damage.render(renderer);

        super.render(renderer);

        if(warningTime > warnTimer) {
            CircleShape warning;
            for(Vector2f position : bulletPoints) {
                warning = new CircleShape(4f);
                warning.setFillColor(Color.RED);
                warning.setPosition(position);
                renderer.draw(warning);
            }
        }

        for(Bullet bullet : bullets) {
            bullet.render(renderer);
        }
    }

    @Override
    public void dispose() {
        for(Bullet bullet : bullets) {
            body.getWorld().removeBody(bullet.getBody());
        }

        bulletPoints.clear();
        bulletAngles.clear();
        bullets.clear();
    }

    public ArrayList<Vector2f> getBulletPoints() {
        return bulletPoints;
    }

    public ArrayList<Float> getBulletAngles() {
        return bulletAngles;
    }

    public void setShooting(boolean shooting) { this.shooting = shooting; }

    public void setLives(int players){
        lives = 360*players;
        totalLives = 360*players;
    }

    public boolean isAlive() { return alive; }
}
