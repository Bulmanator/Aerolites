package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.ExampleInput;
import com.teamtwo.aerolites.UI.Button;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;


/**
 * @author Tijan Weir
 */
public class CreditState extends State {
    Button[] Buttons = new Button[6];
    Text text;
    private ExampleInput hoverBoxChoices;

    public void render() {
        for (int i = 0; i < Buttons.length; i++) {
            Buttons[i].render(window);
        }
    }

    @Override
    public void dispose() {

    }

    //
    // This is the menu game state
    // Game states can be used to implement separate parts of the game
    // Such as levels, menus etc.
    // Just extend the State class to make a new State and you can add/ remove states from the Game State Manager
    //
    public CreditState(GameStateManager gsm) {
        super(gsm);
        //ContentManager.instance.loadFont("Ubuntu", "Ubuntu.ttf");

        text = new Text("Aerolites", ContentManager.instance.getFont("Ubuntu"));
        text.setPosition(window.getSize().x / 2 - text.getLocalBounds().width / 2, window.getSize().y / 2);
        text.setColor(Color.BLACK);


        Buttons[0] = new Button((int)State.WORLD_SIZE.x  / 2, (int)State.WORLD_SIZE.y / 42 * 6, window.getSize().x / 4, window.getSize().y / 10, "James Bulman");
        Buttons[1] = new Button((int)State.WORLD_SIZE.x  / 2, (int)State.WORLD_SIZE.y / 42 * 12, window.getSize().x / 4, window.getSize().y / 10, "Matt Threlfall");
        Buttons[2] = new Button((int)State.WORLD_SIZE.x  / 2, (int)State.WORLD_SIZE.y / 42 * 18, window.getSize().x / 4, window.getSize().y / 10, "Ayo Olutobi");
        Buttons[3] = new Button((int)State.WORLD_SIZE.x  / 2, (int)State.WORLD_SIZE.y / 42 * 24, window.getSize().x / 4, window.getSize().y / 10, "Tijan Weir");
        Buttons[4] = new Button((int)State.WORLD_SIZE.x / 2, (int)State.WORLD_SIZE.y / 42 * 30, window.getSize().x / 4, window.getSize().y / 10, "Pavlos Anastasiadis");
        Buttons[5] = new Button((int)State.WORLD_SIZE.x  / 2, (int)State.WORLD_SIZE.y / 42 * 36, window.getSize().x / 4, window.getSize().y / 10, "Lewis Linaker");


    }

    /**
     * This is called once per frame, used to perform any updates required
     *
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {
        if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) {
            gsm.popState();
        }
    }

}
