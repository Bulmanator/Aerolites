package com.teamtwo.aerolites;

import com.teamtwo.engine.Engine;
import com.teamtwo.engine.Launcher.Configuration;
import org.jsfml.window.WindowStyle;

public class Launcher {

    public static void main(String[] args) {
        Configuration config = new Configuration();

        config.width = 1280;
        config.height = 720;
        config.title = "ExampleGame";

        new Engine(new ExampleGame(), config);
    }
}
