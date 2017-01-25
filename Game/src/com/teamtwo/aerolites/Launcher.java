package com.teamtwo.aerolites;

import com.teamtwo.engine.Engine;
import com.teamtwo.engine.Launcher.LauncherConfig;

public class Launcher {

    public static void main(String[] args) {
        LauncherConfig config = new LauncherConfig();

        config.width = 1280;
        config.height = 720;
        config.title = "ExampleGame";

        new Engine(new ExampleGame(), config);
    }

    //Multiples of 16 * 9s

}
