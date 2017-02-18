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
import org.jsfml.system.Vector2i;

/**
 * @author Matthew Threlfall
 */
public class Bullet extends Entity {

    private static final Vector2f[] vertices = new Vector2f[] {
            new Vector2f(-7.5f, 0), new Vector2f(0f, -5),
            new Vector2f(7.5f, 0), new Vector2f(0, 30)
    };

    // How long the bullet has been alive and how long it can be alive
    private float lifeTime;
    private final float totalLifeTime;

    // The owner of the bullet
    private Type owner;

    private boolean hit;
    private boolean asteroid;
    private boolean enemy;

    public Bullet(float lifeTime, Vector2f position,Type owner, float angle, World world) {

        totalLifeTime = lifeTime;
        this.lifeTime = 0;

        hit = false;
        asteroid = false;
        enemy = false;

        BodyConfig config = new BodyConfig();

        this.offScreenAllowance = new Vector2f(Vector2i.ZERO);

        config.shape = new Polygon(vertices);

        this.owner = owner;
        switch (owner) {
            case Bullet:
                this.renderColour = Color.YELLOW;
                config.category = CollisionMask.BULLET;
                config.mask = CollisionMask.AI;
                break;
            case EnemyBullet:
                this.renderColour = Color.RED;
                config.category = CollisionMask.ENEMY_BULLET;
                config.mask = CollisionMask.PLAYER;
                break;
        }

        config.mask |= CollisionMask.ASTEROID;

        this.body = world.createBody(config);

        this.body.setVelocity(new Vector2f(0, -350));
        this.setMaxSpeed(350);
        this.body.rotateVelocity(angle);
        this.body.setTransform(position,angle);

        body.setData(this);

        body.registerObserver(this, Message.Type.Collision);

        onScreen = true;
        alive = true;
    }

    @Override
    public void render(RenderWindow window){
        super.render(window);
    }


    @Override
    public void update(float dt){
        super.update(dt);
        lifeTime += dt;
        if(lifeTime > totalLifeTime) {
            onScreen = false;
            alive = false;
        }
    }

    @Override
    public void receiveMessage(Message message) {
        if(message.getType() == Message.Type.Collision) {
            CollisionMessage cm = (CollisionMessage) message;

            Entity.Type typeA = (Type) cm.getBodyA().getData().getType();
            Entity.Type typeB = (Type) cm.getBodyB().getData().getType();

            alive = false;

            if(owner == Type.EnemyBullet) {
                if(typeA == Type.StandardAI || typeB == Type.StandardAI) {
                    alive = true;
                }
                else
                {
                    onScreen = false;
                }
            }
            else {
                asteroid = typeA == Type.Asteroid || typeB == Type.Asteroid;

                enemy = typeA == Type.StandardAI || typeB == Type.StandardAI;
                enemy |= typeA == Type.Swamer || typeB == Type.Swamer;
                enemy |= typeA == Type.SwamerBase || typeB == Type.SwamerBase;

                hit = asteroid || enemy;
            }
        }
    }

    public Type getType() { return owner; }

    public boolean isAlive() { return alive; }

    public boolean hasHit() { return hit; }

    @Override
    protected void checkOffScreen() {
        switch (getType()){
            case Bullet:
                super.checkOffScreen();
                break;
            case EnemyBullet:
                if(this.onScreen) {
                    if (body.getTransform().getPosition().x < -offScreenAllowance.x || body.getTransform().getPosition().x > State.WORLD_SIZE.x + offScreenAllowance.x) {
                        this.onScreen = false;
                    }
                    else {
                        this.onScreen = !(body.getTransform().getPosition().y < -offScreenAllowance.y
                                || body.getTransform().getPosition().y > State.WORLD_SIZE.y + offScreenAllowance.y);
                    }
                }
                break;
        }
    }

    public boolean hitAsteroid() {
        return asteroid;
    }

    public boolean hitEnemy() {
        return enemy;
    }


}
