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
    private boolean originalPress;

    private float playerInfoSize;


    public GameOver(GameStateManager gsm, PlayState background, int playerCount) {
        super(gsm);
        this.background = background;
        this.playerCount = playerCount;
        if(playerCount<0){
            this.playerCount = -(playerCount) - 1;
        }
        current = Stage.Gameover;
        backgroundPos = 0;
        backgroundYMovement = 0;
        originalPress = false;
        playerInfoSize = 0;
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

                if(backgroundYMovement == 400){
                    if(playerInfoSize < 700)
                        playerInfoSize += 5000*dt;
                    else if(playerInfoSize > 700){
                        playerInfoSize = 700;
                    }
                }
        }
        switch(playerCount){
            case 4:
                if(Controllers.isButtonPressed(Controller.Player.Four, Controller.Button.A) && originalPress) {
                    if (originalPress && current == Stage.Gameover) {
                        current = Stage.Scores;
                    }
                }
                else
                    originalPress = true;

            case 3:
                if(current == Stage.Gameover && Controllers.isButtonPressed(Controller.Player.Three, Controller.Button.A) && originalPress) {
                    if (originalPress && current == Stage.Gameover) {
                        current = Stage.Scores;
                    }
                }
                else
                    originalPress = true;
            case 2:
                if(current == Stage.Gameover && Controllers.isButtonPressed(Controller.Player.Two, Controller.Button.A) && originalPress) {
                    if (originalPress && current == Stage.Gameover) {
                        current = Stage.Scores;
                    }
                }
                else
                     originalPress = true;
            case 1:
                if(current == Stage.Gameover && Controllers.isButtonPressed(Controller.Player.One, Controller.Button.A) && originalPress) {
                    if(originalPress && current == Stage.Gameover) {
                        current = Stage.Scores;
                    }
                }
                else
                    originalPress = true;
            case 0:
                if(current == Stage.Gameover && Keyboard.isKeyPressed(Keyboard.Key.SPACE)) {
                    if(originalPress && current == Stage.Gameover) {
                        current = Stage.Scores;
                    }
                }
                else
                    originalPress = true;
        }
    }

    @Override
    public void render() {
        background.render();
        switch(current) {
            case Gameover:
                Text gameoverText = new Text("Game Over", ContentManager.instance.getFont("Ubuntu"), 120);

                RectangleShape box = new RectangleShape();
                box.setPosition(0,WORLD_SIZE.y/2-90);
                box.setSize(new Vector2f(backgroundPos,180f));
                box.setFillColor(new Color(255,0,0, 200));
                window.draw(box);

                box = new RectangleShape();
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

                box = new RectangleShape();
                box.setPosition(0,WORLD_SIZE.y/2-90 - backgroundYMovement);
                box.setSize(new Vector2f(backgroundPos,180f));
                box.setFillColor(new Color(255,0,0, 200));
                window.draw(box);

                box = new RectangleShape();
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

                Text scores = new Text("Scoreboard", ContentManager.instance.getFont("Ubuntu"),120);
                FloatRect scoresRect = scores.getLocalBounds();
                scores.setOrigin(scoresRect.width/2,scoresRect.height);
                scores.setPosition(backgroundPos - WORLD_SIZE.x*1.5f, WORLD_SIZE.y / 2 - backgroundYMovement);
                window.draw(scores);

                float playerWidth = (1920-(90 + 40*(playerCount+1)))/(playerCount+1);
                for(int i = 0; i < playerCount+1; i++){
                    box = new RectangleShape();
                    box.setPosition(30+40*(i+1)+playerWidth*i,WORLD_SIZE.y / 2 - backgroundYMovement + 100);
                    box.setSize(new Vector2f(playerWidth,playerInfoSize));
                    box.setFillColor(new Color(90,0,0, 240));
                    window.draw(box);

                    if(playerInfoSize == 700) {
                        Text text = new Text("Player " + (i + 1), ContentManager.instance.getFont("Ubuntu"), 64);
                        FloatRect textRect = scores.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 110);
                        window.draw(text);

                        text = new Text("Score: ", ContentManager.instance.getFont("Ubuntu"), 32);
                        textRect = scores.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 190);
                        window.draw(text);

                        text = new Text("Asteroids Destroyed: ", ContentManager.instance.getFont("Ubuntu"), 32);
                        textRect = scores.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 230);
                        window.draw(text);

                        text = new Text("Enemies Killed: ", ContentManager.instance.getFont("Ubuntu"), 32);
                        textRect = scores.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 270);
                        window.draw(text);

                        text = new Text("Time Survived: ", ContentManager.instance.getFont("Ubuntu"), 32);
                        textRect = scores.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 310);
                        window.draw(text);

                        text = new Text("Bullets Fired: ", ContentManager.instance.getFont("Ubuntu"), 32);
                        textRect = scores.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 350);
                        window.draw(text);

                        text = new Text("Time Spent Boosting: ", ContentManager.instance.getFont("Ubuntu"), 32);
                        textRect = scores.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 390);
                        window.draw(text);
                    }
                }
                break;
        }
    }

    @Override
    public void dispose() {

    }
}
