package com.teamtwo.aerolites.Utilities;

import com.teamtwo.aerolites.Aerolites;
import com.teamtwo.engine.Engine;
import com.teamtwo.engine.Launcher.LauncherConfig;

public class Launcher {

    //TODO End screen look pretty
    public static void main(String[] args) {
        LauncherConfig config = new LauncherConfig();

        config.width = 1920;
        config.height = 1080;
        config.title = "Aerolites";
        config.fpsLimit = 60;

        new Engine(new Aerolites(), config);
    }
}
