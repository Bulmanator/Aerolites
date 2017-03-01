package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Utilities.Score;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

import static com.teamtwo.aerolites.States.GameOver.Stage.*;

public class GameOver extends State {

    public enum Stage{
        Intro,
        Stretch,
        Display
    }

    private Font font;
    private Text congrats;

    private Color[] colours;
    private int colourIndex;
    private float timePerColour;
    private Stage currentStage;

    private float accumulator;
    private RectangleShape background;
    private float speed;

    private Score[] scores;

    public GameOver(GameStateManager gsm, Score[] scores) {
        super(gsm);


        background = new RectangleShape(new Vector2f(16 * 5, 9 * 5));
        background.setPosition(new Vector2f(State.WORLD_SIZE.x,State.WORLD_SIZE.y/2));
        background.setFillColor(new Color(152,0,0, 230));

        speed = 80;

        currentStage = Intro;
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

        congrats = new Text("CONGRATULATIONS, YOU BEAT THE GAME!", font, 65);
        congrats.setPosition(State.WORLD_SIZE.x/2 - congrats.getLocalBounds().width/2, State.WORLD_SIZE.y/8 - congrats.getLocalBounds().height/2);
    }

    public void update(float dt) {
        accumulator += dt;

        congrats.setColor(Color.WHITE);

        switch(currentStage) {
            case Intro:
                if(background.getPosition().x + background.getLocalBounds().width/2 >= State.WORLD_SIZE.x / 2) {
                    background.setPosition(background.getPosition().x - speed, background.getPosition().y);
                    speed -= 3;
                }
                else {
                    background.setPosition(State.WORLD_SIZE.x / 2 - background.getSize().x/2, background.getPosition().y);
                    currentStage = Stretch;
                }
                break;
            case Stretch:
                if(background.getSize().x < State.WORLD_SIZE.x - 80) {
                    background.setOrigin(background.getSize().x/2 , background.getSize().y/2);
                    background.setSize(new Vector2f(background.getSize().x + speed, background.getSize().y + (speed/16) * 9));
                    speed += 1.4f;
                }
                else
                    currentStage = Display;
                break;
            case Display:

                break;
        }
    }

    public void render() {
        window.draw(background);
        if(currentStage == Display)
            window.draw(congrats);
    }

    public void dispose() {}
}
