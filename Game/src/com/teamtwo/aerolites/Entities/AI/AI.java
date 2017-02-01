package com.teamtwo.aerolites.Entities.AI;

import com.teamtwo.aerolites.Entities.Entity;
import org.jsfml.graphics.RenderWindow;

import java.util.ArrayList;

/**
 * @Author Matthew Threlfall
 */
public class AI extends Entity{
    protected ArrayList<Entity> entities;
    protected boolean shooting;

    public AI(){
        shooting = false;
    }

    public boolean isShooting() {
        return shooting;
    }

    protected void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public void setEntities(ArrayList<Entity> entities){
        this.entities = entities;
    }

    @Override
    public void update(float dt) {
        checkOffScreen();
        limitSpeed();
    }

    @Override
    public void render(RenderWindow renderer) {
        super.render(renderer);
    }
}
