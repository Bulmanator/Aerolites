package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Utilities.Score;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
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

    private Stage currentStage;

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

        congrats = new Text("CONGRATULATIONS, YOU BEAT THE GAME!", font, 65);
        congrats.setPosition(State.WORLD_SIZE.x/2 - congrats.getLocalBounds().width/2, State.WORLD_SIZE.y/8 - congrats.getLocalBounds().height/2);
    }

    public void update(float dt) {
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
        if(currentStage == Display) {
            window.draw(congrats);
            float playerWidth = (1920-(90 + 40*(scores.length)))/(scores.length);
            for(int i = 0; i < scores.length; i++) {
                Score score = scores[i];
                score.roundValues();

                Font font = ContentManager.instance.getFont("Ubuntu");

                Text text = new Text("Player " + (i + 1), font, 64);
                text.setOrigin(0, 0);
                text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 8 + 110);
                window.draw(text);

                text = new Text("Score: " + score.getTotalScore(), font, 32);
                text.setOrigin(0, 0);
                text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 8 + 190);
                window.draw(text);

                text = new Text("Asteroids Destroyed: " + score.getTotalAsteroids(), font, 32);
                text.setOrigin(0, 0);
                text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 8 + 230);
                window.draw(text);

                text = new Text("Enemies Killed: " + score.getTotalEnemies(), font, 32);
                text.setOrigin(0, 0);
                text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 8 + 270);
                window.draw(text);

                float seconds = score.getTotalTimeAlive();
                int minutes = (int)(seconds / 60f);

                seconds = MathUtil.round(seconds % 60f, 2);

                text = new Text("Time Survived: " + ((minutes > 0) ? minutes + "m " : "") + seconds + "s", font, 32);

                text.setOrigin(0, 0);
                text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 8 + 310);
                window.draw(text);

                text = new Text("Bullets Fired: " + score.getTotalBulletsFired(), font, 32);
                text.setOrigin(0, 0);
                text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 8 + 350);
                window.draw(text);

                text = new Text("Bullets Missed: " + score.getTotalBulletsMissed(), font, 32);
                text.setOrigin(0, 0);
                text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 8 + 390);
                window.draw(text);

                text = new Text("Accuracy: " + score.getTotalAccuracy() + "%", font, 32);
                text.setOrigin(0, 0);
                text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 8 + 430);
                window.draw(text);

                seconds = score.getTotalTimeBoosting();
                minutes = (int)(seconds / 60f);

                seconds = MathUtil.round(seconds % 60f, 2);

                text = new Text("Time Spent Boosting: " +
                        ((minutes > 0) ? minutes + "m " : "") + seconds + "s", font, 32);
                text.setOrigin(0, 0);
                text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 8 + 470);
                window.draw(text);
            }
        }
    }

    public void dispose() {}
}
