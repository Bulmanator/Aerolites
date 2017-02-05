package com.teamtwo.engine.Messages;

public interface Message {

    enum Type {
        Collision,
        Damage,
        Something
    }

    Type getType();
}
