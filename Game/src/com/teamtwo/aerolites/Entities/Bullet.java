package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Messages.Message;
import com.teamtwo.engine.Messages.Types.CollisionMessage;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * @author Matthew Threlfall
 */
public class Bullet extends Entity {

    private float lifeTime;
    private final float MAX_LIFE_TIME;
    private Type owner;
    private int bulletOwner;
    private boolean hit;
    private boolean asteroid;
    private boolean enemy;

    public Bullet(float lifeTime, Vector2f position,Type owner, float angle, World world){
        MAX_LIFE_TIME = lifeTime;
        this.lifeTime = 0;
        this.owner = owner;
        bulletOwner = -5;
        hit = false;
        asteroid = false;
        enemy = false;

        BodyConfig config = new BodyConfig();

        Vector2f shape[] = new Vector2f[4];
        shape[0] = new Vector2f(-7.5f, 0);
        shape[1] = new Vector2f(0f, -5);
        shape[2] = new Vector2f(7.5f, 0);
        shape[3] = new Vector2f(0, 30);

        this.offScreenAllowance = new Vector2f(0,0);

        config.shape = new Polygon(shape);

        switch (owner){
            case Bullet:
                this.renderColour = Color.YELLOW;
                config.category = CollisionMask.BULLET;
                break;
            case EnemyBullet:
                this.renderColour = Color.RED;
                config.category = CollisionMask.ENEMY_BULLET;
                break;
        }

        config.mask = CollisionMask.AI | CollisionMask.ASTEROID;

        this.body = world.createBody(config);

        this.body.setVelocity(new Vector2f(0,-350));
        this.setMaxSpeed(350);
        this.body.rotateVelocity(angle);
        this.body.setTransform(position,angle);

        body.setData(this);

        body.registerObserver(this, Message.Type.Collision);

        onScreen = true;
    }

    @Override
    public void render(RenderWindow window){
        super.render(window);
    }


    @Override
    public void update(float dt){
        super.update(dt);
        lifeTime+=dt;
        if(lifeTime > MAX_LIFE_TIME){
            onScreen = false;
        }
    }

    @Override
    public void receiveMessage(Message message) {
        if(message.getType() == Message.Type.Collision) {
            CollisionMessage cm = (CollisionMessage) message;
            if(owner == Type.EnemyBullet) {
                if (cm.getBodyA().getData().getType() != Type.StandardAI && cm.getBodyA().getData().getType() != Type.Hexaboss) {
                    onScreen = false;
                    hit = true;
                }

            }
            else {
                if (cm.getBodyA().getData().getType() != Type.Player) {
                    onScreen = false;
                    hit  = true;
                }
            }
            if(cm.getBodyA().getData().getType() == Type.Bullet && cm.getBodyB().getData().getType() == Type.EnemyBullet){
                hit = true;
                onScreen = false;
            }
            if(cm.getBodyB().getData().getType() == Type.Bullet && cm.getBodyA().getData().getType() == Type.EnemyBullet){
                hit = true;
                onScreen = false;
            }

            asteroid = cm.getBodyA().getData().getType() == Type.Asteroid || cm.getBodyB().getData().getType() == Type.Asteroid;
            asteroid = asteroid && getType() == Type.Bullet;

            enemy = cm.getBodyA().getData().getType() == Type.StandardAI || cm.getBodyB().getData().getType() == Type.StandardAI;
            enemy |= cm.getBodyA().getData().getType() == Type.Swamer || cm.getBodyB().getData().getType() == Type.Swamer;
            enemy = enemy && getType() == Type.Bullet;

        }
    }

    public float getMaxLifeTime() {
        return MAX_LIFE_TIME;
    }

    public Type getType() { return owner; }

    public void setType(Type type) { owner = type; }

    public void setBulletOwner(int owner) { bulletOwner = owner;}

    public int getBulletOwner() { return bulletOwner; }

    public boolean isHit() {
        return hit;
    }
    @Override
    protected void checkOffScreen(){
        switch (getType()){
            case Bullet:
                super.checkOffScreen();
                break;
            case EnemyBullet:
                if(this.onScreen) {
                    if (body.getTransform().getPosition().x < -offScreenAllowance.x || body.getTransform().getPosition().x > State.WORLD_SIZE.x + offScreenAllowance.x) {
                        this.onScreen = false;
                    } else
                        this.onScreen = !(body.getTransform().getPosition().y < -offScreenAllowance.y
                                || body.getTransform().getPosition().y > State.WORLD_SIZE.y + offScreenAllowance.y);
                }
                break;
        }
    }

    public boolean isAsteroid() {
        return asteroid;
    }

    public boolean isEnemy() {
        return enemy;
    }
}
