package com.teamtwo.aerolites.States;

import com.teamtwo.engine.Input.Controllers.Controller;
import com.teamtwo.engine.Input.Controllers.Controllers;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

/**
 * @author Matthew Threlfall
 */
public class GameOver extends State {
    public enum Stage{
        Gameover,
        Scores

    }
    private PlayState background;
    private Stage current;
    private float backgroundPos;
    private float backgroundYMovement;
    private int playerCount;


    public GameOver(GameStateManager gsm, PlayState background, int playerCount) {
        super(gsm);
        this.background = background;
        this.playerCount = playerCount;
        if(playerCount<0){
            this.playerCount = -(playerCount);
        }
        current = Stage.Gameover;
        backgroundPos = 0;
        backgroundYMovement = 0;
    }

    @Override
    public void update(float dt) {
        background.update(dt);
        if(Keyboard.isKeyPressed(Keyboard.Key.ESCAPE))
            game.getEngine().close();
        switch (current){
            case Gameover:
                if(backgroundPos < 1920) {
                    backgroundPos += 6500*dt;
                }
                else if(backgroundPos > 1920) {
                    backgroundPos = 1920;
                }
                break;
            case Scores:
                if(backgroundPos < 1920*2) {
                    backgroundPos += 6500*dt;
                }
                else if(backgroundPos > 1920*2) {
                    backgroundPos = 1920*2;
                }
                if(backgroundPos == 1920*2){
                    if(backgroundYMovement<400)
                        backgroundYMovement += 6500*dt;
                    else if(backgroundYMovement > 400)
                        backgroundYMovement = 400;
                }
        }
        switch(playerCount){
            case 4:
                if(current == Stage.Gameover && Controllers.isButtonPressed(Controller.Player.Four, Controller.Button.A))
                    current = Stage.Scores;
            case 3:
                if(current == Stage.Gameover && Controllers.isButtonPressed(Controller.Player.Three, Controller.Button.A))
                    current = Stage.Scores;
            case 2:
                if(current == Stage.Gameover && Controllers.isButtonPressed(Controller.Player.Two, Controller.Button.A))
                    current = Stage.Scores;
            case 1:
                if(current == Stage.Gameover && Controllers.isButtonPressed(Controller.Player.One, Controller.Button.A))
                    current = Stage.Scores;
            case 0:
                if(current == Stage.Gameover && Keyboard.isKeyPressed(Keyboard.Key.SPACE))
                    current = Stage.Scores;
        }
    }

    @Override
    public void render() {
        background.render();
        switch(current) {
            case Gameover:
                Text gameoverText = new Text("Game Over", ContentManager.instance.getFont("Ubuntu"), 120);

                RectangleShape box = new RectangleShape(new Vector2f(0,WORLD_SIZE.y/2-40));
                box.setPosition(0,WORLD_SIZE.y/2-90);
                box.setSize(new Vector2f(backgroundPos,180f));
                box.setFillColor(new Color(255,0,0, 200));
                window.draw(box);

                box = new RectangleShape(new Vector2f(0, WORLD_SIZE.x/2-40));
                box.setPosition(0,WORLD_SIZE.y/2+90);
                box.setSize(new Vector2f(backgroundPos,60));
                box.setFillColor(new Color(122,0,0, 230));
                window.draw(box);

                gameoverText.setStyle(Text.BOLD);
                gameoverText.setColor(Color.WHITE);

                FloatRect gameoverBounds = gameoverText.getLocalBounds();
                gameoverText.setOrigin(gameoverBounds.width / 2, gameoverBounds.height);
                gameoverText.setPosition(backgroundPos - WORLD_SIZE.x / 2, WORLD_SIZE.y / 2);
                window.draw(gameoverText);

                break;
            case Scores:
                gameoverText = new Text("Game Over", ContentManager.instance.getFont("Ubuntu"), 120);

                box = new RectangleShape(new Vector2f(0,WORLD_SIZE.y/2-40));
                box.setPosition(0,WORLD_SIZE.y/2-90 - backgroundYMovement);
                box.setSize(new Vector2f(backgroundPos,180f));
                box.setFillColor(new Color(255,0,0, 200));
                window.draw(box);

                box = new RectangleShape(new Vector2f(0, WORLD_SIZE.x/2-40));
                box.setPosition(0,WORLD_SIZE.y/2+90 - backgroundYMovement);
                box.setSize(new Vector2f(backgroundPos,60 + backgroundYMovement*1.8f));
                box.setFillColor(new Color(122,0,0, 230));
                window.draw(box);

                gameoverText.setStyle(Text.BOLD);
                gameoverText.setColor(Color.WHITE);

                gameoverBounds = gameoverText.getLocalBounds();
                gameoverText.setOrigin(gameoverBounds.width / 2, gameoverBounds.height );
                gameoverText.setPosition(backgroundPos - WORLD_SIZE.x / 2, WORLD_SIZE.y / 2 - backgroundYMovement);
                window.draw(gameoverText);

                Text scores = new Text("Scores", ContentManager.instance.getFont("Ubuntu"),120);
                FloatRect scoresRect = scores.getLocalBounds();
                scores.setOrigin(scoresRect.width/2,scoresRect.height);
                scores.setPosition(backgroundPos - WORLD_SIZE.x*1.5f, WORLD_SIZE.y / 2 - backgroundYMovement);
                window.draw(scores);
                break;
        }
    }

    @Override
    public void dispose() {

    }
}
