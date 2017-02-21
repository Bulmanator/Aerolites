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
import org.jsfml.window.Window;


/**
 * @author Tijan Weir
 */
public class MenuState extends State {
    Button[] Buttons = new Button[5];
    Text text;
    Boolean clicked;

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


        Buttons[0] = new Button((int) State.WORLD_SIZE.x / 2, window.getSize().y / 20 * 8, (int) State.WORLD_SIZE.y / 4, window.getSize().y / 10, "Singleplayer");
        Buttons[1] = new Button((int) State.WORLD_SIZE.x / 2, window.getSize().y / 20 * 12, (int) State.WORLD_SIZE.y / 4, window.getSize().y / 10, "Multiplayer");
        Buttons[2] = new Button((int) State.WORLD_SIZE.x / 2, window.getSize().y / 20 * 16, (int) State.WORLD_SIZE.y / 4, window.getSize().y / 10, "Options");
        Buttons[3] = new Button((int) State.WORLD_SIZE.x / 2, window.getSize().y / 20 * 20, (int) State.WORLD_SIZE.y / 4, window.getSize().y / 10, "Credits");
        Buttons[4] = new Button((int) State.WORLD_SIZE.x / 2, window.getSize().y / 20 * 4, (int) State.WORLD_SIZE.y / 2, window.getSize().y / 10, "Aerolites");



    }

    /**
     * This is called once per frame, used to perform any updates required
     *
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {


        //checks if the mouse is inside a box
        for (int i = 0; i < Buttons.length; i++) {
            Vector2f pos = window.mapPixelToCoords(Mouse.getPosition(window));

            //TODO I need to record the values given when clicked and comapre to next state to stop autoclicking througn pages

            if (Buttons[i].isClicked() && !Mouse.isButtonPressed(Mouse.Button.LEFT)) {
                if (Buttons[i].getLabel().equals("Singleplayer")) {
                    gsm.addState(new ControllerSelectState(gsm)); //change here to one for keyboard and controller
                } else if (Buttons[i].getLabel().equals("Multiplayer")) {
                    gsm.addState(new MultiplayerMenuState(gsm));
                } else if (Buttons[i].getLabel().equals("Credits")) {
                    gsm.addState(new CreditState(gsm));
                }
            }
            Buttons[i].checkInBox(pos);

        }


    }

}
