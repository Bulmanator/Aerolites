package com.teamtwo.aerolites.States;

import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

/**
 * Created by janneht on 18/01/2017.
 */
public class MenuState extends State {
    RectangleShape NewGameBox = new RectangleShape();
    RectangleShape ContinueGameBox = new RectangleShape();
    RectangleShape OptionsBox = new RectangleShape();
    RectangleShape ReserveBox = new RectangleShape();

    int rotation = 0;
    int xSize = 2;
    int ySize = 10;
    int MinusXWindowSize = 250;
    int MinusYWindowSize = 100;
    private Font font;
    Text MenuText;

    @Override
    public void update(float dt) {
        /*rotation +=1;
        NewGameBox.setRotation(rotation);*/

    }

    @Override
    public void render() {
        window.draw(NewGameBox);
        window.draw(MenuText);
        window.draw(ContinueGameBox);
        window.draw(OptionsBox);
        window.draw(ReserveBox);

    }

    @Override
    public void dispose() {

    }

    public MenuState(GameStateManager gsm) {
        super(gsm);
        NewGameBox.setPosition(window.getSize().x/2-MinusXWindowSize,window.getSize().y/10+MinusYWindowSize);
        NewGameBox.setSize(new Vector2f(window.getSize().x/xSize, window.getSize().y/ySize));

        ContinueGameBox.setPosition(window.getSize().x/2-MinusXWindowSize,window.getSize().y/10+MinusYWindowSize*2);
        ContinueGameBox.setSize(new Vector2f(window.getSize().x/xSize, window.getSize().y/ySize));

        OptionsBox.setPosition(window.getSize().x/2-MinusXWindowSize,window.getSize().y/10+MinusYWindowSize*3);
        OptionsBox.setSize(new Vector2f(window.getSize().x/xSize, window.getSize().y/ySize));

        ReserveBox.setPosition(window.getSize().x/2-MinusXWindowSize,window.getSize().y/10+MinusYWindowSize*4);
        ReserveBox.setSize(new Vector2f(window.getSize().x/xSize, window.getSize().y/ySize));

        NewGameBox.setFillColor(new Color(100,100,100));
        ContinueGameBox.setFillColor(new Color(100,100,100));
        OptionsBox.setFillColor(new Color(100,100,100));
        ReserveBox.setFillColor(new Color(100,100,100));

        ContentManager.instance.loadFont("Ubuntu", "Ubuntu.ttf");
        // Using get instead of loading directly
        font = ContentManager.instance.getFont("Ubuntu");

        MenuText = new Text("Menu", font, 100);

        MenuText.setPosition(window.getSize().x/2-75  ,0-25);

    }
}
