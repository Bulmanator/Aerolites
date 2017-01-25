package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Entities.Asteroid;
import com.teamtwo.aerolites.Entities.Bullet;
import com.teamtwo.aerolites.Entities.Entity;
import com.teamtwo.aerolites.Entities.Player;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

import java.util.ArrayList;

/**
 * @author Matthew Threlfall
 */
public class PlayState extends State {

    private World world;
    private ArrayList<Entity> entities;
    private float accum;
    private float spawnRate;
    private float rateEffect;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2f(0,0));
        World.DRAW_VELOCITIES = true;
        World.DRAW_BODIES = true;

        entities = new ArrayList<>();
        entities.add(new Asteroid(world));
        entities.add(new Player(world));
        accum = 0;
        spawnRate = 4;
        rateEffect = 1;
        ContentManager.instance.loadFont("Ubuntu","Ubuntu.ttf");
    }

    @Override
    public void update(float dt) {
        accum += dt;
        world.update(dt);
        if(accum>spawnRate){
            entities.add(new Asteroid(world));
            accum=0;
            spawnRate= 4/rateEffect;
            rateEffect++;
        }
        ArrayList<Entity> removeEntities = new ArrayList<>();
        ArrayList<Bullet> addBullets = new ArrayList<>();
        for(Entity e : entities){ //TODO change this to a normal for loop to remove the concurrent exception
            if(!e.isOnScreen()){
                removeEntities.add(e);
            }
            if(e instanceof Bullet && !((Bullet)e).isAlive()){
                removeEntities.add(e);
            }
            if(e instanceof Player){
                if(((Player) e).shooting()){
                    float x = e.getBody().getShape().getTransformed()[0].x;
                    float y = e.getBody().getShape().getTransformed()[0].y;
                    Vector2f pos = new Vector2f(x, y);
                    addBullets.add(new Bullet(4,pos,e.getBody().getTransform().getAngle(),world));
                }
            }
            e.update(dt);
        }
        for(Entity e : removeEntities){
            entities.remove(e);
            world.removeBody(e.getBody());
        }
        for(Bullet b: addBullets)
            entities.add(b);
        window.setTitle("Entities: " + entities.size());
        if(Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)){
            window.close();
            System.exit(0);
        }
    }

    @Override
    public void render() {
        for(Entity a : entities){
            a.render(window);
        }
        Text enttitiyCount = new Text("Entities: "+entities.size(),ContentManager.instance.getFont("Ubuntu"));
        enttitiyCount.setPosition(new Vector2f(0,0));
        enttitiyCount.setColor(Color.BLUE);
        window.draw(enttitiyCount);
        enttitiyCount = new Text("FPS: "+ gsm.game.getEngine().getFps(),ContentManager.instance.getFont("Ubuntu"));
        enttitiyCount.setPosition(new Vector2f(0,50));
        enttitiyCount.setColor(Color.BLUE);
        window.draw(enttitiyCount);
        world.render(window);
    }

    @Override
    public void dispose() {

    }
}
