package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.RigidBody;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * @author Matthew Threlfall
 */
public class Asteroid extends Entity {

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

    }

    @Override
    public void render(RenderWindow renderer) {
        /** Simply runs the body renderer from the entity class it extends from */
        super.render(renderer);
    }

    public Polygon getShape(){
        return body.getShape();
    }

    public RigidBody getBody() {
        return body;
    }
}
