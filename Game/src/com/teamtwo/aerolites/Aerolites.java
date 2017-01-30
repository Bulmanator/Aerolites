package com.teamtwo.aerolites;

import com.teamtwo.aerolites.States.ControllerTests;
import com.teamtwo.engine.Game;
import com.teamtwo.engine.Utilities.State.GameStateManager;

public class Aerolites extends Game {

    /** A manager for all of the game states */
    private GameStateManager stateManager;

    /** This method is called once before the game begins running */
    public void initialise() {
        stateManager = new GameStateManager(this);
        stateManager.addState(new ControllerTests(stateManager));
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
