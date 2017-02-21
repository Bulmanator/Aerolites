package com.teamtwo.aerolites;

import com.teamtwo.aerolites.States.StarMap;
import com.teamtwo.aerolites.Utilities.InputType;
import com.teamtwo.aerolites.Utilities.LevelConfig;
import com.teamtwo.engine.Game;
import com.teamtwo.engine.Utilities.State.GameStateManager;

public class Aerolites extends Game {

    /** A manager for all of the game states */
    private GameStateManager stateManager;

    /** This method is called once before the game begins running */
    public void initialise() {
        stateManager = new GameStateManager(this);

        int playerCount = 2;

        LevelConfig config = new LevelConfig();
        config.asteroidBaseRate = 2.4f / (playerCount * 1.8f);
        config.swarmerBaseRate = 16.0f / (float) playerCount;
        config.aiBaseRate = 14.0f / (float) playerCount;
        config.textured = true;

        config.bossBaseLives = 180;
        config.bossSpawnTime = 120;

        config.players[0] = InputType.Controller;
        config.players[1] = InputType.Controller;

        stateManager.addState(new PlayState(stateManager, config));
    }

    /**
     * This is called once per frame, used to perform any updates required
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {
        stateManager.update(dt);
    }

    /** This is also called once per frame, used to draw anything needed */
    public void render() {
        window.clear();
        stateManager.render();
    }

    /** This is called at the end of execution to destroy any unwanted objects */
    public void dispose() {}
}
