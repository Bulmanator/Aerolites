package com.teamtwo.aerolites.Entities.AI;

import com.teamtwo.aerolites.Entities.Entity;
import com.teamtwo.engine.Messages.Message;

import java.util.ArrayList;

/**
 * A class to represent the base AI
 * @author Matthew Threlfall
 */
public abstract class AI extends Entity {

    /**
     * An {@link ArrayList} of entities which
     */
    protected ArrayList<Entity> entities;
    protected boolean shooting;

    /**
     *
     */
    public AI(){
        shooting = false;
    }

    /**
     * Whether or not the AI is currently shooting
     * @return True if the AI is shooting, otherwise false
     */
    public boolean isShooting() {
        return shooting;
    }

    /**
     * Change whether or not the AI is shooting
     * @param shooting True for the AI to shoot, otherwise false
     */
    protected void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public void setEntities(ArrayList<Entity> entities){
        this.entities = entities;
    }

    @Override
    public void receiveMessage(Message message) {}

    @Override
    public abstract Type getType();
}
