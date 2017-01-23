package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Entities.Asteroid;
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
    private ArrayList<Asteroid> asteroids;
    private float accum;
    private Player player;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2f(0,0));
        World.DRAW_VELOCITIES = true;

        asteroids = new ArrayList<>();
        asteroids.add(new Asteroid(world));
        player = new Player(world);
        accum = 0;
    }

    @Override
    public void update(float dt) {
        accum += dt;
        world.update(dt);
        if(MathUtil.isZero(accum % 5, 0.05f)){
            asteroids.add(new Asteroid(world));
        }
        player.update(dt);
    }

    @Override
    public void render() {
        for(Asteroid a : asteroids){
            a.render(window);
        }

        player.render(window);
        world.render(window);
    }

    @Override
    public void dispose() {

    }
}
