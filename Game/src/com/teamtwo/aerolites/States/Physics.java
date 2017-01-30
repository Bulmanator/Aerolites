package com.teamtwo.aerolites.States;

import com.teamtwo.engine.Physics.BodyConfig;
import com.teamtwo.engine.Physics.Polygon;
import com.teamtwo.engine.Physics.RigidBody;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

public class Physics extends State {

    private World world;
    private Font font;

    private RigidBody body;

    private Texture texture;

    public Physics(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2f(0, 98.1f));
        World.DRAW_VELOCITIES = true;

        BodyConfig config = new BodyConfig();


        font = ContentManager.instance.loadFont("Ubuntu", "Ubuntu.ttf");

        Vector2f[] vertices = new Vector2f[4];
        vertices[0] = new Vector2f(-640, -10);
        vertices[1] = new Vector2f(640, -10);
        vertices[2] = new Vector2f(640, 10);
        vertices[3] = new Vector2f(-640, 10);

        config.shape = new Polygon(vertices);
        config.position = new Vector2f(640, 600);

        config.density = 0;

        config.velocity = Vector2f.ZERO;
        config.angularVelocity = 0;

        world.createBody(config);

        texture = ContentManager.instance.loadTexture("Asteroid", "Asteroid.png");
        texture.setSmooth(true);


        for(int i = 0; i < 10; i++) {
            config.shape = new Polygon();
            config.velocity = Vector2f.ZERO;
            config.angularVelocity = 0;
            config.density = MathUtil.randomFloat(0.1f, 1f);
            config.position = new Vector2f(MathUtil.randomFloat(5, 1275), MathUtil.randomFloat(0, 100));
            config.restitution = MathUtil.randomFloat(0, 1);

            if(i == 0)
                body = world.createBody(config);
            else
                world.createBody(config);
        }
    }

    public void update(float dt) {
        world.update(dt);
    }

    public void render() {

        ConvexShape shape = new ConvexShape(body.getShape().getVertices());
        shape.setPosition(body.getTransform().getPosition());
        shape.setRotation(body.getTransform().getAngle() * MathUtil.RAD_TO_DEG);
        shape.setTexture(texture);

        window.draw(shape);

        Text t = new Text("Press R to generate bodies", font, 20);
        t.setPosition(100, 50);
        window.draw(t);

        world.render(window);
    }

    public void dispose() {}
}
