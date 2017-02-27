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
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Text;
import org.jsfml.window.Keyboard;

import java.util.HashMap;


/**
 * @author Tijan Weir
 */
public class PlayerSelect extends State {

    private static final Color[] colours = new Color[] {
            new Color(61, 64, 255), new Color(255, 228, 94), new Color(123, 255, 94),
            new Color(124, 255, 189), new Color(124, 235, 255), new Color(244, 75, 66),
            new Color(204, 86, 255), new Color(255, 107, 210)
    };

    private InputType[] types;
    private int nextInput;
    private boolean keyboardTaken;

    private ControllerState[] prevStates;
    private boolean prevEscape;
    private boolean prevSpace;

    private boolean ready;
    private float readyTimer;

    private Font font;

    public PlayerSelect(GameStateManager gsm, InputType input) {
        super(gsm);

        keyboardTaken = input == InputType.Keyboard;
        types = new InputType[8];
        types[0] = input;

        nextInput = 1;

        ready = false;

        prevStates = new ControllerState[8];
        for(PlayerNumber player : PlayerNumber.values()) {
            prevStates[player.ordinal()] = Controllers.getState(player);
        }
        prevEscape = Keyboard.isKeyPressed(Keyboard.Key.ESCAPE);
        prevSpace = Keyboard.isKeyPressed(Keyboard.Key.SPACE);

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

        if(!ready) {
            int offset = types[0] == InputType.Controller ? 0 : 1;
            for (int i = 1; i < types.length; i++) {
                int cNum = i - offset;

                if (types[i] == null) {
                    if (states[cNum].button(Button.A)) {
                        types[i] = InputType.Controller;
                    } else if (!keyboardTaken && Keyboard.isKeyPressed(Keyboard.Key.SPACE)) {
                        keyboardTaken = true;
                        types[i] = InputType.Keyboard;
                        offset = 1;
                    }
                } else if (types[i] == InputType.Keyboard) {
                    offset = 1;
                    if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) {
                        keyboardTaken = false;
                        types[i] = null;
                        System.arraycopy(types, i + 1, types, i, types.length - 1 - i);
                        i--;
                        offset = 0;
                    }
                } else {
                    if (states[cNum].button(Button.B) && !prevStates[cNum].button(Button.B)) {
                        types[i] = null;
                        i--;
                    }
                }
            }

            if(types[0] == InputType.Controller) {
                if(!states[0].button(Button.B) && prevStates[0].button(Button.B)) {
                    gsm.popState();
                }
                else if(states[0].button(Button.A) && !prevStates[0].button(Button.A)) {
                    System.out.println("Readying up!");
                    ready = true;
                }
            }
            else {
                if(!Keyboard.isKeyPressed(Keyboard.Key.ESCAPE) && prevEscape) {
                    gsm.popState();
                }
                else if(Keyboard.isKeyPressed(Keyboard.Key.SPACE) && !prevSpace) {
                    System.out.println("Readying up!");
                    ready = true;
                }
            }
        }
        else {
            readyTimer += dt;

            if(readyTimer >= 5f) {
                int playerCount = 0;
                for (InputType type : types) {
                    if (type != null) playerCount++;
                }

                InputType[] playerTypes = new InputType[playerCount];
                playerCount = 0;
                for (InputType type : types) {
                    if(type != null) {
                        playerTypes[playerCount] = type;
                        playerCount++;
                    }
                }

                gsm.setState(new StarMap(gsm, playerTypes));
            }
            else if(types[0] == InputType.Controller && !states[0].button(Button.B) && prevStates[0].button(Button.B)) {
                ready = false;
                readyTimer = 0;
                System.out.println("Unreadying!");
            }
            else if(!Keyboard.isKeyPressed(Keyboard.Key.ESCAPE) && prevEscape) {
                ready = false;
                readyTimer = 0;
                System.out.println("Unreadying!");
            }
        }


        prevEscape = Keyboard.isKeyPressed(Keyboard.Key.ESCAPE);
        prevSpace = Keyboard.isKeyPressed(Keyboard.Key.SPACE);
        prevStates = states;
    }

    public void render() {
        int playerCount = 0;

        for (InputType type : types) {
            if (type != null) {
                float x = State.WORLD_SIZE.x / 4f;
                ConvexShape shape = new ConvexShape(Player.vertices);
                shape.setPosition(350 + (x * playerCount), ((playerCount / 2) * 300f) + 150f);
                shape.setFillColor(colours[playerCount]);
                window.draw(shape);

                Text input = new Text("Input Type: " + type, font, 30);
                input.setPosition(shape.getPosition().x -
                        (input.getLocalBounds().width / 2f), shape.getPosition().y + 50);
                window.draw(input);

                playerCount++;
            }
        }
    }

    public void dispose() {}
}
