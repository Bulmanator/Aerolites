package com.teamtwo.aerolites;

import com.teamtwo.engine.Engine;
import com.teamtwo.engine.Launcher.LauncherConfig;

public class Launcher {

    public static void main(String[] args) {
        LauncherConfig config = new LauncherConfig();

        config.width = 600;
        config.height = 600;
        config.title = "ExampleGame";

        new Engine(new ExampleGame(), config);
    }
}
