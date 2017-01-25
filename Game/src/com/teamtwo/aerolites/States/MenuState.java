package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.ExampleInput;
import com.teamtwo.aerolites.UI.Button;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;

public class MenuState extends State {
    Button[] Buttons = new Button[4];
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
    public MenuState(GameStateManager gsm) {
        super(gsm);
        ContentManager.instance.loadFont("Ubuntu", "Ubuntu.ttf");

        text = new Text("Aerolites", ContentManager.instance.getFont("Ubuntu"));
        text.setPosition(window.getSize().x / 2 - text.getLocalBounds().width / 2, window.getSize().y / 2);
        text.setColor(Color.BLACK);


        Buttons[0] = new Button(window.getSize().x / 2, window.getSize().y / 20 * 4, window.getSize().x / 4, window.getSize().y / 10, "New Game");
        Buttons[1] = new Button(window.getSize().x / 2, window.getSize().y / 20 * 8, window.getSize().x / 4, window.getSize().y / 10, "Continue Game");
        Buttons[2] = new Button(window.getSize().x / 2, window.getSize().y / 20 * 12, window.getSize().x / 4, window.getSize().y / 10, "Options");
        Buttons[3] = new Button(window.getSize().x / 2, window.getSize().y / 20 * 16, window.getSize().x / 4, window.getSize().y / 10, "Credits");


    }

    /**
     * This is called once per frame, used to perform any updates required
     *
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {
        for(int i = 0; i < Buttons.length; i++)
        {
            if(Buttons[i].checkInBox(new Vector2f(Mouse.getPosition(window))))System.out.println("it worked");
        }

    }

}
