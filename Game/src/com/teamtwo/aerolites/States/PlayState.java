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
 * Created by matt on 22/01/17.
 */
public class PlayState extends State {

    private World world;
    private ArrayList<Asteroid> asteroids;
    private float accum;
    private Player player;

    public PlayState(GameStateManager csm) {
        super(csm);
        world = new World(new Vector2f(0,0));
        asteroids = new ArrayList<>();
        asteroids.add(new Asteroid(world));
        player = new Player(world);
        accum=0;
    }

    @Override
    public void update(float dt) {
        accum+=dt;
        world.update(dt);
        if(MathUtil.isZero(accum%5,0.05f)){
            asteroids.add(new Asteroid(world));
        }
        player.update(dt);
    }

    @Override
    public void render() {
        for(Asteroid a: asteroids){
            a.render(window);
        }
        player.render(window);
    }

    @Override
    public void dispose() {

    }
}
