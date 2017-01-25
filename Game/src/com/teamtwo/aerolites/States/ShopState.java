package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.ExampleInput;
import com.teamtwo.aerolites.UI.Button;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;


    public class ShopState extends State {

        Button shopMenuButton;
        Button shield;
        Button fireRateUp;
        Button newShip;
        Button buyANewLife;
        Button AlFriendlyShip;
        Button laserCannon;
        Button selfDestruct;
        Button speedUp;
        Button fasterTurning;
        Button AlTurrets;

        Button[] Buttons = new Button[5];


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
        public ShopState(GameStateManager gsm) {
            super(gsm);
            ContentManager.instance.loadFont("Ubuntu", "Ubuntu.ttf");

        /*hoverBoxChoices = new ExampleInput();
        game.getEngine().setInputHandler(hoverBoxChoices);*/

           Buttons[0] = new Button(window.getSize().x / 2, window.getSize().y / 30*8, window.getSize().x / 4, window.getSize().y / 10, "Shield"); //TODO replace this button with actual text

           Buttons[1] = new Button(window.getSize().x / 2, window.getSize().y / 30*12, window.getSize().x / 4, window.getSize().y / 10, "Fire Rateup");

           Buttons[2] = new Button(window.getSize().x / 2, window.getSize().y / 30*16, window.getSize().x / 4, window.getSize().y / 10, "Buy A New Life");
           Buttons[3] = new Button(window.getSize().x / 2, window.getSize().y / 30*20, window.getSize().x / 4, window.getSize().y / 10, "Al Friendly Ship");

           Buttons[4] = new Button(window.getSize().x / 2, window.getSize().y / 30*24, window.getSize().x / 4, window.getSize().y / 10, "Laser Cannon");
        //   Buttons[5] = new Button(window.getSize().x / 2, window.getSize().y / 30*28, window.getSize().x / 4, window.getSize().y / 10, "Laser Cannon");

        }

        /**
         * This is called once per frame, used to perform any updates required
         *
         * @param dt The amount of time passed since last frame
         */
        public void update(float dt) {

        }

    }
