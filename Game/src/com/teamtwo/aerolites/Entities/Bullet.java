package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * Created by matt on 24/01/17.
 */
public class Bullet extends Entity {
    private float lifeTime;
    private final float MAX_LIFE_TIME;
    private boolean alive;

    public Bullet(float lifeTime, Vector2f position, float angle, World world){
        MAX_LIFE_TIME = lifeTime;
        this.lifeTime = 0;
        alive = true;

        BodyConfig config = new BodyConfig();
        Vector2f shape[] = new Vector2f[4];
        shape[0] = new Vector2f(position.x, position.y);
        shape[1] = new Vector2f(position.x + 5, position.y);
        shape[2] = new Vector2f(position.x + 5, position.y + 20);
        shape[3] = new Vector2f(position.x, position.y + 20);

        config.shape = new Polygon(shape);

        this.body = world.createBody(config);
        this.body.setVelocity(new Vector2f(0,-800));
        this.body.setTransform(position,angle);
    }

    @Override
    public void render(RenderWindow window){
        super.render(window);
    }
    @Override
    public void update(float dt){
        lifeTime+=dt;
        if(lifeTime > MAX_LIFE_TIME){
            alive = false;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public float getMAX_LIFE_TIME() {
        return MAX_LIFE_TIME;
    }
}
