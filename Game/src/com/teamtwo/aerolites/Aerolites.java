package com.teamtwo.aerolites;

import com.teamtwo.aerolites.States.Testing;
import com.teamtwo.engine.Utilities.Game;
import com.teamtwo.engine.Utilities.State.GameStateManager;

public class Aerolites extends Game {

    private GameStateManager gsm;
    private Controls controls;

    public void initialise() {

        engine.setInputHandler(controls = new Controls());

        gsm = new GameStateManager(this);
        gsm.addState(new Testing(gsm));

    }

    public void update(float dt) {
        gsm.update(dt);
    }

    public void render() {
        window.clear(/* new Color(100, 149, 237) */);
        gsm.render();
    }

    public void dispose() {}

    public Controls getControls() {
        return controls;
    }
}
