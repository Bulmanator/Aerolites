package com.teamtwo.engine.Input.Controllers;

import static org.jsfml.window.Joystick.Axis;

public class Controller {

    /**
     * An enumeration which represents the players which can be connected
     */
    public enum Player {
        /** The first player in the game */
        One,
        /** The second player in the game */
        Two,
        /** The third player in the game */
        Three,
        /** The fourth player in the game */
        Four
    }

    /**
     * An enumeration which represents the controllers which are supported
     */
    public enum Type {
        /** A Playstation 3 controller */
        PS3(0x054c, 0x0268, 19),
        /** A Playstation 4 controller */
        PS4(0x054c, 0x09cc, 14),
        /** An Xbox 360 controller */
        Xbox_360(0x045e, 0x028e, 11),
        /** An Xbox one controller */
        Xbox_One(0x045e, 0x02ee, 11),
        /** An Xbox one elite controller */
        Xbox_Elite(0x045e, 0x02e3, 15),
        /** The type of an unknown controller */
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

    /**
     * An enumeration for the supported buttons (in Xbox variant)
     */
    public enum Button {
        /** The A or Cross button */
        A(14, 1, 0),
        /** The B or Circle button */
        B(13, 2, 1),
        /** The X or Square button */
        X(15, 0, 2),
        /** The Y or Triangle button */
        Y(12, 3, 3),

        /** The L3 button */
        L3(1, 10, 9),
        /** The LB or L1 button */
        LB(10, 4, 4),
        /** The LT or L2 button */
        LT(8, 6, -1),

        /** The R3 button */
        R3(2, 11, 10),
        /** The RB or R1 button */
        RB(11, 5, 5),
        /** The RT or R2 button */
        RT(9, 7, -1),

        /** Up on the DPad */
        DPad_Up(4, 97, 97),
        /** Down on the DPad */
        DPad_Down(6, 98, 98),
        /** Left on the DPad */
        DPad_Left(7, 99, 99),
        /** Right on the DPad */
        DPad_Right(5, 100, 100),

        /** The Start or Options button */
        Start(3, 9, 7),
        /** The Back/ Select or Share button */
        Back(0, 8, 6),
        /** The Xbox or Playstation Button */
        Xbox(16, 12, 8);

        final int ps3;
        final int ps4;
        final int xbox;

        Button(int ps3, int ps4, int xbox) {
            this.ps3 = ps3;
            this.ps4 = ps4;
            this.xbox = xbox;
        }

        int toValue(Type type) {
            switch (type) {
                case PS3:
                    return ps3;
                case PS4:
                    return ps4;
                case Xbox_360:
                case Xbox_One:
                case Xbox_Elite:
                    return xbox;
                default:
                    return -1;
            }
        }
    }

    /**
     * An enumeration which represents the back triggers of the controllers
     */
    public enum Trigger {
        /** The LT or L2 trigger */
        LT(Axis.U, Axis.R),
        /** The RT or R2 trigger */
        RT(Axis.V, Axis.Z);

        final Axis ps4;
        final Axis xbox;

        Trigger(Axis ps4, Axis xbox) {
            this.ps4 = ps4;
            this.xbox = xbox;
        }

        /**
         * Converts each trigger into their respective {@link Button}
         * @return The button which represents the trigger
         */
        public Button toButton() {
            if(this == LT) {
                return Button.LT;
            }

            return Button.RT;
        }

        Axis toAxis(Type type) {
            switch (type) {
                case PS4:
                    return ps4;
                case Xbox_360:
                case Xbox_One:
                case Xbox_Elite:
                    return xbox;
            }

            return null;
        }
    }

    /** An enumeration which represents the thumbsticks of the controllers */
    public enum Thumbstick {
        /** The left thumbstick*/
        Left(new Axis[] { Axis.Y, Axis.X }, new Axis[] { Axis.Y, Axis.X }),
        /** The right thumbstick */
        Right(new Axis[] { Axis.R, Axis.Z }, new Axis[] { Axis.V, Axis.U });

        final Axis[] ps;
        final Axis[] xbox;

        Thumbstick(Axis[] ps, Axis[] xbox) {
            this.ps = ps;
            this.xbox = xbox;
        }
    }
}
