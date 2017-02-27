package com.teamtwo.aerolites.Utilities;

import com.teamtwo.aerolites.Entities.Player;
import com.teamtwo.engine.Messages.Message;

/**
 * Created by james on 25/02/17.
 */
public class LevelOverMessage implements Message {

    private Player[] players;
    private boolean complete;

    public LevelOverMessage(Player[] players, boolean complete) {
        this.players = players;
        this.complete = complete;
    }

    public Player getPlayers(int index) {
        return players[index];
    }

    public boolean isComplete() {
        return complete;
    }

    @Override
    public Type getType() {
        return Type.LevelOver;
    }
}
