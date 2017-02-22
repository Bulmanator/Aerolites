package com.teamtwo.aerolites.States;

import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.Text;
import org.jsfml.window.Keyboard;


/**
 * @author Tijan Weir
 */
public class CreditState extends State {

    private static final float timerPerColour = 0.5f;

    private Text[] names;
    private RectangleShape background;

    private float colourTimer;
    private int currentColour;

    private boolean prevEscape;

    private Color[] colours;

    public CreditState(GameStateManager gsm) {
        super(gsm);
        Font font = ContentManager.instance.getFont("Ubuntu");

        background = new RectangleShape(State.WORLD_SIZE);
        background.setPosition(0, 0);
        background.setTexture(ContentManager.instance.getTexture("Background"));

/*        Buttons[0] = new UIButton((int)State.WORLD_SIZE.x  / 2, (int)State.WORLD_SIZE.y / 42 * 6, window.getSize().x / 4, window.getSize().y / 10, "James Bulman");
        Buttons[1] = new UIButton((int)State.WORLD_SIZE.x  / 2, (int)State.WORLD_SIZE.y / 42 * 12, window.getSize().x / 4, window.getSize().y / 10, "Matt Threlfall");
        Buttons[2] = new UIButton((int)State.WORLD_SIZE.x  / 2, (int)State.WORLD_SIZE.y / 42 * 18, window.getSize().x / 4, window.getSize().y / 10, "Ayo Olutobi");
        Buttons[3] = new UIButton((int)State.WORLD_SIZE.x  / 2, (int)State.WORLD_SIZE.y / 42 * 24, window.getSize().x / 4, window.getSize().y / 10, "Tijan Weir");
        Buttons[4] = new UIButton((int)State.WORLD_SIZE.x / 2, (int)State.WORLD_SIZE.y / 42 * 30, window.getSize().x / 4, window.getSize().y / 10, "Pavlos Anastasiadis");
        Buttons[5] = new UIButton((int)State.WORLD_SIZE.x  / 2, (int)State.WORLD_SIZE.y / 42 * 36, window.getSize().x / 4, window.getSize().y / 10, "Lewis Linaker");*/

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

        for(int i = 0; i < names.length; i++) {
            names[i].setPosition((State.WORLD_SIZE.x / 2f) - (names[i].getLocalBounds().width / 2f), 100 + (i * 80));
        }

        prevEscape = false;
    }

    /**
     * This is called once per frame, used to perform any updates required
     *
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {
        if (!Keyboard.isKeyPressed(Keyboard.Key.ESCAPE) && prevEscape) {
            gsm.popState();
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

        prevEscape = Keyboard.isKeyPressed(Keyboard.Key.ESCAPE);
    }

    public void render() {
        window.draw(background);
        for(Text text : names) { window.draw(text); }
    }

    @Override
    public void dispose() {

    }

}
