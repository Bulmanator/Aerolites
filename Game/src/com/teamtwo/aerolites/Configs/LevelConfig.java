package com.teamtwo.aerolites.Configs;

/**
 * @author Matthew Threlfall
 */
public class LevelConfig {

    public enum levelTypes{
        timeSurvival,
        arenaClear
    }

    public float asteroidBaseRate;

    public float swarmerBaseRate;

    public float aiBaseRate;

    public float bossSpawnTime;

    public float bossSpawnEntitiy;

    public float bossLives;

    public int playerCount;


    public LevelConfig(){
        asteroidBaseRate = 1.1f;

        swarmerBaseRate = 6f;

        aiBaseRate = 8f;

        bossSpawnTime = 120;

        bossSpawnEntitiy = 0;

        bossLives = 180;

        playerCount = 0;
    }
}
