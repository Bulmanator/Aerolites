package com.teamtwo.aerolites;

import com.teamtwo.engine.Engine;
import com.teamtwo.engine.Launcher.LauncherConfig;

public class Launcher {

    public static void main(String[] args) {
        LauncherConfig config = new LauncherConfig();

        config.width = 1280;
        config.height = 720;
        config.title = "Aerolites";
        config.fpsLimit = 120;

        new Engine(new Aerolites(), config);
    }
}
