package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * Created by matt on 24/01/17.
 */
public class Bullet extends Entity {
    private float lifeTime;
    private final float MAX_LIFE_TIME;

    public Bullet(float lifeTime, Vector2f position, float angle, World world){
        MAX_LIFE_TIME = lifeTime;
        this.lifeTime = 0;

        BodyConfig config = new BodyConfig();
        Vector2f shape[] = new Vector2f[4];
        shape[0] = new Vector2f(-7.5f, 0);
        shape[1] = new Vector2f(0f, -5);
        shape[2] = new Vector2f(7.5f, 0);
        shape[3] = new Vector2f(0, 30);

        this.offScreenAllowance = new Vector2f(0,0);

        config.shape = new Polygon(shape);

        this.body = world.createBody(config);
        this.body.setVelocity(new Vector2f(0,-350));
        this.setMaxSpeed(350);
        this.body.rotateVelocity(angle);
        this.body.setTransform(position,angle);

        this.renderColour = Color.RED;

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

    public float getMAX_LIFE_TIME() {
        return MAX_LIFE_TIME;
    }
}
