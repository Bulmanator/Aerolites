package com.teamtwo.aerolites.UI;

import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.Interfaces.EntityRenderable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

/**
 * Created by janneht on 25/01/2017.
 */
public class Button implements EntityRenderable{

    RectangleShape shape;
    Text text;


    @Override
    public void render(RenderWindow renderer) {
        renderer.draw(shape);
        renderer.draw(text);
    }

   public Button(int centerX, int centerY, int width, int height, String label)
   {
       shape = new RectangleShape();
       //shape.setOrigin(centerX, centerY);
       shape.setPosition(centerX-width/2, centerY-height/2);
       shape.setSize(new Vector2f(width,height));
       text = new Text(label, ContentManager.instance.getFont("Ubuntu"), 30);
    //   text = new Text(label, )

       text.setColor(Color.BLACK);

       text.setPosition(centerX- text.getLocalBounds().width/2, centerY- text.getLocalBounds().height); //TODO center text correctly
    //   text.setPosition(centerX, centerY); //TODO center text correctly
   }
}
