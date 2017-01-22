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
import org.jsfml.window.Keyboard;

public class Physics extends State {

    private RigidBody bodyA;
    private RigidBody bodyB;

    private RigidBody plat;

    private BodyConfig config;

    private World world;

    private Font font;

    private boolean r;

    public Physics(GameStateManager gsm) {
        super(gsm);

        Vector2f[] vertices = new Vector2f[4];
        vertices[0] = new Vector2f(-15, -15);
        vertices[1] = new Vector2f(15, -15);
        vertices[2] = new Vector2f(15, 15);
        vertices[3] = new Vector2f(-15, 15);

        world = new World(new Vector2f(0, 98.1f));

        config = new BodyConfig();

        config.position = new Vector2f(200, 200);
        config.shape = new Polygon(vertices);

        config.restitution = 0.8f;
        config.velocity = new Vector2f(200, 0);
        config.angularVelocity = MathUtil.PI / 8f;

        config.density = 0.6f;

        bodyA = world.createBody(config);

        config.position = new Vector2f(1000, 200);
        config.shape = new Polygon(vertices);

        config.restitution = 0.4f;
        config.velocity = new Vector2f(-800, 0);
        config.angularVelocity = MathUtil.PI / 6f;

        config.density = 0.3f;

        bodyB = world.createBody(config);

        font = ContentManager.instance.loadFont("Ubuntu", "Ubuntu.ttf");

        vertices[0] = new Vector2f(-640, -10);
        vertices[1] = new Vector2f(640, -10);
        vertices[2] = new Vector2f(640, 10);
        vertices[3] = new Vector2f(-640, 10);

        config.shape = new Polygon(vertices);
        config.position = new Vector2f(640, 600);

        config.density = 0;

        config.velocity = Vector2f.ZERO;
        config.angularVelocity = 0;

        plat = world.createBody(config);

        r = false;
    }

    @Override
    public void update(float dt) {
        if(Keyboard.isKeyPressed(Keyboard.Key.R) && !r) {
            world.removeBody(bodyA);
            world.removeBody(bodyB);

            config.position = new Vector2f(200, 200);
            config.shape = new Polygon();

            config.restitution = 0.3f;
            config.velocity = new Vector2f(800, 0);
            config.angularVelocity = MathUtil.PI / 32f;

            config.density = 0.3f;

            bodyA = world.createBody(config);

            config.position = new Vector2f(1000, 200);
            config.shape = new Polygon();

            config.restitution = 0.4f;
            config.velocity = new Vector2f(-200, 0);
            config.angularVelocity = MathUtil.PI / 24f;

            config.density = 0.4f;
            bodyB = world.createBody(config);
        }

        world.update(dt);

        r = Keyboard.isKeyPressed(Keyboard.Key.R);
    }

    @Override
    public void render() {
        ConvexShape shapeA = new ConvexShape(bodyA.getShape().getVertices());
        shapeA.setPosition(bodyA.getTransform().getPosition());
        shapeA.setRotation(bodyA.getTransform().getAngle() * MathUtil.RAD_TO_DEG);
        shapeA.setFillColor(Color.RED);

        ConvexShape shapeB = new ConvexShape(bodyB.getShape().getVertices());
        shapeB.setPosition(bodyB.getTransform().getPosition());
        shapeB.setRotation(bodyB.getTransform().getAngle() * MathUtil.RAD_TO_DEG);
        shapeB.setFillColor(Color.GREEN);

        ConvexShape shapePlat = new ConvexShape(plat.getShape().getVertices());
        shapePlat.setPosition(plat.getTransform().getPosition());
        shapePlat.setRotation(plat.getTransform().getAngle());

        window.draw(shapeA);
        window.draw(shapeB);
        window.draw(shapePlat);

        Text t = new Text("Press R to generate bodies", font, 20);
        t.setPosition(100, 50);

        window.draw(t);
    }

    @Override
    public void dispose() {

    }
}
