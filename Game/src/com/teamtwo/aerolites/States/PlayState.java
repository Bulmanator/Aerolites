package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Entities.AI.AI;
import com.teamtwo.aerolites.Entities.AI.StandardAI;
import com.teamtwo.aerolites.Entities.AI.Swarmer;
import com.teamtwo.aerolites.Entities.AI.SwarmerBase;
import com.teamtwo.aerolites.Entities.Asteroid;
import com.teamtwo.aerolites.Entities.Bullet;
import com.teamtwo.aerolites.Entities.Entity;
import com.teamtwo.aerolites.Entities.Player;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
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
    private float asteroidSpawnRate;
    private float swarmerSpawnRate;
    private float lastSwarmer;
    private long startTime;

    private float lastStandard;
    private float standardTime;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2f(0,0));
        World.DRAW_VELOCITIES = false;
        World.DRAW_BODIES = false;
        World.DRAW_AABB = false;

        entities = new ArrayList<>();
        //entities.add(new Asteroid(world));
        entities.add(new Player(world));
        //entities.add(new StandardAI(world, new Vector2f(700,150)));
        //entities.add(new StandardAI(world, new Vector2f(900,150)));
        //entities.add(new StandardAI(world, new Vector2f(1100,150)));
        //entities.add(new SwarmerBase(world));
        accum = 0;
        asteroidSpawnRate = 2f;
        swarmerSpawnRate = 80f;
        standardTime = 39f;
        lastSwarmer = 0;

        ContentManager.instance.loadFont("Ubuntu","Ubuntu.ttf");
        loadContent();
        startTime = System.nanoTime();
    }

    @Override
    public void update(float dt) {
        accum += dt;
        lastSwarmer += dt;
        lastStandard += dt;
        world.update(dt);
        if(accum>asteroidSpawnRate){
            entities.add(new Asteroid(world));
            asteroidSpawnRate = MathUtil.clamp(0.99f*asteroidSpawnRate, 0.4f,3);
            accum = 0;
        }
        if(lastSwarmer>swarmerSpawnRate){
            entities.add(new SwarmerBase(world));
            lastSwarmer = 0;
        }
        if(lastStandard>standardTime){
            entities.add(new StandardAI(world));
            lastStandard = 0;
        }

        for(int i = 0; i < entities.size(); i++){
            boolean alive = true;
            if(!entities.get(i).isOnScreen()){
                world.removeBody(entities.get(i).getBody());
                entities.remove(i);
                i--;
            }
            else {
                if (entities.get(i) instanceof Player) {
                    if (((Player) entities.get(i)).shooting()) {
                        float x = entities.get(i).getBody().getShape().getTransformed()[0].x;
                        float y = entities.get(i).getBody().getShape().getTransformed()[0].y;
                        Vector2f pos = new Vector2f(x, y);
                        entities.add(new Bullet(2, pos, entities.get(i).getBody().getTransform().getAngle(), world));
                        ContentManager.instance.getSound("pew").play();
                    }
                } else if(entities.get(i) instanceof AI) {
                    ((AI) entities.get(i)).setEntities(entities);
                    if(entities.get(i) instanceof SwarmerBase && ((SwarmerBase) entities.get(i)).isShooting()){
                        for(int j = 0; j < 6; j++)
                            entities.add(new Swarmer(world,entities.get(i).getBody().getTransform().getPosition()));
                        world.removeBody(entities.get(i).getBody());
                        entities.remove(i);
                        alive = false;
                    }
                    else if(((AI) entities.get(i)).isShooting()){
                        float x = entities.get(i).getBody().getShape().getTransformed()[0].x;
                        float y = entities.get(i).getBody().getShape().getTransformed()[0].y;
                        Vector2f pos = new Vector2f(x, y);
                        entities.add(new Bullet(2, pos, entities.get(i).getBody().getTransform().getAngle(), world));
                    }
                }
            }

            if(alive){
                entities.get(i).update(dt);
            }
        }
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
    public void loadContent(){
        ContentManager.instance.loadTexture("Asteroid", "Asteroid.png");
        ContentManager.instance.loadSound("pew", "pew.wav");
    }
}
