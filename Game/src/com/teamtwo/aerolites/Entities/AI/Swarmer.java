package com.teamtwo.aerolites.Entities.AI;

import com.teamtwo.aerolites.Entities.CollisionMask;
import com.teamtwo.aerolites.Entities.Entity;
import com.teamtwo.engine.Graphics.Particles.ParticleConfig;
import com.teamtwo.engine.Graphics.Particles.ParticleEmitter;
import com.teamtwo.engine.Messages.Message;
import com.teamtwo.engine.Messages.Types.CollisionMessage;
import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;

/**
 * @author Matthew Threlfall
 */
public class Swarmer extends AI {
    private final float MAX_FORCE = 800;
    private ParticleEmitter jet;
    private Entity target;

    public Swarmer(World world, Vector2f pos){
        this.onScreen = true;
        BodyConfig config = new BodyConfig();

        config.mask = CollisionMask.SWARMER;
        config.category = CollisionMask.ALL;

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
        body.setData(this);

        ParticleConfig pConfig = new ParticleConfig();


        pConfig.minAngle = 0;
        pConfig.maxAngle = 0;
        pConfig.speed = 70;
        pConfig.rotationalSpeed = 40;
        pConfig.pointCount = 0;
        pConfig.colours[0] = Color.TRANSPARENT;
        //pConfig.colours[0] = Color.MAGENTA;
        pConfig.colours[1] = Color.YELLOW;
        pConfig.colours[2] = Color.YELLOW;
        pConfig.fadeOut = false;
        pConfig.startSize = 5;
        pConfig.endSize = 1;
        pConfig.minLifetime = 0.5f;
        pConfig.maxLifetime = 1;
        body.registerObserver(this, Message.Type.Collision);


        pConfig.position = body.getTransform().getPosition();
        jet = new ParticleEmitter(pConfig, 40f, 400);
    }

    @Override
    public void update(float dt){
        super.update(dt);
        jet.update(dt);
        findTarget();
        float x = body.getTransform().getPosition().x;
        float y = body.getTransform().getPosition().y;
        jet.getConfig().position = body.getShape().getTransformed()[0];

        Vector2f pos = target.getBody().getTransform().getPosition();

        float degreeBetween =  (float)Math.atan2(pos.y - y, pos.x - x) + MathUtil.PI/2;

        float xForce = MathUtil.sin(degreeBetween)*MAX_FORCE;
        float yForce = MathUtil.cos(degreeBetween)*-MAX_FORCE;
        body.applyForce(new Vector2f(xForce,yForce));
    }

    public void findTarget(){
        float lowestDistance = 100000000;
        for(Entity p : entities) {
            float x = p.getBody().getTransform().getPosition().x;
            float y = p.getBody().getTransform().getPosition().y;

            float xAI = getBody().getTransform().getPosition().x;
            float yAI = getBody().getTransform().getPosition().y;
            float distanceTo= MathUtil.square(x - xAI) + MathUtil.square(y - yAI);
            if ( distanceTo < lowestDistance) {
                lowestDistance = MathUtil.square(x - xAI) + MathUtil.square(y - yAI);
                target = p;
            }
        }
    }

    @Override
    public void render(RenderWindow window){
        jet.render(window);
        super.render(window);
    }

    @Override
    public void receiveMessage(Message message) {
        if (message.getType() == Message.Type.Collision) {
            CollisionMessage cm = (CollisionMessage) message;
            if (cm.getBodyB().getData().getType() == Type.Bullet || cm.getBodyA().getData().getType() == Type.Bullet) {
                onScreen = false;
            }
        }
    }

    @Override
    public Type getType() { return Type.Swamer; }

    @Override
    public void setEntities(ArrayList entities){
        this.entities = entities;
    }
}
