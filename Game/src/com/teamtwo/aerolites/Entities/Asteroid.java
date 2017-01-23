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
 * @author Matthew Threlfall
 */
public class Asteroid implements EntityRenderable {

    /** The physics body which represents this asteroid */
    private RigidBody body;
    /** The display which is drawn to the screen */
    private ConvexShape display;

    /**
     * Constructs a new procedurally generated asteroid
     * @param world The world which is used to create the body
     */
    public Asteroid(World world) {
        BodyConfig config = new BodyConfig();

        config.position = new Vector2f(500, 350);
        config.shape = new Polygon();

        config.restitution = 0.3f;
        config.velocity = new Vector2f(MathUtil.randomFloat(-100, 100), MathUtil.randomFloat(-100, 100));
        config.angularVelocity = MathUtil.randomFloat(0, MathUtil.PI / 4f);

        config.density = 0.6f;
        body = world.createBody(config);

        display = new ConvexShape(body.getShape().getVertices());
    }

    @Override
    public void render(RenderWindow renderer) {
        display.setPosition(body.getTransform().getPosition());
        display.setRotation(body.getTransform().getAngle() * MathUtil.RAD_TO_DEG);
        renderer.draw(display);
    }

    public Polygon getShape(){
        return body.getShape();
    }

    public RigidBody getBody() {
        return body;
    }
}
