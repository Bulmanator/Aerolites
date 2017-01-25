package com.teamtwo.engine.Input.Controllers;

import com.teamtwo.engine.Utilities.Debug.Debug;
import org.jsfml.window.Joystick;

public final class Controllers {

    private Controller.Type[] controllers;

    public static final Controllers instance;
    static { instance = new Controllers(); }

    private Controllers() {
        controllers = new Controller.Type[Joystick.JOYSTICK_COUNT];
        for(int i = 0; i < Joystick.JOYSTICK_COUNT; i++) {
            if(Joystick.isConnected(i)) {
                Joystick.Identification id = Joystick.getIdentification(i);
                controllers[i] = getController(id.vendorId, id.productId);
            }
        }
    }

    public void update() {
        for(int i = 0; i < Joystick.JOYSTICK_COUNT; i++) {
            if(Joystick.isConnected(i)) {
                Joystick.Identification id = Joystick.getIdentification(i);
                controllers[i] = getController(id.vendorId, id.productId);
            }
            else {
                if(controllers[i] != null) controllers[i] = null;
            }
        }
    }

    private Controller.Type getController(int vendor, int product) {
        switch (vendor | product) {
            case 0x076C:
                return Controller.Type.PS3;
            case 0x0DCC:
                return Controller.Type.PS4;
            case 0x06DE:
                return Controller.Type.Xbox_360;
            case 0x06FE:
                return Controller.Type.Xbox_One;
            case 0x06FF:
                return Controller.Type.Xbox_Elite;
            default:
                return Controller.Type.Unknown;
        }
    }

    public static boolean isButtonPressed(Controller.Player player, Controller.Button button) {
        Controller.Type type = instance.controllers[player.ordinal()];
        if(type == null) {
            Debug.log(Debug.LogLevel.WARNING, "Player " + player + " is not connected!");
            return false;
        }

        int value = -1;
        switch (type) {
            case PS3:
                value = button.ps3;
                break;
            case PS4:
                value = button.ps4;
                break;
            case Xbox_360:
            case Xbox_One:
            case Xbox_Elite:
                value = button.xbox;
                break;
        }

        if(value < 0) {
            return false;
        }

        return Joystick.isButtonPressed(player.ordinal(), value);
    }
}
