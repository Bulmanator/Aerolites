package com.teamtwo.engine.Input.Controllers;

import com.teamtwo.engine.Utilities.Debug.Debug;
import org.jsfml.window.Joystick;

class Controller {

    /**
     * The Different types of supported controllers
     */
    public enum Type {
        /** A PS3 Controller (Sixaxis or Dualshock 3) */
        PS3,
        /** A PS4 Controller (Dualshock 4) */
        PS4,
        /** An Xbox 360 Controller (Wired) */
        Xbox360,
        /** An Xbox One Controller */
        XboxOne,
        /** An Xbox One Elite Controller */
        XboxElite,
        /** An Unknown Controller Type */
        Unknown
    }

    private Type type;
    private String name;
    private int vendorID;
    private int productID;

    Controller(Joystick.Identification id) {
        name = id.name;
        vendorID = id.vendorId;
        productID = id.productId;

        switch (vendorID | productID) {
            case 0x076c:
                type = Type.PS3;
                break;
            case 0x0dcc:
                type = Type.PS4;
                break;
            case 0x06de:
                type = Type.Xbox360;
                break;
            case 0x06df:
                type = Type.XboxOne;
                break;
            case 0x06ff:
                type = Type.XboxElite;
                break;
            default:
                Debug.log(Debug.LogLevel.WARNING, "Controller[ Vendor ID: " + vendorID + ", Product ID: " + productID + "] was unknown");
                type = Type.Unknown;
        }

        System.out.println(toString());
    }

    public String toString() {
        return String.format("Controller[ Name: %s, Type: %s, Vendor ID: 0x%04x, Product ID: 0x%04x ]",
                name, type.toString(), vendorID, productID);
    }
}
