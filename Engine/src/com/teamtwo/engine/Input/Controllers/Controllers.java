package com.teamtwo.engine.Input.Controllers;

import com.teamtwo.engine.Utilities.Debug.Debug;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Joystick;

import static com.teamtwo.engine.Input.Controllers.Controller.Type;

public final class Controllers {

    private Type[] controllers;

    public static final Controllers instance;
    static { instance = new Controllers(); }

    private Controllers() {
        controllers = new Type[Joystick.JOYSTICK_COUNT];
        for(int i = 0; i < Joystick.JOYSTICK_COUNT; i++) {
            if(Joystick.isConnected(i)) {
                Joystick.Identification id = Joystick.getIdentification(i);

                Debug.log(Debug.LogLevel.DEBUG,
                        String.format("[Controller: Name: %s, Vendor ID: 0x%04x, Product ID: 0x%04x] Connected!",
                                id.name, id.vendorId, id.productId));

                controllers[i] = getController(id.vendorId, id.productId);
            }
        }
    }

    public void update() {
        for(int i = 0; i < Joystick.JOYSTICK_COUNT; i++) {
            if(Joystick.isConnected(i)) {
                if(controllers[i] == null) {
                    Joystick.Identification id = Joystick.getIdentification(i);
                    controllers[i] = getController(id.vendorId, id.productId);

                    Debug.log(Debug.LogLevel.DEBUG,
                            String.format("[Controller: Type: %s, Vendor ID: 0x%04x, Product ID: 0x%04x] Connected!",
                                    controllers[i].toString(), id.vendorId, id.productId));
                }
            }
            else {
                if(controllers[i] != null) {
                    Debug.log(Debug.LogLevel.DEBUG,
                            String.format("[Controller: Type: %s, Vendor ID: 0x%04x, Product ID: 0x%04x] Disconnected!",
                                    controllers[i].toString(), controllers[i].vendor, controllers[i].product));
                    controllers[i] = null;
                }
            }
        }
    }

    private Type getController(int vendor, int product) {
        switch (vendor | product) {
            case 0x076C:
                return Type.PS3;
            case 0x0DCC:
                return Type.PS4;
            case 0x06DE:
                return Type.Xbox_360;
            case 0x06FE:
                return Type.Xbox_One;
            case 0x06FF:
                return Type.Xbox_Elite;
            default:
                return Type.Unknown;
        }
    }

    public static boolean isButtonPressed(Controller.Player player, Controller.Button button) {
        Type type = instance.controllers[player.ordinal()];
        if(type == null) {
          //  Debug.log(Debug.LogLevel.WARNING, "Player " + player + " is not connected!");
            return false;
        }

        int value = button.toValue(type);

        if(value < 0) {
            return false;
        }

        if(value >= 97) {
            switch (value) {
                case 97:
                    return Joystick.getAxisPosition(player.ordinal(), Joystick.Axis.POV_Y) < 0;
                case 98:
                    return Joystick.getAxisPosition(player.ordinal(), Joystick.Axis.POV_Y) > 0;
                case 99:
                    return Joystick.getAxisPosition(player.ordinal(), Joystick.Axis.POV_X) < 0;
                case 100:
                    return Joystick.getAxisPosition(player.ordinal(), Joystick.Axis.POV_X) > 0;
            }
        }

        return Joystick.isButtonPressed(player.ordinal(), value);
    }

    public static float getTriggerValue(Controller.Player player, Controller.Trigger trigger) {
        Type type = instance.controllers[player.ordinal()];

        if(type == null) {
            return 100;
        }
        else if(type == Type.PS3) {
            if(isButtonPressed(player, trigger.toButton())) {
                return -100;
            }
            return 100;
        }

        Joystick.Axis axis = trigger.toAxis(type);
        if(axis == null) {
            throw new NullPointerException("Error: Axis was null");
        }

        return Joystick.getAxisPosition(player.ordinal(), axis);
    }

    public static Vector2f getThumbstickValues(Controller.Player player, Controller.Thumbstick thumbstick) {
        Type type = instance.controllers[player.ordinal()];
        if(type == null || type == Type.Unknown) { return Vector2f.ZERO; }

        float x, y;
        
        if(type == Type.PS3 || type == Type.PS4) {
            x = Joystick.getAxisPosition(player.ordinal(), thumbstick.ps[1]);
            y = Joystick.getAxisPosition(player.ordinal(), thumbstick.ps[0]);
        }
        else {
            x = Joystick.getAxisPosition(player.ordinal(), thumbstick.xbox[1]);
            y = Joystick.getAxisPosition(player.ordinal(), thumbstick.xbox[0]);
        }

        return new Vector2f(x, y);
    }
}
