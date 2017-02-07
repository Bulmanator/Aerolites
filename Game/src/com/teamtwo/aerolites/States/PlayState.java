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
        //entities.add(new StandardAI(world));
        //entities.add(new StandardAI(world, new Vector2f(900,150)));
        //entities.add(new StandardAI(world, new Vector2f(1100,150)));
        //entities.add(new SwarmerBase(world));
        accum = 0;
        asteroidSpawnRate = 2f;
        swarmerSpawnRate = 17f;
        standardTime = 10f;
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
        if(accum > asteroidSpawnRate) {
            entities.add(new Asteroid(world));
            asteroidSpawnRate = MathUtil.clamp(0.99f * asteroidSpawnRate, 0.4f, 3);
            accum = 0;
        }
        if(lastSwarmer > swarmerSpawnRate) {
            entities.add(new SwarmerBase(world));
            ((AI)entities.get(entities.size()-1)).setEntities(entities);
            lastSwarmer = 0;
        }
        if(lastStandard>standardTime){
            entities.add(new StandardAI(world));
            lastStandard = 0;
        }

        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).update(dt);
            if(!entities.get(i).isOnScreen()) {
                world.removeBody(entities.get(i).getBody());
                entities.remove(i);
                i--;
            }
            else {
                Entity e = entities.get(i);
                switch (entities.get(i).getType()) {
                    case Asteroid:
                        i = updateAsteroid((Asteroid) e);
                        break;
                    case Player:
                        i = updatePlayer((Player) e);
                        break;
                    case StandardAI:
                    case SwamerBase:
                    case Swamer:
                        i = updateAI((AI) e);
                        break;
                }
            }
        }

        window.setTitle("Entities: " + entities.size());
        if(Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) {
            game.getEngine().close();
        }
    }

    public int updatePlayer(Player p){
        int index = entities.indexOf(p);
        if (p.shooting()) {
            float x = p.getBody().getShape().getTransformed()[0].x;
            float y = p.getBody().getShape().getTransformed()[0].y;
            Vector2f pos = new Vector2f(x, y);
            entities.add(new Bullet(4, pos, Entity.Type.Bullet, p.getBody().getTransform().getAngle(), world));
            ContentManager.instance.getSound("pew").play();
        }
        return index;
    }
    public int updateAsteroid(Asteroid a){
        int index = entities.indexOf(a);
        if(a.shouldExpload()) {
            if(a.getBody().getShape().getRadius()/2 > 15)
            {
                Asteroid a1 = new Asteroid(world, a.getBody().getTransform().getPosition(), new Vector2f(a.getBody().getVelocity().x*1.2f,a.getBody().getVelocity().y*1.2f), a.getShape().getRadius()*MathUtil.randomFloat(0.5f,0.8f));
                Asteroid a2 = new Asteroid(world, a.getBody().getTransform().getPosition(), new Vector2f(-a.getBody().getVelocity().x*1.2f,-a.getBody().getVelocity().y*1.2f), a.getShape().getRadius()*MathUtil.randomFloat(0.5f,0.8f));
                entities.add(a1);
                entities.add(a2);
            }

            world.removeBody(a.getBody());
            entities.remove(index);
            index--;
        }
        return index;
    }
    public int updateAI(AI ai){
        int index = entities.indexOf(ai);
        ai.setEntities(entities);
        if(ai.isShooting()) {
            if(ai.getType() == Entity.Type.SwamerBase) {
                for(int j = 0; j < MathUtil.randomInt(4,6); j++){
                    entities.add(new Swarmer(world,entities.get(index).getBody().getTransform().getPosition()));
                    ((AI)entities.get(entities.size()-1)).setEntities(entities);
                }
                world.removeBody(entities.get(index).getBody());
                entities.remove(index);
            }
            else {
                float x = entities.get(index).getBody().getShape().getTransformed()[0].x;
                float y = entities.get(index).getBody().getShape().getTransformed()[0].y;
                Vector2f pos = new Vector2f(x, y);
                entities.add(new Bullet(2, pos, Entity.Type.EnemyBullet, entities.get(index).getBody().getTransform().getAngle(), world));
            }
        }
        return index;
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

        enttitiyCount = new Text("Time Alive: "+ (System.nanoTime()-startTime)/1000000000,ContentManager.instance.getFont("Ubuntu"));
        enttitiyCount.setPosition(new Vector2f(0,100));
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
