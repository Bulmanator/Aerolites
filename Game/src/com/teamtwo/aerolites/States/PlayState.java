package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Configs.LevelConfig;
import com.teamtwo.aerolites.Entities.AI.*;
import com.teamtwo.aerolites.Entities.Asteroid;
import com.teamtwo.aerolites.Entities.Bullet;
import com.teamtwo.aerolites.Entities.Entity;
import com.teamtwo.aerolites.Entities.Player;
import com.teamtwo.engine.Input.Controllers.PlayerNumber;
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

    private Player[] players;

    private float accum;
    private float asteroidSpawnRate;
    private float swarmerSpawnRate;
    private float lastSwarmer;
    private boolean gameOver;

    // The configuration for the level
    private LevelConfig config;

    private float bossTimer;
    private boolean boss;
    private boolean alertStopper;

    private float lastStandard;
    private float standardTime;
    private float bossSpawnTime;
    private int playerCount;

    //TODO make boss scale
    //TODO make configs work
    //TODO shoot bullets out of ait
    //TODO make power ups work
    //TODO star map
    //TODO shop and stuff
    //TODO count asteroids destoyed and enemies killed and show them on score board
    //TODO make another boss
    //TODO make the scores save over levels
    //TODO make Tijans shit work
    //TODO tie everything together
    //TODO fix LevelOver multiple player next shite

    /**
     * Creates a new Play state, the player count is negative if only controllers are used. -1 will create 1 player, -4 will create 4 players_list, controllers only.
     * 0 will create a single player using the keyboard, 4 will create 5 players_list, one using the keyboard
     * @param gsm the game state manager for the entire game
     * @param config The configuration for the level
     */
    public PlayState(GameStateManager gsm, LevelConfig config) {
        super(gsm);

        gameOver = false;
        world = new World(Vector2f.ZERO);

        entities = new ArrayList<>();

        bossSpawnTime = 120;
        bossTimer = 0;
        boss = false;
        alertStopper = true;

        int playerCount = 0;
        while (config.players[playerCount] != null) {
            playerCount++;
            if(playerCount == config.players.length) break;
        }

        players = new Player[playerCount];
        for(int i = 0; i < playerCount; i++) {
            players[i] = new Player(world, PlayerNumber.values()[i]);
        }



/*        players_list.add(new Player(world));
        players_list.get(0).setPlayerNumber(1);
        int keyboard = 0;
        if(playerCount < 0){
            players_list.get(0).setControllerNum(0);
            keyboard = 1;
            playerCount = -(playerCount) -1;
        }
        for(int i = 0; i < playerCount; i++){
            players_list.add(new Player(world));
            ((Player) players_list.get(i+1)).setControllerNum(i+keyboard);
            ((Player) players_list.get(i+1)).setPlayerNumber(i+2);
        }
*/
        accum = 0;
        if(playerCount <= 0) {
            asteroidSpawnRate = 1.8f;
            swarmerSpawnRate = 8f;
            standardTime = 10f;
        }
        else {
            asteroidSpawnRate = 1f/playerCount*1.8f;
            swarmerSpawnRate = 6/playerCount;
            standardTime = 8f/playerCount;
        }
        lastSwarmer = 0;

        loadContent();

        ContentManager.instance.getMusic("PlayMusic").play();
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
        else if(boss && !gameOver)
        {
            boolean bossAlive = false;
            for(Entity e: entities)
                if(e.getType() == Entity.Type.Hexaboss) {
                    bossAlive = true;
                }
            if(!bossAlive){
                for (int i = 0; i < players_list.size(); i++) {
                    deadPlayers.add(players_list.get(i));
                    world.removeBody(players_list.get(i).getBody());
                    players_list.remove(i);
                    i--;
                }
                deadPlayers.sort(Comparator.comparing(Player::getPlayerNumber));
                gameOver = true;
                gsm.setState(new LevelOver(gsm, this, playerCount, true));
            }
        }

        if(players_list.size() == 0 && !gameOver) {
            deadPlayers.sort(Comparator.comparing(Player::getPlayerNumber));
            gameOver = true;
            gsm.setState(new LevelOver(gsm, this, playerCount, false));
        }

        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).update(dt);
            if(!entities.get(i).isOnScreen()) {
                if(entities.get(i).getType() == Entity.Type.Bullet){
                    int bulletOwner = ((Bullet)entities.get(i)).getBulletOwner();
                    if(searchPlayers(players_list,bulletOwner)>=0 && !((Bullet) entities.get(i)).isHit())
                        players_list.get(searchPlayers(players_list,bulletOwner)).incrementBulletsMissed();
                    else if(searchPlayers(deadPlayers,bulletOwner)>=0 && !((Bullet) entities.get(i)).isHit())
                        players_list.get(searchPlayers(deadPlayers,bulletOwner)).incrementBulletsMissed();
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
        for(int i = 0; i < players_list.size(); i++){
            players_list.get(i).update(dt);
            updatePlayer(players_list.get(i));
             if(!players_list.get(i).isOnScreen()){
                 deadPlayers.add(players_list.get(i));
                 world.removeBody(players_list.get(i).getBody());
                 players_list.remove(i);
                 i--;
             }
        }
        if(Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) {
            game.getEngine().close();
        }
    }

    public int updatePlayer(Player p){
        int index = entities.indexOf(p);
        if (p.isShooting()) {
            float x = p.getBody().getShape().getTransformed()[0].x;
            float y = p.getBody().getShape().getTransformed()[0].y;
            Vector2f pos = new Vector2f(x, y);
            entities.add(new Bullet(2, pos, Entity.Type.Bullet, p.getBody().getTransform().getAngle(), world));
            ((Bullet)entities.get(entities.size()-1)).setBulletOwner(p.getPlayerNumber());
            p.incrementBulletsShot();
            ContentManager.instance.getSound("Pew").play();
            float pitch = ContentManager.instance.getSound("Pew").getPitch();
            ContentManager.instance.getSound("Pew").setPitch(pitch + MathUtil.randomFloat(-0.1f, 0.1f));
        }
        return index;
    }
    public int updateHexaboss(Hexaboss h) {
        if(h.isShooting()){
            for(int i = 0; i < h.getBulletPoints().size();i++){
                Vector2f v = h.getBulletPoints().get(i);
                float angle = h.getBulletAngles().get(i);
                entities.add(new Bullet(10f, v, Entity.Type.EnemyBullet, h.getBody().getTransform().getAngle()+angle, world));
                entities.get(entities.size() - 1).setMaxSpeed(250);
                h.setShooting(false);
            }
        }
        return entities.indexOf(h);
    }

    public int updateAsteroid(Asteroid a){
        int index = entities.indexOf(a);
        if(a.shouldExpload()) {
            if(a.getBody().getShape().getRadius() / 2 > 15) {
                Vector2f position = a.getBody().getTransform().getPosition();
                Vector2f velocity = new Vector2f(a.getBody().getVelocity().x * 1.2f, a.getBody().getVelocity().y * 1.2f);
                float radius = a.getShape().getRadius() * MathUtil.randomFloat(0.5f, 0.8f);

                Asteroid a1 = new Asteroid(world, position, velocity, radius);
                radius = a.getShape().getRadius() * MathUtil.randomFloat(0.5f, 0.8f);
                Asteroid a2 = new Asteroid(world, position, Vector2f.neg(velocity), radius);

                entities.add(a1);
                entities.add(a2);
            }
            ContentManager.instance.getSound("Explode_" + MathUtil.randomInt(1, 4)).play();
            world.removeBody(a.getBody());
            entities.remove(index);
            index--;
        }
        return index;
    }

    public int updateAI(AI ai){
        int index = entities.indexOf(ai);
        if(ai.getType() == Entity.Type.Swamer)
            ai.setEntities(players_list);
        else if(ai.getType() == Entity.Type.SwamerBase) {
            ai.setEntities(players_list);
        }
        else
            ai.setEntities(entities);
        if(ai.isShooting()) {
            if(ai.getType() == Entity.Type.SwamerBase) {
                for(int j = 0; j < MathUtil.randomInt(4, 8); j++){
                    entities.add(new Swarmer(world, entities.get(index).getBody().getTransform().getPosition()));
                    ((AI)entities.get(entities.size()-1)).setEntities(players_list);
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

    public void spawnEntities(float dt) {
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
            ((AI)entities.get(entities.size()-1)).setEntities(players_list);
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

        for(Player p: players_list)
            p.render(window);

        for(Player p: players_list) {
            for(int i = 0; i < p.getLives()+1; i++) {
                Text text = new Text("Player " + (p.getPlayerNumber()), ContentManager.instance.getFont("Ubuntu"), 28);
                text.setStyle(Text.BOLD);
                text.setOrigin(0, 0);
                text.setPosition(0,25+ players_list.indexOf(p)*60);
                window.draw(text);

                RigidBody body = p.getBody();
                ConvexShape bodyShape = new ConvexShape(body.getShape().getVertices());
                bodyShape.setPosition(150+i*30, 50+ players_list.indexOf(p)*60);
                bodyShape.setFillColor(p.getDefaultColour());
                window.draw(bodyShape);
            }

            if(bossTimer > bossSpawnTime - 6 && bossTimer < bossSpawnTime + 10 && MathUtil.round(bossTimer%1f,0) == 0) {
                Text text = new Text("Danger! Boss Approaching!", ContentManager.instance.getFont("Ubuntu"), 36);
                text.setStyle(Text.BOLD | TextStyle.UNDERLINED);
                text.setColor(Color.RED);
                FloatRect screenRect = text.getLocalBounds();
                text.setOrigin(screenRect.width/2, 0);
                text.setPosition(State.WORLD_SIZE.x/2,40);
                window.draw(text);
                if(alertStopper) {
                    ContentManager.instance.getSound("Alert").play();
                    alertStopper = false;}
            }
            else
                alertStopper = true;
        }
    }

    @Override
    public void dispose() {

    }

    private void loadContent() {

        // Load Textures
        ContentManager.instance.loadTexture("Asteroid", "Asteroid.png");

        // Load Fonts
        ContentManager.instance.loadFont("Ubuntu","Ubuntu.ttf");

        // Load Sounds
        ContentManager.instance.loadSound("Pew", "pew.wav");
        ContentManager.instance.loadSound("Explode_1", "explode.wav");
        ContentManager.instance.loadSound("Explode_2", "explode2.wav");
        ContentManager.instance.loadSound("Explode_3", "explode3.wav");
        ContentManager.instance.loadSound("Alert", "alert.wav");

        // Load Music
        ContentManager.instance.loadMusic("PlayMusic", "music.wav");
    }

    public Player[] getPlayers() { return players; }
}
