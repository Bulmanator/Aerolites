package com.teamtwo.aerolites.Utilities;

import com.teamtwo.aerolites.Entities.Player;
import com.teamtwo.engine.Messages.Message;

public class LevelOverMessage implements Message {

    private Player[] players;
    private boolean complete;

    public LevelOverMessage(Player[] players, boolean complete) {
        this.players = players;
        this.complete = complete;
    }

    public Player getPlayer(int index) {
        return players[index];
    }

    public int getPlayerCount() { return players.length; }

    public boolean isComplete() {
        return complete;
    }

    @Override
    public Type getType() {
        return Type.LevelOver;
    }
}
