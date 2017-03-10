package com.teamtwo.aerolites.States;

import com.teamtwo.engine.Input.Controllers.Button;
import com.teamtwo.engine.Input.Controllers.ControllerState;
import com.teamtwo.engine.Input.Controllers.Controllers;
import com.teamtwo.engine.Input.Controllers.PlayerNumber;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.*;
import org.jsfml.window.Keyboard;


/**
 * @author Tijan Weir
 */
public class CreditState extends State {

    private static final float timerPerColour = 0.5f;

    private Text[] names;
    private Text[] musicCredit;
    private RectangleShape background;

    private float colourTimer;
    private int currentColour;

    private boolean prevEscape;
    private ControllerState prevState;

    private Color[] colours;

    private boolean enabled;
    private Button[] code = new Button[] {
            Button.DPad_Up, Button.DPad_Up, Button.DPad_Down, Button.DPad_Down, Button.DPad_Left, Button.DPad_Right,
            Button.DPad_Left, Button.DPad_Right, Button.B, Button.A
    };
    private int currentIndex;

    public CreditState(GameStateManager gsm) {
        super(gsm);
        Font font = ContentManager.instance.getFont("Ubuntu");
        background = new RectangleShape(State.WORLD_SIZE);
        background.setPosition(0, 0);
        background.setTexture(ContentManager.instance.getTexture("Space"));

        colours = new Color[] {
                Color.RED, Color.YELLOW, new Color(255, 45, 195),
                Color.GREEN, new Color(255, 114, 38), Color.MAGENTA,
                Color.CYAN
        };

        names = new Text[6];

        int fontSize = 40;

        names[0] = new Text("Matthew Threfall", font, fontSize);
        names[1] = new Text("James Bulman", font, fontSize);
        names[2] = new Text("Tijan Weir", font, fontSize);
        names[3] = new Text("Ayo Olutobi", font, fontSize);
        names[4] = new Text("Pavlos Anastasiadis", font, fontSize);
        names[5] = new Text("Lewis Linaker", font, fontSize);

        musicCredit = new Text[4];
        musicCredit[0] = new Text("Perturbator - Complete Domination (Feat. Carpenter Brut)", font, fontSize);
        musicCredit[1] = new Text("Chipzel - Focus (? Remix)", font, fontSize);
        musicCredit[2] = new Text("Sabrepulse - First Crush (Feat. Knife City)", font, fontSize);
        musicCredit[3] = new Text("Quadtron Boss music name goes here", font, fontSize);


        for(int i = 0; i < names.length; i++) {
            names[i].setPosition(380, 100 + (i * 70));
        }

        for(int i = 0; i < musicCredit.length; i++) {
            musicCredit[i].setPosition(380, 580 + (i * 80));
        }

        currentIndex = 0;

        prevEscape = false;
        prevState = Controllers.getState(PlayerNumber.One);

        enabled = true;
    }

    /**
     * This is called once per frame, used to perform any updates required
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {
        ControllerState state = Controllers.getState(PlayerNumber.One);

        if(currentIndex != 8) {
            if (!Keyboard.isKeyPressed(Keyboard.Key.ESCAPE) && prevEscape) {
                gsm.popState();
            }
            else if (!state.button(Button.B) && prevState.button(Button.B)) {
                gsm.popState();
            }
        }


        colourTimer += dt;

        float ratio = colourTimer / timerPerColour;

        for(int i = 0; i < names.length; i++) {
            names[i].setColor(MathUtil.lerpColour(colours[(currentColour + i) % 6],
                    colours[(currentColour + i + 1) % 6], ratio));
        }

        if(colourTimer >= timerPerColour) {
            colourTimer = 0;
            currentColour++;
        }

        for (Button button : Button.values()) {
            if (!state.button(button) && prevState.button(button)) {
                if (button == code[currentIndex]) {
                    currentIndex++;
                    if(currentIndex == code.length) {
                        enabled = !enabled;
                        currentIndex = 0;
                    }
                }
                else {
                    currentIndex = 0;
                }
            }
        }

        prevState = state;
        prevEscape = Keyboard.isKeyPressed(Keyboard.Key.ESCAPE);
    }

    public void render() {

        Font font = ContentManager.instance.getFont("Ubuntu");

        window.draw(background);

        Text music = new Text("Music", font, 85);
        music.setPosition(255, 840);
        music.setStyle(TextStyle.BOLD | TextStyle.UNDERLINED);
        music.setColor(Color.GREEN);
        music.setRotation(-90);

        window.draw(music);

        music.setString("Development");
        music.setCharacterSize(55);
        music.setPosition(285, 475);
        window.draw(music);

        for(Text text : names) { window.draw(text); }
        for(Text text : musicCredit) { window.draw(text); }

        if(enabled) {
            Text superHot = new Text("Super Hot Mode Enabled!", font, 55);
            superHot.setPosition(1040, 260);
            superHot.setStyle(TextStyle.BOLD | TextStyle.UNDERLINED);
            superHot.setColor(Color.GREEN);

            window.draw(superHot);
        }
    }

    @Override
    public void dispose() {

    }

}
