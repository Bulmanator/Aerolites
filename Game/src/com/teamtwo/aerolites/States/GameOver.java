package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Utilities.Score;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Text;

public class GameOver extends State {

    private Font font;
    private Text congrats;

    private Color[] colours;
    private int colourIndex;
    private float timePerColour;

    private float accumulator;

    private Score[] scores;

    public GameOver(GameStateManager gsm, Score[] scores) {
        super(gsm);

        font = ContentManager.instance.getFont("Ubuntu");

        this.scores = scores;

        colours = new Color[] {
                Color.RED, Color.YELLOW, new Color(255, 45, 195),
                Color.GREEN, new Color(255, 114, 38), Color.MAGENTA,
                Color.CYAN
        };

        accumulator = 0;
        colourIndex = 0;
        timePerColour = 0.5f;

        congrats = new Text("CONGRATULATIONS, YOU BEAT THE GAME", font, 65);
        congrats.setPosition(160, 160);
    }

    public void update(float dt) {
        accumulator += dt;
        if(accumulator >= timePerColour) {
            colourIndex = (colourIndex + 1) % colours.length;
            accumulator = 0;
        }

        congrats.setColor(MathUtil.lerpColour(colours[colourIndex], colours[colourIndex % colours.length],
                (accumulator / timePerColour)));
    }

    public void render() {

        Text t = new Text("Player 1 Score: " + scores[0].getTotalScore(), font, 30);
        t.setPosition(450, 450);
        window.draw(t);

        window.draw(congrats);
    }

    public void dispose() {}
}
