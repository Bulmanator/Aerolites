package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Graphics.Particles.ParticleConfig;
import com.teamtwo.engine.Graphics.Particles.ParticleEmitter;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

/**
 * @author Matthew Threlfall
 */
public class Player extends Entity{

    private final float ROTATION_SPEED = MathUtil.PI;
    private final float FORCE_FROM_JET = 75000;
    private final float TIME_BETWEEN_SHOTS = 0.2f;

    private ParticleEmitter jet;
    private float shootCooldown;
    private boolean shoot;

    public Player(World w){
        BodyConfig config = new BodyConfig();

        Vector2f[] vertices = new Vector2f[4];
        vertices[0] = new Vector2f(0, -15);
        vertices[1] = new Vector2f(15, 30);
        vertices[2] = new Vector2f(0, 35);
        vertices[3] = new Vector2f(-15, 30);

        config.position = new Vector2f(500, 150);
        config.shape = new Polygon(vertices);

        config.restitution = 0.4f;
        config.velocity = new Vector2f(0,0);
        config.angularVelocity = 0;

        config.density = 0.6f;
        body = w.createBody(config);
        ParticleConfig pConfig = new ParticleConfig();


        pConfig.minAngle = 0;
        pConfig.maxAngle = 0;
        pConfig.speed = 70;
        pConfig.rotationalSpeed = 40;
        pConfig.pointCount = 3;
        pConfig.colours[0] = Color.RED;
        pConfig.colours[1] = Color.YELLOW;
        pConfig.colours[2] = Color.MAGENTA;
        pConfig.fadeOut = true;
        pConfig.startSize = 14;
        pConfig.endSize = 4;
        pConfig.minLifetime = 1.5f;
        pConfig.maxLifetime = 9;


        pConfig.position = body.getTransform().getPosition();
        jet = new ParticleEmitter(pConfig, 40f, 400);

        renderColour = Color.GREEN;
        shoot = false;
    }

    @Override
    public void update(float dt) {

        // Update the particle emitter
        jet.update(dt);
        shootCooldown += dt;

        if(Keyboard.isKeyPressed(Keyboard.Key.W)) {

            // Apply force to move the ship
            Vector2f force = body.getTransform().applyRotation( new Vector2f(0, -FORCE_FROM_JET));
            body.applyForce(force);

            // Move the particle emitter to be at the bottom of the ship
            jet.getConfig().position = body.getTransform().apply(new Vector2f(0, 15));
            jet.setActive(true);

            jet.getConfig().maxAngle = -body.getTransform().getAngle()*MathUtil.RAD_TO_DEG - 60;
            jet.getConfig().minAngle = -body.getTransform().getAngle()*MathUtil.RAD_TO_DEG -120;

        }
        else {
            // Deactivate the particle emitter as the ship is not boosting
            jet.setActive(false);
        }

        // If A or D are pressed then set the rotational speed accordingly
        if(Keyboard.isKeyPressed(Keyboard.Key.D)) {
            body.setAngularVelocity(ROTATION_SPEED);
        }
        else if(Keyboard.isKeyPressed(Keyboard.Key.A)) {
            body.setAngularVelocity(-ROTATION_SPEED);
        }
        else {
            body.setAngularVelocity(0);
        }
        if(Keyboard.isKeyPressed(Keyboard.Key.SPACE)) {
            if(shootCooldown > TIME_BETWEEN_SHOTS){
                shootCooldown = 0;
                shoot = true;
            }
        }
    }

    @Override
    public void render(RenderWindow renderer) {
        jet.render(renderer);
        super.render(renderer);
    }

    public boolean shooting(){
        if(shoot){
            shoot = false;
            return true;
        }
        return false;
    }
}
