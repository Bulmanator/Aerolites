package com.teamtwo.aerolites.UI;

import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.Interfaces.EntityRenderable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;


/**
 * @author Tijan Weir
 */
public class Button implements EntityRenderable {

    RectangleShape shape;
    Text text;
    int centerX, centerY;
    //RectangleShape testShape;

    @Override
    public void render(RenderWindow renderer) {
        renderer.draw(shape);
        renderer.draw(text);
        //renderer.draw(testShape);
    }

    public Button(int centerX, int centerY, int width, int height, String label) {
        this.centerX = centerX;
        this.centerY = centerY;

        shape = new RectangleShape();
        //shape.setOrigin(centerX, centerY);
        shape.setPosition(centerX - width / 2, centerY - height / 2);
        shape.setSize(new Vector2f(width, height));
        text = new Text(label, ContentManager.instance.getFont("Ubuntu"));
        text.setColor(Color.BLACK);
        text.setPosition(centerX - text.getLocalBounds().width / 2, centerY - text.getLocalBounds().height); //TODO center text correctly

       /*testShape = new RectangleShape(new Vector2f(text.getLocalBounds().width, text.getLocalBounds().height));
       testShape.setPosition(text.getPosition());
       testShape.setFillColor(Color.RED);*/
    }

    public int getCenterX(){
        return centerX;
    }

    public int getCenterY(){
        return centerY;
    }

    public boolean checkInBox(Vector2f mouse){
        if(mouse.x > centerX - shape.getSize().x/2 && mouse.x < centerX + shape.getSize().x/2)
        {
            if(mouse.y > centerY - shape.getSize().y/2 && mouse.y < centerY + shape.getSize().y/2)
            {
                shape.setFillColor(Color.BLUE);
                return true;
            }
        }
        shape.setFillColor(Color.WHITE);
        return false;
    }

    public String getLabel() { return text.getString(); }

}
