package com.teamtwo.aerolites.Utilities;

/**
 * @author Matthew Threlfall
 */
public class LevelConfig {

    /**
     * An enumeration for the different types of levels
     */
    public enum Type {
        /** Survive for a given time */
        TimeSurvival,
        /** Clear all of the asteroids in the level */
        ArenaClear
    }

    /**
     * An enumeration for the difficulty of the level
     */
    public enum Difficulty {
        /** Easy difficulty */
        Easy,
        /** Medium difficulty */
        Medium,
        /** Hard difficulty */
        Hard
    }

    /** The type of level to create, default = {@link Type#TimeSurvival}*/
    public Type levelType;

    /** The difficulty of the level, default = {@link Difficulty#Medium} */
    public Difficulty difficulty;

    /** The base asteroid spawn rate, default = 1.1 */
    public float asteroidBaseRate;

    /** The base swarmer spawn rate, default = 6 */
    public float swarmerBaseRate;

    /** The base standard AI spawn rate, default = 8 */
    public float aiBaseRate;

    /** The time taken for the boss to spawn, in seconds, default = 120 */
    public float bossSpawnTime;

    /** The amount of entities which need to be destroyed before the boss is spawned, default = 0 */
    public float bossSpawnEntitiy;

    /** The number of hits the boss can take per player, default = 180 */
    public int bossBaseLives;

    /** The input types of each player, this also defines how many players,
     * default = 1 player, using {@link InputType#Keyboard} */
    public final InputType[] players;

    /**
     * Constructs a default level configuration
     */
    public LevelConfig() {

        levelType = Type.TimeSurvival;
        difficulty = Difficulty.Medium;

        asteroidBaseRate = 1.1f;

        swarmerBaseRate = 6f;

        aiBaseRate = 8f;

        bossSpawnTime = 120;

        bossSpawnEntitiy = 0;

        bossBaseLives = 180;

        players = new InputType[8];
        players[0] = InputType.Keyboard;
    }
}
