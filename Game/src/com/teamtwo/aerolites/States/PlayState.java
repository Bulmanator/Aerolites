package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Entities.AI.*;
import com.teamtwo.aerolites.Entities.Asteroid;
import com.teamtwo.aerolites.Entities.Bullet;
import com.teamtwo.aerolites.Entities.Entity;
import com.teamtwo.aerolites.Entities.Player;
import com.teamtwo.engine.Physics.RigidBody;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Matthew Threlfall
 */
public class PlayState extends State {

    private World world;
    private ArrayList<Entity> entities;
    private ArrayList<Player> players;
    private ArrayList<Player> deadPlayers;
    private float accum;
    private float asteroidSpawnRate;
    private float swarmerSpawnRate;
    private float lastSwarmer;
    private boolean gameOver;


    private float bossTimer;
    private boolean boss;
    private boolean alertStopper;

    private float lastStandard;
    private float standardTime;
    private float bossSpawnTime;
    private int playerCount;


    /**
     * Creates a new Play state, the player count is negative if only controllers are used. -1 will create 1 player, -4 will create 4 players, controllers only.
     * 0 will create a single player using the keyboard, 4 will create 5 players, one using the keyboard
     * @param gsm the game state manager for the entire game
     * @param playerCount the amount of players to be in the game, key highlighted above
     */
    public PlayState(GameStateManager gsm, int playerCount) {
        super(gsm);

        gameOver = false;
        world = new World(new Vector2f(0,0));
        World.DRAW_VELOCITIES = false;
        World.DRAW_BODIES = false;
        World.DRAW_AABB = false;
        this.playerCount = playerCount;

        entities = new ArrayList<>();
        players = new ArrayList<>();
        deadPlayers = new ArrayList<>();

        bossSpawnTime = 60;
        bossTimer = 0;
        boss = false;
        alertStopper = true;


        players.add(new Player(world));
        players.get(0).setPlayerNumber(1);
        int keyboard = 0;
        if(playerCount < 0){
            players.get(0).setControllerNum(0);
            keyboard = 1;
            playerCount = -(playerCount) -1;
        }
        for(int i = 0; i < playerCount; i++){
            players.add(new Player(world));
            ((Player)players.get(i+1)).setControllerNum(i+keyboard);
            ((Player)players.get(i+1)).setPlayerNumber(i+2);
        }
        accum = 0;
        if(playerCount<=0) {
            asteroidSpawnRate = 1f;
            swarmerSpawnRate = 15f;
            standardTime = 20f;
        }
        else {
            asteroidSpawnRate = 1/playerCount*1.8f;
            swarmerSpawnRate = 6/playerCount;
            standardTime = 10f/playerCount;
        }
        lastSwarmer = 0;

        ContentManager.instance.loadFont("Ubuntu","Ubuntu.ttf");
        loadContent();
    }

