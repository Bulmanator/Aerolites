package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Entities.Asteroid;
import com.teamtwo.aerolites.Entities.Bullet;
import com.teamtwo.aerolites.Entities.Entity;
import com.teamtwo.aerolites.Entities.Player;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;

/**
 * @author Matthew Threlfall
 */
public class PlayState extends State {

    private World world;
    private ArrayList<Entity> entities;
    private float accum;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2f(0,0));
        World.DRAW_VELOCITIES = false;
        World.DRAW_BODIES = false;

        entities = new ArrayList<>();
        entities.add(new Asteroid(world));
        entities.add(new Player(world));
        accum = 0;
    }

    @Override
    public void update(float dt) {
        accum += dt;
        world.update(dt);
        if(MathUtil.isZero(accum % 5, 0.05f)){
            entities.add(new Asteroid(world));
        }
        ArrayList<Asteroid> removeAsteroids = new ArrayList<>();
        ArrayList<Bullet> addBullets = new ArrayList<>();
        for(Entity a : entities){
            if(a instanceof Asteroid){
                if(!((Asteroid) a).isOnScreen()) {
                    removeAsteroids.add((Asteroid) a);
                }
            }
            else if(a instanceof Player){
                if(((Player) a).shooting()){
                    float x = a.getBody().getShape().getTransformed()[0].x;
                    float y = a.getBody().getShape().getTransformed()[0].y;
                    Vector2f pos = new Vector2f(x, y);
                    addBullets.add(new Bullet(4,pos,a.getBody().getTransform().getAngle(),world));
                }
            }

            a.update(dt);
        }
        for(Asteroid a : removeAsteroids){
            entities.remove(a);
            world.removeBody(a.getBody());
        }
        for(Bullet b: addBullets)
            entities.add(b);
    }

    @Override
    public void render() {
        for(Entity a : entities){
            a.render(window);
        }
        world.render(window);
    }

    @Override
    public void dispose() {

    }
}
