package com.teamtwo.aerolites.Utilities;

import com.teamtwo.aerolites.Aerolites;
import com.teamtwo.engine.Engine;
import com.teamtwo.engine.Launcher.LauncherConfig;
import org.jsfml.window.Window;

public class Launcher {

    //TODO End screen look pretty
    public static void main(String[] args) {
        LauncherConfig config = new LauncherConfig();

        config.width = 1280;
        config.height = 720;
        config.style |= Window.RESIZE;
        config.title = "Aerolites";
        config.fpsLimit = 60;

        new Engine(new Aerolites(), config);
    }
}
