package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.ExampleInput;
import com.teamtwo.aerolites.UI.Button;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;


public class MenuState extends State {
    Button MenuButton;
    Button NewGameButton;
    Button ContinueGameButton;
    Button OptionsButton;
    Button Credits;

    Button[] Buttons = new Button[4];



    private ExampleInput hoverBoxChoices;



    public void render() {
        //spaceAnimation.render(window);
        MenuButton.render(window);
        NewGameButton.render(window);
        ContinueGameButton.render(window);
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
        /*hoverBoxChoices = new ExampleInput();
        game.getEngine().setInputHandler(hoverBoxChoices);*/

        MenuButton = new Button(window.getSize().x/2, window.getSize().y/10, window.getSize().x/4, window.getSize().y/10, "Menu"); //TODO replace this button with actual text

        NewGameButton = new Button(window.getSize().x/2, window.getSize().y/10*4, window.getSize().x/4, window.getSize().y/10, "New Game");



    }

    /**
     * This is called once per frame, used to perform any updates required
     *
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {

    }

}
