package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Messages.Message;
import com.teamtwo.engine.Messages.Types.CollisionMessage;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.MathUtil;
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

    public Bullet(float lifeTime, Vector2f position,Type owner, float angle, World world){
        MAX_LIFE_TIME = lifeTime;
        this.lifeTime = 0;
        this.owner = owner;

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
                config.mask = CollisionMask.BULLET;
                break;
            case EnemyBullet:
                this.renderColour = Color.RED;
                config.mask = CollisionMask.ENEMY_BULLET;
                break;
        }

        config.category = CollisionMask.AI | CollisionMask.ASTEROID;

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
                if (cm.getBodyA().getData().getType() != Type.StandardAI)
                    onScreen = false;
            }
            else {
                if (cm.getBodyA().getData().getType() != Type.Player)
                    onScreen = false;
            }

        }
    }

    public float getMaxLifeTime() {
        return MAX_LIFE_TIME;
    }

    public Type getType() { return owner; }

    public void setType(Type type) { owner = type; }

}
