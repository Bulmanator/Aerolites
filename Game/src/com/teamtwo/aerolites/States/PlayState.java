package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Entities.AI.*;
import com.teamtwo.aerolites.Entities.*;
import com.teamtwo.aerolites.Utilities.InputType;
import com.teamtwo.aerolites.Utilities.LevelConfig;
import com.teamtwo.engine.Input.Controllers.PlayerNumber;
import com.teamtwo.engine.Physics.World;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.audio.Music;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

import java.util.ArrayList;

import static com.teamtwo.aerolites.Entities.Entity.Type.*;

/**
 * @author Matthew Threlfall
 */
public class PlayState extends State {

    private World world;
    private ArrayList<Entity> entities;

    private Player[] players;

    // The configuration for the level
    private LevelConfig config;
    private boolean gameOver;

    private float accumulator;

    private Entity boss;
    private Entity boss2;
    private float bossTimer;
    private boolean bossSpawned;

    private boolean alertPlaying;

    private float swarmerTimer;
    private float aiTimer;
    private float asteroidTimer;

    private Entity.Type bossType;

    //TODO shoot bullets out of ait
    //TODO make power ups work
    //TODO star map
    //TODO shop and stuff
    //TODO make Tijans shit work
    //TODO tie everything together

    /**
     * Creates a new level from the configuration provided
     * @param gsm the game state manager for the entire game
     * @param config The configuration for the level
     */
    public PlayState(GameStateManager gsm, LevelConfig config) {
        super(gsm);
        bossType = Entity.Type.PascalBoss;

        this.config = config;

        // Load content and then play the level music
        loadContent(config.textured);
        Music bgm = ContentManager.instance.getMusic("PlayMusic");
        bgm.setVolume(10f);
        bgm.play();

        gameOver = false;
        world = new World(Vector2f.ZERO);
        World.BODY_COLOUR = Color.RED;

        entities = new ArrayList<>();

        bossTimer = 0;
        bossSpawned = false;

        int playerCount = 0;
        while (config.players[playerCount] != null) {
            playerCount++;
            if(playerCount == config.players.length) break;
        }

        players = new Player[playerCount];
        int controllerNumber = 0;
        for(int i = 0; i < playerCount; i++) {
            if(config.players[i] == InputType.Controller) {
                players[i] = new Player(world, PlayerNumber.values()[i], controllerNumber++);
            }
            else {
                players[i] = new Player(world, PlayerNumber.values()[i]);
            }
        }

        config.asteroidBaseRate /= (1.8f * playerCount);
        config.swarmerBaseRate /= (float) playerCount;
        config.aiBaseRate /= (float) playerCount;

        config.bossBaseLives *= playerCount;

        accumulator = 0;
        alertPlaying = false;
    }

