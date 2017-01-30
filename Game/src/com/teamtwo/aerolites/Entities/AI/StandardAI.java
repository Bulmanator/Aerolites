package com.teamtwo.aerolites.Entities.AI;

import com.teamtwo.aerolites.Entities.Bullet;
import com.teamtwo.aerolites.Entities.Entity;
import com.teamtwo.engine.Graphics.Particles.ParticleConfig;
import com.teamtwo.engine.Graphics.Particles.ParticleEmitter;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * @Author Matthew Threlfall
 */
public class StandardAI extends AI {

    private float planTime;
    private final float PLAN_EXECUTE_TIME;
    private final float FORCE_FROM_JET = 40000;
    private final float rotationSpeed = MathUtil.PI*30;
    private ParticleEmitter jet;
    private Entity target;

    public StandardAI(World world, Vector2f position){
        this.onScreen = true;
        PLAN_EXECUTE_TIME = 0.2f;
        BodyConfig config = new BodyConfig();

        config.position = position;

        Vector2f[] vertices = new Vector2f[5];
        vertices[0] = new Vector2f(0, 0);
        vertices[1] = new Vector2f(30, 70);
        vertices[2] = new Vector2f(20, 90);
        vertices[3] = new Vector2f(-20, 90);
        vertices[4] = new Vector2f(-30, 70);

        renderColour = Color.RED;

        config.shape = new Polygon(vertices);

        body = world.createBody(config);
        planTime = 0;

        ParticleConfig pConfig = new ParticleConfig();


        pConfig.minAngle = 0;
        pConfig.maxAngle = 0;
        pConfig.speed = 70;
        pConfig.rotationalSpeed = 40;
        pConfig.pointCount = 6;
        pConfig.colours[0] = Color.RED;
        //pConfig.colours[0] = Color.MAGENTA;
        pConfig.colours[1] = Color.YELLOW;
        pConfig.colours[2] = Color.YELLOW;
        pConfig.fadeOut = true;
        pConfig.startSize = 14;
        pConfig.endSize = 4;
        pConfig.minLifetime = 1.5f;
        pConfig.maxLifetime = 6;


        pConfig.position = body.getTransform().getPosition();
        jet = new ParticleEmitter(pConfig, 40f, 400);
    }
    @Override
    public void update(float dt){
        jet.getConfig().position = body.getTransform().apply(new Vector2f(0, 15));
        jet.setActive(true);

        jet.getConfig().maxAngle = -body.getTransform().getAngle()*MathUtil.RAD_TO_DEG - 60;
        jet.getConfig().minAngle = -body.getTransform().getAngle()*MathUtil.RAD_TO_DEG -120;
        jet.update(dt);
        planTime+=dt;
        if(planTime>PLAN_EXECUTE_TIME){
            planTime = 0;
            chooseTarget();
        }
        if(target!=null){
            checkInDegree(target.getBody().getTransform().getPosition(), dt);
            float x = target.getBody().getTransform().getPosition().x;
            float y = target.getBody().getTransform().getPosition().y;

            float xAI = getBody().getTransform().getPosition().x;
            float yAI = getBody().getTransform().getPosition().y;
            float distanceTo= MathUtil.squared(x - xAI) + MathUtil.squared(y - yAI);
            if(distanceTo>MathUtil.squared(200)){
                move(0,-FORCE_FROM_JET*2);
                jet.setActive(true);
            }
            else
                jet.setActive(false);
        }
        else{
            move(0,-FORCE_FROM_JET);
            jet.setActive(true);
        }
        checkOffScreen();
    }
    public void checkInDegree(Vector2f pos, float dt){
        float x = body.getTransform().getPosition().x;
        float y = body.getTransform().getPosition().y;

        float degreeBetween =  (float)Math.atan2(pos.y - y, pos.x - x) - body.getTransform().getAngle() + MathUtil.PI/2;
        body.setAngularVelocity(rotationSpeed*dt*MathUtil.normalizeAngle(degreeBetween)/MathUtil.abs(MathUtil.normalizeAngle(degreeBetween)));

    }

    public void chooseTarget(){
        float lowestDistance = 1000000000;
        target = null;

        for(Entity e: entities){
            if(!(e instanceof AI )&& !(e instanceof Bullet)) {
                float x = e.getBody().getTransform().getPosition().x;
                float y = e.getBody().getTransform().getPosition().y;

                float xAI = getBody().getTransform().getPosition().x;
                float yAI = getBody().getTransform().getPosition().y;
                float distanceTo= MathUtil.squared(x - xAI) + MathUtil.squared(y - yAI);
                if ( distanceTo < lowestDistance && distanceTo < MathUtil.squared(600)) {
                    lowestDistance = MathUtil.squared(x - xAI) + MathUtil.squared(y - yAI);
                    target = e;
                }
            }
        }
    }


    @Override
    public void render(RenderWindow renderer) {
        jet.render(renderer);
        super.render(renderer);
    }

}