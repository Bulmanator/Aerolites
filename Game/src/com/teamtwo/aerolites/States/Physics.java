package com.teamtwo.aerolites.States;

import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.RigidBody;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

public class Physics extends State {

    private RigidBody bodyA;
    private RigidBody bodyB;

    private World world;

    private Font font;

    public Physics(GameStateManager gsm) {
        super(gsm);

        Vector2f[] vertices = new Vector2f[4];
        vertices[0] = new Vector2f(-15, -15);
        vertices[1] = new Vector2f(15, -15);
        vertices[2] = new Vector2f(15, 15);
        vertices[3] = new Vector2f(-15, 15);

        world = new World(new Vector2f(0, 0));

        BodyConfig config = new BodyConfig();

        config.position = new Vector2f(200, 100);
        config.shape = new Polygon();

        config.restitution = 0.3f;
        config.velocity = new Vector2f(100, 0);

        config.density = 0.3f;

        bodyA = world.createBody(config);

        config.position = new Vector2f(1000, 100);
        config.shape = new Polygon();

        config.restitution = 0.4f;
        config.velocity = new Vector2f(-200, 0);

        config.density = 0.4f;

        bodyB = world.createBody(config);

        font = ContentManager.instance.loadFont("Ubuntu", "Ubuntu.ttf");
    }

    @Override
    public void update(float dt) {
        world.update(dt);
    }

    @Override
    public void render() {
        ConvexShape shapeA = new ConvexShape(bodyA.getShape().getVertices());
        shapeA.setPosition(bodyA.getTransform().getPosition());
        shapeA.setRotation(bodyA.getTransform().getAngle());
        shapeA.setFillColor(Color.RED);

        ConvexShape shapeB = new ConvexShape(bodyB.getShape().getVertices());
        shapeB.setPosition(bodyB.getTransform().getPosition());
        shapeB.setRotation(bodyB.getTransform().getAngle());
        shapeB.setFillColor(Color.GREEN);

        window.draw(shapeA);
        window.draw(shapeB);

        Text t = new Text("Body B Pos: (" + bodyB.getTransform().getPosition().x + ", " + bodyB.getTransform().getPosition().y + ")", font, 20);
        t.setPosition(100, 500);

        window.draw(t);
    }

    @Override
    public void dispose() {

    }
}