    @Override
    public void update(float dt) {

        world.update(dt);
        if(!gameOver)
            bossTimer += dt;

        int alivePlayers = 0;
        for(Player player : players) {
            if(player.isAlive()) alivePlayers++;
        }

        if(bossTimer - 6 > config.bossSpawnTime && !bossSpawned && entities.size() == 0) {
            switch (bossType){
                case PascalBoss:
                    boss2 = new PascalBoss(world,config.bossBaseLives / 4,false);
                    boss = new PascalBoss(world,config.bossBaseLives / 4,true);
                    break;
                case Hexaboss:
                    boss = new Hexaboss(world, config.bossBaseLives);
                    break;
            }
            bossSpawned = true;
        }
        else if(bossTimer < config.bossSpawnTime) {
            spawnEntities(dt);
        }
        else if(bossSpawned && !gameOver) {

            boolean bossAlive = boss.isAlive();
            if(bossType == Entity.Type.PascalBoss)
                bossAlive = bossAlive || boss2.isAlive();
            if(!bossAlive) {
                for(Player player : players) {
                    player.setAlive(false);
                    world.removeBody(player.getBody());
                }

                gameOver = true;
                gsm.setState(new LevelOver(gsm, this, true));
            }
        }

        if(alivePlayers == 0 && !gameOver) {
            gameOver = true;
            gsm.setState(new LevelOver(gsm, this, false));
        }
        boolean alive = false;
        if(bossSpawned) {
            alive = boss.isAlive();
            if (bossType == Entity.Type.PascalBoss) alive = alive || boss2.isAlive();
        }
        if(bossSpawned && alive) {
            boss.update(dt);
            switch (bossType) {
                case Hexaboss:
                    updateHexaboss((Hexaboss) boss);
                    break;
                case PascalBoss:
                    boss2.update(dt);
                    updatePascalBoss((PascalBoss) boss);
                    updatePascalBoss((PascalBoss) boss2);
                    break;
            }
        }

        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).update(dt);
            if(!entities.get(i).isOnScreen()) {
                world.removeBody(entities.get(i).getBody());
                Entity entity = entities.remove(i);
                if(entity.getType() == Entity.Type.StandardAI) {
                    ((StandardAI) entity).dispose();
                }
                i--;
            }
            else {
                Entity e = entities.get(i);
                switch (e.getType()) {
                    case Hexaboss:
                        break;
                    case PascalBoss:
                        break;
                    case Asteroid:
                        i = updateAsteroid((Asteroid) e);
                        break;
                    case StandardAI:
                        StandardAI ai = (StandardAI) e;
                        ai.findTarget(entities, players);
                        break;
                    case SwamerBase:
                        SwarmerBase base = (SwarmerBase) e;
                        base.findTarget(players);
                        if(base.shouldSplit()) {
                            entities.remove(base);
                            i--;
                            int swarmer = MathUtil.randomInt(4, 8);
                            for(int j = 0; j < swarmer; j++) {
                                entities.add(new Swarmer(world, base.getBody().getTransform().getPosition()));
                            }

                            world.removeBody(base.getBody());
                        }
                        break;
                    case Swamer:
                        Swarmer swarmer = (Swarmer) e;
                        swarmer.findTarget(players);
                        break;
                }

                e.update(dt);
            }
        }

        for(Player player : players) {
            player.update(dt);
            if(!player.isAlive()) {
                world.removeBody(player.getBody());
                player.dispose();
            }
        }

        // Temporary -- Close game on escape
        if(Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) { game.getEngine().close(); }
    }

    public int updateHexaboss(Hexaboss h) {
        if(h.isShooting()) {
            for(int i = 0; i < h.getBulletPoints().size(); i++) {
                Vector2f v = h.getBulletPoints().get(i);
                float angle = h.getBulletAngles().get(i);
                entities.add(new Bullet(10f, v, Entity.Type.EnemyBullet, h.getBody().getTransform().getAngle()+angle, world));
                entities.get(entities.size() - 1).setMaxSpeed(250);
                h.setShooting(false);
            }
        }
        return entities.indexOf(h);
    }

    public void updatePascalBoss(PascalBoss p){
        if(p.isShooting() && p.isAlive()) {
            for(Vector2f v : p.getBulletPoints()) {
                entities.add(new Bullet(10f, v, EnemyBullet, p.getBody().getTransform().getAngle() + p.getBulletAngles().get(p.getBulletPoints().indexOf(v)), world));
                entities.get(entities.size() - 1).setMaxSpeed(250);
                p.setShooting(false);
            }
        }
        if(!p.isAlive()){
            world.removeBody(p.getBody());
        }
    }

    public int updateAsteroid(Asteroid a){
        int index = entities.indexOf(a);

        Vector2f pos = a.getBody().getTransform().getPosition();

        if(a.shouldExplode()) {

            int powerUp = MathUtil.randomInt(1, 10);
            if(powerUp == 1)
            {
                powerUp = MathUtil.randomInt(1, 4);
                Entity.Type type;
                switch (powerUp) {
                    case 1:
                        type = Shield;
                        break;
                    case 2:
                        type = Life;
                        break;
                    case 3:
                        type = ShotSpeed;
                        break;
                    default:
                        type = null;
                }


                entities.add(new PowerUpPickUp(type, 20, pos, world));
            }


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

    public void spawnEntities(float dt) {
        accumulator += dt;
        swarmerTimer += dt;
        aiTimer += dt;

        if(accumulator > config.asteroidBaseRate) {
            entities.add(new Asteroid(world));
            config.asteroidBaseRate = MathUtil.clamp(0.99f * config.asteroidBaseRate, 0.6f, 3);
            accumulator = 0;
        }
        if(swarmerTimer > config.swarmerBaseRate) {
            entities.add(new SwarmerBase(world));
            config.swarmerBaseRate = MathUtil.clamp(0.99f * config.swarmerBaseRate, 4f, 10);
            swarmerTimer = 0;
        }
        if(aiTimer > config.aiBaseRate) {
            entities.add(new StandardAI(world));
           aiTimer = 0;
        }
    }

    @Override
    public void render() {

        window.setTitle("FPS: " + game.getEngine().getFps());

        for(Entity entity : entities) {
            entity.render(window);
        }

        for(Player player : players) {
            player.render(window);
            if(player.isAlive()) {
                int number = player.getNumber().ordinal();
                Text text = new Text("Player " + (number + 1), ContentManager.instance.getFont("Ubuntu"), 28);
                text.setStyle(TextStyle.BOLD);
                text.setPosition(15, 25 + (number * 60));
                window.draw(text);

                for(int i = 0; i < player.getLives() + 1; i++) {
                    ConvexShape shape = new ConvexShape(player.getBody().getShape().getVertices());
                    shape.setPosition(150 + (i * 30), 50 + (number * 60));
                    shape.setFillColor(player.getDefaultColour());
                    window.draw(shape);
                }
            }
        }
        Text text = new Text("Time Survived " + MathUtil.round(bossTimer,2)+"s", ContentManager.instance.getFont("Ubuntu"), 28);
        text.setStyle(TextStyle.BOLD);
        text.setPosition(15, 25 + ((players.length) * 60));
        window.draw(text);

        if(bossSpawned && bossType == Entity.Type.PascalBoss)
            if(boss2.isAlive()) boss2.render(window);
        if(bossSpawned && boss.isAlive()) {
            boss.render(window);
        }

        boolean showText = bossTimer > config.bossSpawnTime - 6 && bossTimer < config.bossSpawnTime + 10
                && MathUtil.round(bossTimer % 1f, 0) == 0;

        if(showText) {

            text = new Text("Danger! Boss Approaching!", ContentManager.instance.getFont("Ubuntu"), 36);
            text.setStyle(Text.BOLD | TextStyle.UNDERLINED);
            text.setColor(Color.RED);
            FloatRect screenRect = text.getLocalBounds();
            text.setOrigin(screenRect.width / 2, 0);
            text.setPosition(State.WORLD_SIZE.x / 2, 40);
            window.draw(text);

            if(!alertPlaying) {
                ContentManager.instance.getSound("Alert").play();
                alertPlaying = true;
            }
        }
        else {
            alertPlaying = false;
        }

       world.render(window);
    }

    @Override
    public void dispose() {}

    private void loadContent(boolean textured) {


        // Load Textures
        if(textured) {
            ContentManager.instance.loadTexture("Asteroid", "Asteroid.png");
            ContentManager.instance.loadTexture("Player", "Player.png");
        }
        else {
            ContentManager.instance.loadTexture("Asteroid", "Retro.png");
            ContentManager.instance.loadTexture("Player", "Retro.png");
        }

        // Load Fonts
        ContentManager.instance.loadFont("Ubuntu","Ubuntu.ttf");

        // Load Sounds
        ContentManager.instance.loadSound("Pew", "pew.wav");
        ContentManager.instance.loadSound("Explode_1", "explode.wav");
        ContentManager.instance.loadSound("Explode_2", "explode2.wav");
        ContentManager.instance.loadSound("Explode_3", "explode3.wav");
        ContentManager.instance.loadSound("Alert", "alert.wav");
        ContentManager.instance.getSound("Alert").setVolume(50f);

        // Load Music
        ContentManager.instance.loadMusic("PlayMusic", "Music.wav");
        ContentManager.instance.loadMusic("Hexagon", "Focus.ogg");
        ContentManager.instance.loadMusic("Pascal", "Pascal.ogg");
    }

    public Player[] getPlayers() { return players; }
}
