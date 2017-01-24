package com.teamtwo.engine.Input.Controllers;

import org.jsfml.window.Joystick;

public final class Controllers {

    /** A singleton instance of the Controllers class, used for querying */
    public static Controllers instance;
    static { instance = new Controllers(); }

    private Controller[] controllers;

    private Controllers() { controllers = new Controller[Joystick.JOYSTICK_COUNT]; }

    public void update() {
        for(int i = 0; i < Joystick.JOYSTICK_COUNT; i++) {
            if(Joystick.isConnected(i)) {

                if(controllers[i] == null) {
                    // Connect the controller
                    controllers[i] = new Controller(Joystick.getIdentification(i));
                }
            }
            else {
                // Disconnect the controller
                if(controllers[i] != null) controllers[i] = null;
            }
        }
    }
}
