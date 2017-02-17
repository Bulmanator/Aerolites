package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Entities.Player;
import com.teamtwo.aerolites.Entities.ScoreObject;
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
public class LevelOver extends State {
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
    private String bannerMessage;


    public LevelOver(GameStateManager gsm, PlayState background, int playerCount, boolean win) {
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
        if(win)
            bannerMessage = "Level Cleared!";
        else
            bannerMessage = "GameOver";
    }

    @Override
    public void update(float dt) {
        background.update(dt);
        if(Keyboard.isKeyPressed(Keyboard.Key.ESCAPE))
            game.getEngine().close();
        switch (current){
            case Gameover:
                if(backgroundPos < 1920) {
                    backgroundPos += 4000*dt;
                }
                else if(backgroundPos > 1920) {
                    backgroundPos = 1920;
                }
                break;
            case Scores:
                if(backgroundPos < 1920*2) {
                    backgroundPos += 4000*dt;
                }
                else if(backgroundPos > 1920*2) {
                    backgroundPos = 1920*2;
                }
                if(backgroundPos == 1920*2){
                    if(backgroundYMovement<400)
                        backgroundYMovement += 4000*dt;
                    else if(backgroundYMovement > 400)
                        backgroundYMovement = 400;
                }

                if(backgroundYMovement == 400){
                    if(playerInfoSize < 700)
                        playerInfoSize += 3000*dt;
                    else if(playerInfoSize > 700){
                        playerInfoSize = 700;
                    }
                }
        }
        switch(playerCount){
            case 4:
                if(current == Stage.Gameover && Controllers.isButtonPressed(Controller.Player.Four, Controller.Button.A)) {
                    if (originalPress && current == Stage.Gameover) {
                        current = Stage.Scores;
                    }
                }
                else
                    originalPress = true;
            case 3:
                if(current == Stage.Gameover && Controllers.isButtonPressed(Controller.Player.Three, Controller.Button.A)) {
                    if (originalPress && current == Stage.Gameover) {
                        current = Stage.Scores;
                    }
                }
                else
                    originalPress = true;
            case 2:
                if(current == Stage.Gameover && Controllers.isButtonPressed(Controller.Player.Two, Controller.Button.A)) {
                    if (originalPress && current == Stage.Gameover) {
                        current = Stage.Scores;
                    }
                }
                else
                     originalPress = true;
            case 1:
            case 0:
                if(current == Stage.Gameover && Controllers.isButtonPressed(Controller.Player.One, Controller.Button.A)) {
                    if(originalPress && current == Stage.Gameover) {
                        current = Stage.Scores;
                    }
                }
                else if(current == Stage.Gameover && Keyboard.isKeyPressed(Keyboard.Key.SPACE)) {
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
                Text gameoverText = new Text(bannerMessage, ContentManager.instance.getFont("Ubuntu"), 120);

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
                gameoverText = new Text(bannerMessage, ContentManager.instance.getFont("Ubuntu"), 120);

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
                int font = 20;
                if(playerCount > 3)
                    font = 20;
                for(int i = 0; i < playerCount+1; i++){
                    box = new RectangleShape();
                    box.setPosition(30+40*(i+1)+playerWidth*i,WORLD_SIZE.y / 2 - backgroundYMovement + 100);
                    box.setSize(new Vector2f(playerWidth,playerInfoSize));
                    box.setFillColor(new Color(90,0,0, 240));
                    window.draw(box);
                    Player p = ((Player)background.getDeadPlayers().get(i));
                    ScoreObject s = ((Player)background.getDeadPlayers().get(i)).getScore();


                    if(playerInfoSize == 700) {
                        Text text = new Text("Player " + p.getPlayerNumber(), ContentManager.instance.getFont("Ubuntu"), 64);
                        FloatRect textRect = text.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 110);
                        window.draw(text);

                        text = new Text("Score: " + s.getScore() + " / " + s.getScoreTotal() +" total", ContentManager.instance.getFont("Ubuntu"), font);
                        textRect = text.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 190);
                        window.draw(text);

                        text = new Text("Asteroids Destroyed: " + s.getAsteroidsDestroyed() +" / " +s.getAsteroidsDestroyedTotal()+" total", ContentManager.instance.getFont("Ubuntu"), font);
                        textRect = text.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 230);
                        window.draw(text);

                        text = new Text("Enemies Killed: "+ s.getEnemiesDestoryed()+" / "+s.getEnemiesDestoryedTotal()+" total", ContentManager.instance.getFont("Ubuntu"), font);
                        textRect = text.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 270);
                        window.draw(text);

                        text = new Text("Time Survived: " + s.getTimeAlive()+"s / "+s.getTimeAliveTotal()+"s"+" total", ContentManager.instance.getFont("Ubuntu"), font);
                        textRect = text.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 310);
                        window.draw(text);

                        text = new Text("Bullets Fired: " + s.getBulletsFired()+" / "+s.getBulletsFiredTotal()+" total", ContentManager.instance.getFont("Ubuntu"), font);
                        textRect = text.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 350);
                        window.draw(text);

                        text = new Text("Bullets Missed: " + s.getBulletsMissed()+" / "+s.getBulletsMissedTotal()+" total", ContentManager.instance.getFont("Ubuntu"), font);
                        textRect = text.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 390);
                        window.draw(text);

                        text = new Text("Accuracy: " + s.getAccuracy()+"% / "+s.getTotalAccuracy()+"% total", ContentManager.instance.getFont("Ubuntu"), font);
                        textRect = text.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 430);
                        window.draw(text);

                        text = new Text("Time Spent Boosting: " +s.getTimeSpentBoosting()+"s / "+s.getTimeSpentBoostingTotal()+"s"+" total", ContentManager.instance.getFont("Ubuntu"), font);
                        textRect = text.getLocalBounds();
                        text.setOrigin(0, 0);
                        text.setPosition(30 + 40 * (i + 1) + playerWidth * i + 10, WORLD_SIZE.y / 2 - backgroundYMovement + 470);
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
