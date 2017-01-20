package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.ExampleInput;
import com.teamtwo.engine.Graphics.Animation;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import javafx.scene.image.ImageView;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;


public class MenuState extends State {
    RectangleShape NewGameBox = new RectangleShape();
    RectangleShape ContinueGameBox = new RectangleShape();
    RectangleShape OptionsBox = new RectangleShape();
    RectangleShape ReserveBox = new RectangleShape();
    RectangleShape rotateNewGameBox = new RectangleShape();
    RectangleShape rotateContinueGameBox = new RectangleShape();
    RectangleShape rotateOptionsBox = new RectangleShape();
    RectangleShape rotateReserveBox = new RectangleShape();

    Random randomColour = new Random();

    int rotation = 0;
    int xSize = 2;
    int ySize = 10;
    int MinusXWindowSize = 250;
    int MinusYWindowSize = 100;
    private Font font;
    Text MenuText;
    private Animation spaceAnimation;
    private ExampleInput hoverBoxChoices;
    Text text;


    public void render() {
        spaceAnimation.render(window);
        window.draw(NewGameBox);
        window.draw(MenuText);
        window.draw(ContinueGameBox);
        window.draw(OptionsBox);
        window.draw(ReserveBox);

        window.draw(rotateNewGameBox);
        window.draw(rotateContinueGameBox);
        window.draw(rotateOptionsBox);
        window.draw(rotateReserveBox);

     //   text = new Text("Mouse Position: [" + hoverBoxChoices.position.x + ":" + hoverBoxChoices.position.y + "]", font, 15);
    //    text.setPosition(330, (window.getSize().y / 2) - 30);
     //   window.draw(text);
    }

    @Override
    public void dispose() {

    }

    public MenuState(GameStateManager gsm) {
        super(gsm);

        hoverBoxChoices = new ExampleInput();
        game.getEngine().setInputHandler(hoverBoxChoices);

        NewGameBox.setPosition(150, window.getSize().y / 10 + MinusYWindowSize);
        NewGameBox.setSize(new Vector2f(window.getSize().x / xSize, window.getSize().y / ySize));

        rotateNewGameBox.setPosition(420, window.getSize().y / 10 + MinusYWindowSize + 30);
        rotateNewGameBox.setSize(new Vector2f(20, 20));

        ContinueGameBox.setPosition(150, window.getSize().y / 10 + MinusYWindowSize * 2);
        ContinueGameBox.setSize(new Vector2f(window.getSize().x / xSize, window.getSize().y / ySize));

        rotateContinueGameBox.setPosition(420, window.getSize().y / 10 + MinusYWindowSize * 2 + 30);
        rotateContinueGameBox.setSize(new Vector2f(20,20));

        OptionsBox.setPosition(150, window.getSize().y / 10 + MinusYWindowSize * 3);
        OptionsBox.setSize(new Vector2f(window.getSize().x / xSize, window.getSize().y / ySize));

        rotateOptionsBox.setPosition(420, window.getSize().y / 10 + MinusYWindowSize * 3 + 30);
        rotateOptionsBox.setSize(new Vector2f(20,20));

        ReserveBox.setPosition(150, window.getSize().y / 10 + MinusYWindowSize * 4);
        ReserveBox.setSize(new Vector2f(window.getSize().x / xSize, window.getSize().y / ySize));

        rotateReserveBox.setPosition(420, window.getSize().y / 10 + MinusYWindowSize * 4 + 30);
        rotateReserveBox.setSize(new Vector2f(20,20));


        NewGameBox.setFillColor(new Color(100, 100, 100));
        ContinueGameBox.setFillColor(new Color(100, 100, 100));
        OptionsBox.setFillColor(new Color(100, 100, 100));
        ReserveBox.setFillColor(new Color(100, 100, 100));

        rotateNewGameBox.setFillColor(new Color(0, 0, 0));
        rotateContinueGameBox.setFillColor(new Color(0,0,0));
        rotateOptionsBox.setFillColor(new Color(0,0,0));
        rotateReserveBox.setFillColor(new Color(0,0,0));


        ContentManager.instance.loadFont("Blazed", "Blazed.ttf");    // Using get instead of loading directly
        font = ContentManager.instance.getFont("Blazed");

        Texture image = ContentManager.instance.loadTexture("Space", "Space.png");

        spaceAnimation = new Animation(image, 1,2,0.1f);
        spaceAnimation.setPosition(300,300);

        MenuText = new Text("Menu", font, 100);
        MenuText.setPosition(100, 0 - 25);
    }

    public void update(float dt) {

        if(149.0 < hoverBoxChoices.position.x & hoverBoxChoices.position.x < 448.0 & hoverBoxChoices.position.y > 162 & hoverBoxChoices.position.y < 218) {
            rotation += 1;
            rotateNewGameBox.setRotation(rotation);
        }

        if(149.0 < hoverBoxChoices.position.x & hoverBoxChoices.position.x < 448.0 & hoverBoxChoices.position.y > 253 & hoverBoxChoices.position.y < 319) {
            rotation += 1;
            rotateContinueGameBox.setRotation(rotation);
        }

        if(149.0 < hoverBoxChoices.position.x & hoverBoxChoices.position.x < 448.0 & hoverBoxChoices.position.y > 357 & hoverBoxChoices.position.y < 418) {
            rotation += 1;
            rotateOptionsBox.setRotation(rotation);
        }

        if(149.0 < hoverBoxChoices.position.x & hoverBoxChoices.position.x < 448.0 & hoverBoxChoices.position.y > 459 & hoverBoxChoices.position.y < 519) {
            rotation += 1;
            rotateReserveBox.setRotation(rotation);
        }


        MenuText.setColor(new Color(randomColour.nextInt(255) + 0, randomColour.nextInt(255) + 0, randomColour.nextInt(255) + 0));
        spaceAnimation.update(dt);

    }
    
}
