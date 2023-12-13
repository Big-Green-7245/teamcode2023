package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Arrays;

/**
 * ButtonHelper - Utility to organize gamepad state and 'pressing' events (change of state)
 */

public class ButtonHelper {
    private final Gamepad gamepad;

    private final boolean[] buttons = new boolean[15];
    private final boolean[] held = new boolean[15];

    /**
     * Buttons for use in the pressed(), released(), and pressing() methods
     */
    public static final int dpad_up            =  0,
                            dpad_down          =  1,
                            dpad_left          =  2,
                            dpad_right         =  3,
                            a                  =  4,
                            b                  =  5,
                            x                  =  6,
                            y                  =  7,
                            guide              =  8,
                            start              =  9,
                            back               = 10,
                            left_bumper        = 11,
                            right_bumper       = 12,
                            left_stick_button  = 13,
                            right_stick_button = 14;


    /**
     * Initialize gamepad for ButtonHelper
     * @param gamepad Gamepad object from TeleOp
     */
    public ButtonHelper(final Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    /**
     * Update the current status of the gamepad
     * Recommended update per loop for helper to function
     */
    public void update() {
        boolean[] buttons2 = Arrays.copyOf(buttons, 15);
        buttons[dpad_up] = gamepad.dpad_up;
        buttons[dpad_down] = gamepad.dpad_down;
        buttons[dpad_left] = gamepad.dpad_left;
        buttons[dpad_right] = gamepad.dpad_right;
        buttons[a] = gamepad.a;
        buttons[b] = gamepad.b;
        buttons[x] = gamepad.x;
        buttons[y] = gamepad.y;
        buttons[guide] = gamepad.guide;
        buttons[start] = gamepad.start;
        buttons[back] = gamepad.back;
        buttons[left_bumper] = gamepad.left_bumper;
        buttons[right_bumper] = gamepad.right_bumper;
        buttons[left_stick_button] = gamepad.left_stick_button;
        buttons[right_stick_button] = gamepad.right_stick_button;
        for (int i = 0; i < 15; i++) {
            held[i] = buttons[i] && buttons2[i];
        }
    }

    /**
     * Return whether button is pressed on gamepad
     * @param idx Gamepad button value
     */
    public boolean pressed(int idx) {
        return buttons[idx];
    }

    /**
     * Return whether button is released on gamepad
     * @param idx Gamepad button value
     */
    public boolean released(int idx) {
        return !pressed(idx);
    }

    /**
     * Return whether button is being pressed on
     * Determined through transition from unpressed to
     * pressed button
     * @param idx Gamepad button value
     */
    public boolean pressing(int idx) {
        return !held[idx] && pressed(idx);
    }
}
