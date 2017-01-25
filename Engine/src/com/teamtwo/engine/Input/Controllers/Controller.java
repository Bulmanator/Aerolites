package com.teamtwo.engine.Input.Controllers;

import org.jsfml.window.Joystick;

public class Controller {

    public enum Player {
        One,
        Two,
        Three,
        Four
    }

    public enum Type {
        PS3(0x054c, 0x0268, 19),
        PS4(0x054c, 0x09cc, 14),
        Xbox_360(0x045e, 0x028e, 11),
        Xbox_One(0x045e, 0x02ee, 11),
        Xbox_Elite(0x045e, 0x02e3, 15),
        Unknown(-1, -1, 0);

        final int vendor;
        final int product;
        final int buttonCount;

        Type(int vendor, int product, int buttonCount) {
            this.vendor = vendor;
            this.product = product;
            this.buttonCount = buttonCount;
        }
    }

    public enum Button {
        A(14, 1, 0),
        B(13, 2, 1),
        X(15, 0, 2),
        Y(12, 3, 3),

        L3(1, 10, 9),
        LB(10, 4, 4),
        LT(8, 6, -1),

        R3(2, 11, 10),
        RB(11, 5, 5),
        RT(9, 7, -1),

        DPad_Up(4, -1, -1),
        DPad_Down(6, -1, -1),
        DPad_Left(7, -1, -1),
        DPad_Right(5, -1, -1),

        Start(3, 9, 7),
        Back(0, 8, 6),
        Xbox(16, 12, 8);

        final int ps3;
        final int ps4;
        final int xbox;

        Button(int ps3, int ps4, int xbox) {
            this.ps3 = ps3;
            this.ps4 = ps4;
            this.xbox = xbox;
        }
    }

    public enum DPad {
        Up(Joystick.Axis.POV_Y, Joystick.Axis.POV_Y),
        Down(Joystick.Axis.POV_Y, Joystick.Axis.POV_Y),
        Left(Joystick.Axis.POV_X, Joystick.Axis.POV_X),
        Right(Joystick.Axis.POV_X, Joystick.Axis.POV_X);

        final Joystick.Axis ps4;
        final Joystick.Axis xbox;

        DPad(Joystick.Axis ps4, Joystick.Axis xbox) {
            this.ps4 = ps4;
            this.xbox = xbox;
        }
    }

    public enum Trigger {
        RT(Joystick.Axis.U, Joystick.Axis.Z),
        LT(Joystick.Axis.V, Joystick.Axis.R);

        final Joystick.Axis ps4;
        final Joystick.Axis xbox;

        Trigger(Joystick.Axis ps4, Joystick.Axis xbox) {
            this.ps4 = ps4;
            this.xbox = xbox;
        }
    }
}
