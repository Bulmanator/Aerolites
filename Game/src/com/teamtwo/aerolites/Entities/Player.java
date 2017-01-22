package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Graphics.Particles.ParticleConfig;
import com.teamtwo.engine.Graphics.Particles.ParticleEmitter;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.RigidBody;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.Interfaces.EntityRenderable;
import com.teamtwo.engine.Utilities.Interfaces.Updateable;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

/**
 * Created by matt on 22/01/17.
 */
public class Player implements EntityRenderable, Updateable {

    private final float ROTATION_SPEED = MathUtil.PI;
    private final float FORCE_FROM_JET = 75000;

    private RigidBody body;
    private ParticleEmitter jet;

    public Player(World w){
        BodyConfig config = new BodyConfig();

        Vector2f[] vertices = new Vector2f[4];
        vertices[0] = new Vector2f(0, -15);
        vertices[1] = new Vector2f(15, 30);
        vertices[2] = new Vector2f(0, 35);
        vertices[3] = new Vector2f(-15, 30);

        config.position = new Vector2f(500, 150);
        config.shape = new Polygon(vertices);

        config.restitution = 0.8f;
        config.velocity = new Vector2f(0,0);
        config.angularVelocity = 0;

        config.density = 0.6f;
        body = w.createBody(config);
        ParticleConfig pConfig = new ParticleConfig();


        pConfig.minAngle = 0;
        pConfig.maxAngle = 0;
        pConfig.speed = 70;
        pConfig.rotationalSpeed = 2;
        pConfig.pointCount = 3;
        pConfig.colours[0] = Color.RED;
        pConfig.colours[1] = Color.YELLOW;
        pConfig.colours[2] = Color.MAGENTA;
        pConfig.fadeOut = true;
        pConfig.startSize = 9;
        pConfig.endSize = 4;
        pConfig.minLifetime = 1.5f;
        pConfig.maxLifetime = 2;


        pConfig.position = body.getTransform().getPosition();
        jet = new ParticleEmitter(pConfig,20f, 400);
    }

    @Override
    public void render(RenderWindow renderer) {
        ConvexShape player = new ConvexShape(body.getShape().getVertices());
        player.setPosition(body.getTransform().getPosition());
        player.setRotation(body.getTransform().getAngle()*MathUtil.RAD_TO_DEG);
        player.setFillColor(Color.BLUE);
        renderer.draw(player);
        jet.render(renderer);
    }

    @Override
    public void update(float dt) {
        jet.update(dt);
        if(Keyboard.isKeyPressed(Keyboard.Key.W)){
            Vector2f force = new Vector2f(0,-FORCE_FROM_JET);
            float sin = MathUtil.sin(body.getTransform().getAngle());
            float cos = MathUtil.cos(body.getTransform().getAngle());
            float x = (force.x * cos) - (force.y * sin);
            float y = (force.x * sin) + (force.y * cos);
            body.applyForce(new Vector2f(x,y));
            Vector2f bodyPos = body.getTransform().getPosition();


            Vector2f emitterPos = new Vector2f(0,15);
            sin = MathUtil.sin(body.getTransform().getAngle());
            cos = MathUtil.cos(body.getTransform().getAngle());
            x = (emitterPos.x * cos) - (emitterPos.y * sin);
            y = (emitterPos.x * sin) + (emitterPos.y * cos);

            jet.getConfig().position = Vector2f.add(new Vector2f(x,y),body.getTransform().getPosition());
            jet.setEmissionRate(20);

            jet.getConfig().maxAngle = -body.getTransform().getAngle()*MathUtil.RAD_TO_DEG - 60;
            jet.getConfig().minAngle = -body.getTransform().getAngle()*MathUtil.RAD_TO_DEG -120;

        }
        else
            jet.setEmissionRate(0);
        if(Keyboard.isKeyPressed(Keyboard.Key.D)){
            body.setAngularVelocity(ROTATION_SPEED);
        }
        else if(Keyboard.isKeyPressed(Keyboard.Key.A)){
            body.setAngularVelocity(-ROTATION_SPEED);
        }
        else
        {
            body.setAngularVelocity(0);
        }
    }
}
