package com.teamtwo.aerolites;

import com.oracle.webservices.internal.api.EnvelopeStyle;
import com.teamtwo.engine.Engine;
import com.teamtwo.engine.Launcher.LauncherConfig;
import org.jsfml.window.Window;

public class   Launcher {

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
