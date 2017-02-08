package com.teamtwo.aerolites.Entities;

/**
 * Created by james on 07/02/17.
 */
public class CollisionMask {

    public static final int PLAYER = 0x01;
    public static final int BULLET = 0x02;
    public static final int ENEMY_BULLET = 0x04;
    public static final int ASTEROID = 0x08;
    public static final int STANDARD_AI = 0x10;
    public static final int SWARMER_BASE = 0x20;
    public static final int SWARMER = 0x40;
    public static final int AI = STANDARD_AI | SWARMER | SWARMER_BASE;
    public static final int ALL = PLAYER | ASTEROID | AI | BULLET | ENEMY_BULLET;
}