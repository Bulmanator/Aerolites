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
    private final float FORCE_FROM_JET = 20000;
    private final float rotationSpeed = MathUtil.PI*35;
    private ParticleEmitter jet;
    private Entity target;
    private boolean shouldShoot;
    private final float shootTime;
    private float shootCooldown;

    public StandardAI(World world){
        this.onScreen = true;
        PLAN_EXECUTE_TIME = 0.2f;
        shootCooldown = 0;
        shootTime = 0.7f;
        BodyConfig config = new BodyConfig();


        Vector2f[] vertices = new Vector2f[5];
        vertices[0] = new Vector2f(0, 0);
        vertices[1] = new Vector2f(30, 70);
        vertices[2] = new Vector2f(20, 90);
        vertices[3] = new Vector2f(-20, 90);
        vertices[4] = new Vector2f(-30, 70);

        renderColour = Color.RED;

        config.shape = new Polygon(vertices);

        int x = 0, y = 0;
        int screenSide = MathUtil.randomInt(0,4);
        switch(screenSide) {
            case 0:
                x = MathUtil.randomInt(0, 1920);
                y = 0;
                break;
            case 1:
                x = MathUtil.randomInt(0, 1920);
                y = 1080;

                break;
            case 2:
                x = 0;
                y = MathUtil.randomInt(0, 1080);
                break;
            case 3:
                x = 1920;
                y = MathUtil.randomInt(0, 1080);

                break;
            default:
                System.out.println("WHAT?!");
        }

        config.position = new Vector2f(x,y);

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
        pConfig.maxLifetime = 3;


        pConfig.position = body.getTransform().getPosition();
        jet = new ParticleEmitter(pConfig, 40f, 400);
    }
    @Override
    public void update(float dt){
        setShooting(false);
        jet.getConfig().position = body.getTransform().apply(new Vector2f(0, 15));
        jet.setActive(true);

        jet.getConfig().maxAngle = -body.getTransform().getAngle()*MathUtil.RAD_TO_DEG - 60;
        jet.getConfig().minAngle = -body.getTransform().getAngle()*MathUtil.RAD_TO_DEG -120;
        jet.update(dt);
        planTime += dt;
        shootCooldown += dt;
        if(planTime>PLAN_EXECUTE_TIME){
            planTime = 0;
            chooseTarget();
        }
        if(target!=null){
            trackToTarget(target.getBody().getTransform().getPosition(), dt);
            float x = target.getBody().getTransform().getPosition().x;
            float y = target.getBody().getTransform().getPosition().y;
            if(shootCooldown>shootTime){
                shouldShoot = true;
                setShooting(shouldShoot);
                shootCooldown=0;
            }
            float xAI = getBody().getTransform().getPosition().x;
            float yAI = getBody().getTransform().getPosition().y;
            float distanceTo= MathUtil.squared(x - xAI) + MathUtil.squared(y - yAI);
            if(distanceTo>MathUtil.squared(200)){
                move(0,-FORCE_FROM_JET*6);
                jet.setActive(true);
            }
            else
                jet.setActive(false);
        }
        else{
            move(0,-FORCE_FROM_JET);
            body.setAngularVelocity(0);
            jet.setActive(true);
            shouldShoot=false;
        }
        checkOffScreen();
        super.update(dt);
    }
    public void trackToTarget(Vector2f pos, float dt){
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
                if ( distanceTo < lowestDistance && distanceTo < MathUtil.squared(1200)) {
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
