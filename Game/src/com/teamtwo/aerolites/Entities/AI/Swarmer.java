package com.teamtwo.aerolites.Entities.AI;

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
public class Swarmer extends AI {
    private final float MAX_FORCE = 800;
    private ParticleEmitter jet;

    public Swarmer(World world, Vector2f pos){
        this.onScreen = true;
        BodyConfig config = new BodyConfig();

        setMaxSpeed(130);
        config.angularVelocity = MathUtil.PI2;

        config.position = pos;

        renderColour = new Color(255,140,0);

        Vector2f[] vertices = new Vector2f[3];
        vertices[0] = new Vector2f(0,0);
        vertices[1] = new Vector2f(30,0);
        vertices[2] = new Vector2f(15,30);
        config.restitution = 1;

        config.shape = new Polygon(vertices);
        config.density = 0.01f;

        body = world.createBody(config);

        ParticleConfig pConfig = new ParticleConfig();


        pConfig.minAngle = 0;
        pConfig.maxAngle = 0;
        pConfig.speed = 70;
        pConfig.rotationalSpeed = 40;
        pConfig.pointCount = 4;
        pConfig.colours[0] = Color.YELLOW;
        //pConfig.colours[0] = Color.MAGENTA;
        pConfig.colours[1] = Color.YELLOW;
        pConfig.colours[2] = Color.YELLOW;
        pConfig.fadeOut = true;
        pConfig.startSize = 5;
        pConfig.endSize = 1;
        pConfig.minLifetime = 0.5f;
        pConfig.maxLifetime = 1;


        pConfig.position = body.getTransform().getPosition();
        jet = new ParticleEmitter(pConfig, 40f, 400);
    }

    @Override
    public void update(float dt){
        super.update(dt);
        jet.update(dt);
        float x = body.getTransform().getPosition().x;
        float y = body.getTransform().getPosition().y;
        jet.getConfig().position = body.getShape().getTransformed()[0];

        Vector2f pos = entities.get(0).getBody().getTransform().getPosition();

        float degreeBetween =  (float)Math.atan2(pos.y - y, pos.x - x) + MathUtil.PI/2;

        float xForce = MathUtil.sin(degreeBetween)*MAX_FORCE;
        float yForce = MathUtil.cos(degreeBetween)*-MAX_FORCE;
        body.applyForce(new Vector2f(xForce,yForce));
    }
    @Override
    public void render(RenderWindow window){
        super.render(window);
        jet.render(window);
    }

}