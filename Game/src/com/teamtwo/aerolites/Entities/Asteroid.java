package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.RigidBody;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.Interfaces.EntityRenderable;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * Created by matt on 18/01/17.
 */
public class Asteroid implements EntityRenderable {

    private RigidBody body;

    public Asteroid(World w){
        BodyConfig config = new BodyConfig();

        config.position = new Vector2f(500, 350);
        config.shape = new Polygon();

        config.restitution = 0.8f;
        config.velocity = new Vector2f((float)(Math.random()*200)-100, (float)(Math.random()*200)-100);
        config.angularVelocity = MathUtil.PI*35*(float)Math.random();

        config.density = 0.6f;
        body = w.createBody(config);
    }

    @Override
    public void render(RenderWindow renderer) {
        ConvexShape asteroid = new ConvexShape(getShape().getVertices());
        asteroid.setPosition(getBody().getTransform().getPosition());
        asteroid.setRotation(getBody().getTransform().getAngle());
        renderer.draw(asteroid);
    }

    public void setBody(RigidBody b){
        body=b;
    }
    public Polygon getShape(){
        return body.getShape();
    }

    public RigidBody getBody() {
        return body;
    }
}
