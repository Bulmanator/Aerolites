package com.teamtwo.aerolites;

import com.teamtwo.engine.Engine;
import com.teamtwo.engine.Launcher.Configuration;

public class Launcher {

    public static void main(String[] args) {
        Configuration config = new Configuration();

        config.width = 1280;
        config.height = 720;
        config.title = "Aerolites";
        config.fpsLimit = 168;

        new Engine(new Aerolites(), config);
    }
}