    @Override
    public void update(float dt) {

        world.update(dt);
        bossTimer+=dt;

        if(bossTimer -6 > bossSpawnTime && !boss) {
            boss = true;
            entities.add(new Hexaboss(world));
        }
        else if(bossTimer < bossSpawnTime)
            spawnEntities(dt);

        if(players.size() == 0 && !gameOver) {
            deadPlayers.sort(Comparator.comparing(Player::getPlayerNumber));
            gameOver = true;
            gsm.addState(new GameOver(gsm, this, playerCount));
        }

        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).update(dt);
            if(!entities.get(i).isOnScreen()) {
                if(entities.get(i).getType() == Entity.Type.Bullet){
                    int bulletOwner = ((Bullet)entities.get(i)).getBulletOwner();
                    if(searchPlayers(players,bulletOwner)>=0 && !((Bullet) entities.get(i)).isHit())
                        players.get(searchPlayers(players,bulletOwner)).incrementBulletsMissed();
                    else if(searchPlayers(deadPlayers,bulletOwner)>=0 && !((Bullet) entities.get(i)).isHit())
                        players.get(searchPlayers(deadPlayers,bulletOwner)).incrementBulletsMissed();
                }

                world.removeBody(entities.get(i).getBody());
                entities.remove(i);
                i--;
            }
            else {
                Entity e = entities.get(i);
                switch (entities.get(i).getType()) {
                    case Hexaboss:
                        i = updateHexaboss((Hexaboss) e);
                        break;
                    case Asteroid:
                        i = updateAsteroid((Asteroid) e);
                        break;
                    case StandardAI:
                    case SwamerBase:
                    case Swamer:
                        i = updateAI((AI) e);
                        break;
                }
            }
        }
        for(int i = 0 ; i < players.size(); i++){
            players.get(i).update(dt);
            updatePlayer(players.get(i));
             if(!players.get(i).isOnScreen()){
                 deadPlayers.add(players.get(i));
                 world.removeBody(players.get(i).getBody());
                 players.remove(i);
                 i--;
             }
        }
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
            entities.add(new Bullet(2, pos, Entity.Type.Bullet, p.getBody().getTransform().getAngle(), world));
            ((Bullet)entities.get(entities.size()-1)).setBulletOwner(p.getPlayerNumber());
            p.incrementBulletsShot();
            ContentManager.instance.getSound("pew").play();
        }
        return index;
    }
    public int updateHexaboss(Hexaboss h){
        if(h.isShooting()){
            for(int i = 0; i < h.getBulletPoints().size();i++){
                Vector2f v = h.getBulletPoints().get(i);
                float angle = h.getBulletAngles().get(i);
                entities.add(new Bullet(1.75f, v, Entity.Type.EnemyBullet, h.getBody().getTransform().getAngle()+angle, world));
                h.setShooting(false);
            }
        }
        return entities.indexOf(h);
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
            ContentManager.instance.getSound("expload" +MathUtil.randomInt(1,4)).play();
            world.removeBody(a.getBody());
            entities.remove(index);
            index--;
        }
        return index;
    }

    public int updateAI(AI ai){
        int index = entities.indexOf(ai);
        if(ai.getType() == Entity.Type.Swamer)
            ai.setEntities(players);
        else if(ai.getType() == Entity.Type.SwamerBase) {
            ai.setEntities(players);
        }
        else
            ai.setEntities(entities);
        if(ai.isShooting()) {
            if(ai.getType() == Entity.Type.SwamerBase) {
                for(int j = 0; j < MathUtil.randomInt(4,8); j++){
                    entities.add(new Swarmer(world,entities.get(index).getBody().getTransform().getPosition()));
                    ((AI)entities.get(entities.size()-1)).setEntities(players);
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

    public void spawnEntities(float dt){
        accum += dt;
        lastSwarmer += dt;
        lastStandard += dt;

        if(accum > asteroidSpawnRate) {
            entities.add(new Asteroid(world));
            asteroidSpawnRate = MathUtil.clamp(0.99f * asteroidSpawnRate, 0.6f, 3);
            accum = 0;
        }
        if(lastSwarmer > swarmerSpawnRate) {
            entities.add(new SwarmerBase(world));
            ((AI)entities.get(entities.size()-1)).setEntities(players);
            swarmerSpawnRate = MathUtil.clamp(0.99f * swarmerSpawnRate, 4f, 10);
            lastSwarmer = 0;
        }
        if(lastStandard>standardTime){
            entities.add(new StandardAI(world));
            lastStandard = 0;
        }
    }

    @Override
    public void render() {
        for(Entity a : entities){
            a.render(window);
        }

        for(Player p: players)
            p.render(window);

        for(Player p: players) {
            for(int i = 0; i < p.getLives()+1; i++) {
                Text text = new Text("Player " + (p.getPlayerNumber()), ContentManager.instance.getFont("Ubuntu"), 28);
                text.setStyle(Text.BOLD);
                text.setOrigin(0, 0);
                text.setPosition(0,25+ players.indexOf(p)*60);
                window.draw(text);

                RigidBody body = p.getBody();
                ConvexShape bodyShape = new ConvexShape(body.getShape().getVertices());
                bodyShape.setPosition(150+i*30, 50+ players.indexOf(p)*60);
                bodyShape.setFillColor(p.getDefaultColour());
                window.draw(bodyShape);
            }

            if(bossTimer > bossSpawnTime - 6 && MathUtil.round(bossTimer%1f,0) == 0) {
                Text text = new Text("Danger! Boss Approaching!", ContentManager.instance.getFont("Ubuntu"), 36);
                text.setStyle(Text.BOLD | TextStyle.UNDERLINED);
                text.setColor(Color.RED);
                FloatRect screenRect = text.getLocalBounds();
                text.setOrigin(screenRect.width/2, 0);
                text.setPosition(State.WORLD_SIZE.x/2,40);
                window.draw(text);
                if(alertStopper) {ContentManager.instance.getSound("alert").play(); alertStopper = false;}
            }
            else
                alertStopper = true;
        }
    }

    @Override
    public void dispose() {

    }

    public void loadContent(){
        ContentManager.instance.loadTexture("Asteroid", "Asteroid.png");
        ContentManager.instance.loadSound("pew", "pew.wav");
        ContentManager.instance.loadSound("expload1", "expload.wav");
        ContentManager.instance.loadSound("expload2", "expload2.wav");
        ContentManager.instance.loadSound("expload3", "expload3.wav");
        ContentManager.instance.loadSound("alert", "alert.wav");
        ContentManager.instance.getSound("alert").setVolume(0.3f);
        ContentManager.instance.getSound("pew").setVolume(0.3f);
    }
    public ArrayList getDeadPlayers(){ return deadPlayers; }

    public int searchPlayers(ArrayList<Player> list, int find){
        for(Player p: list)
            if(p.getPlayerNumber() == find)
                return players.indexOf(p);
        return -1;
    }
}
