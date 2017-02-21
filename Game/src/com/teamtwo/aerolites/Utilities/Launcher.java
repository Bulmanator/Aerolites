package com.teamtwo.aerolites.Utilities;

import com.teamtwo.aerolites.Aerolites;
import com.teamtwo.engine.Engine;
import com.teamtwo.engine.Launcher.LauncherConfig;
import org.jsfml.window.WindowStyle;

public class Launcher {

    public static void main(String[] args) {
        LauncherConfig config = new LauncherConfig();

        config.width = 1920;
        config.height = 1080;
        config.style = WindowStyle.FULLSCREEN;
        config.title = "Aerolites";
        config.fpsLimit = 60;

        new Engine(new Aerolites(), config);
    }
}
