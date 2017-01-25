package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Graphics.Particles.ParticleConfig;
import com.teamtwo.engine.Graphics.Particles.ParticleEmitter;
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
    private boolean alive;
    private ParticleEmitter jet;
    private boolean onScreen;

    public Bullet(float lifeTime, Vector2f position, float angle, World world){
        MAX_LIFE_TIME = lifeTime;
        this.lifeTime = 0;
        alive = true;

        BodyConfig config = new BodyConfig();
        Vector2f shape[] = new Vector2f[4];
        shape[0] = new Vector2f(0, 0);
        shape[1] = new Vector2f(5, 0);
        shape[2] = new Vector2f(5, 30);
        shape[3] = new Vector2f(0, 30);

        config.shape = new Polygon(shape);

        this.body = world.createBody(config);
        this.body.setVelocity(new Vector2f(0,-350));
        this.body.rotateVelocity(angle);
        this.body.setTransform(position,angle);

        this.renderColour = Color.RED;
        ParticleConfig pConfig = new ParticleConfig();


        pConfig.minAngle = 0;
        pConfig.maxAngle = 360;
        pConfig.speed = 15;
        pConfig.rotationalSpeed = 40;
        pConfig.pointCount = 6;
        pConfig.colours[0] = Color.YELLOW;
        pConfig.colours[1] = Color.MAGENTA;
        pConfig.fadeOut = true;
        pConfig.startSize = 3;
        pConfig.endSize = 1;
        pConfig.minLifetime = 1.5f;
        pConfig.maxLifetime = 4;


        pConfig.position = body.getTransform().getPosition();
        jet = new ParticleEmitter(pConfig, 80f, 100);
        onScreen = true;
    }

    @Override
    public void render(RenderWindow window){
        //jet.render(window);
        super.render(window);
    }
    @Override
    public void update(float dt){
        super.update(dt);
        jet.update(dt);
        jet.getConfig().position = body.getTransform().apply(new Vector2f(0, 15));
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
