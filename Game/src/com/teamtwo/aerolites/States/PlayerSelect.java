package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Entities.Player;
import com.teamtwo.aerolites.Utilities.InputType;
import com.teamtwo.engine.Input.Controllers.Button;
import com.teamtwo.engine.Input.Controllers.ControllerState;
import com.teamtwo.engine.Input.Controllers.Controllers;
import com.teamtwo.engine.Input.Controllers.PlayerNumber;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Text;
import org.jsfml.window.Keyboard;


/**
 * @author Tijan Weir
 */
public class PlayerSelect extends State {

    private boolean lastQuit;

    private InputType defaultInput;

    private InputType[] inputTypes;
    private boolean keyboardTaken;

    private Font font;

    public PlayerSelect(GameStateManager gsm, InputType input) {
        super(gsm);

        defaultInput = input;

        keyboardTaken = defaultInput == InputType.Keyboard;

        inputTypes = new InputType[8];
        inputTypes[0] = defaultInput;

        lastQuit = false;

        font = ContentManager.instance.getFont("Ubuntu");
    }

    /**
     * This is called once per frame, used to perform any updates required
     *
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {

        ControllerState[] states = new ControllerState[8];
        int count = 0;
        for(PlayerNumber player : PlayerNumber.values()) {
            states[count] = Controllers.getState(player);
            count++;
        }


        if(defaultInput == InputType.Controller) {
            if(!states[0].button(Button.B) && lastQuit) {
                gsm.popState();
            }

            lastQuit = states[0].button(Button.B);

            for(int i = 1; i < states.length; i++) {
                if (inputTypes[i] == null) {
                    if(states[i].button(Button.A)) {
                        inputTypes[i] = InputType.Controller;
                        System.out.println("Controller Connected!");
                    }
                    else if(!keyboardTaken && Keyboard.isKeyPressed(Keyboard.Key.SPACE)) {
                        keyboardTaken = true;
                        inputTypes[i] = InputType.Keyboard;
                        System.out.println("Keyboard Now in use");
                    }
                }
                else if(inputTypes[i] == InputType.Controller) {
                    if(states[i].button(Button.B)) {
                        inputTypes[i] = null;
                        System.out.println("Controller Disconnected!");
                    }
                }
                else {
                    if(Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) {
                        inputTypes[i] = null;
                        keyboardTaken = false;
                        System.out.println("Keyboard not in use");
                    }
                }
            }
        }
        else {
            if (!Keyboard.isKeyPressed(Keyboard.Key.ESCAPE) && lastQuit) {
                gsm.popState();
            }

            for(int i = 0; i < states.length - 1; i++) {
                if (inputTypes[i + 1] == null) {
                    if(states[i].button(Button.A)) {
                        inputTypes[i + 1] = InputType.Controller;
                        System.out.println("Controller Connected!");
                    }
                    else if(!keyboardTaken && Keyboard.isKeyPressed(Keyboard.Key.SPACE)) {
                        keyboardTaken = true;
                    }
                }
                else if(inputTypes[i + 1] == InputType.Controller) {
                    if(states[i].button(Button.B)) {
                        inputTypes[i + 1] = null;
                        System.out.println("Controller Disconnected!");
                    }
                }
                else {
                    if(Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) {
                        inputTypes[i + 1] = null;
                        keyboardTaken = false;
                    }
                }
            }

            lastQuit = Keyboard.isKeyPressed(Keyboard.Key.ESCAPE);
        }


    }

    public void render() {
        int playerCount = 0;
        for(int i = 0; i < inputTypes.length; i++) {
            if(inputTypes[i] != null) {
                float x = State.WORLD_SIZE.x /  4f;
                ConvexShape shape = new ConvexShape(Player.vertices);
                shape.setPosition(150 + (x * playerCount), (((i + 1) / 3) * 150f) + 150f);
                window.draw(shape);

                Text input = new Text("Input Type: " + inputTypes[i], font, 30);
                input.setPosition((x * playerCount), 200);
                window.draw(input);

                playerCount++;
            }
        }
    }

    public void dispose() {}
}
