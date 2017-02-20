package com.teamtwo.engine.Messages.Types;

import com.teamtwo.engine.Messages.Message;

/**
 * Created by james on 01/02/17.
 */
public class DamageMessage implements Message {

    private int damage;

    public DamageMessage(int amount) {
        damage = amount;
    }

    public Type getType() {
        return Type.Damage;
    }

    public int getDamage() { return damage; }
}
