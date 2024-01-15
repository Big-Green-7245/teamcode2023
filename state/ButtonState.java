package org.firstinspires.ftc.teamcode.state;

import org.firstinspires.ftc.teamcode.util.ButtonHelper;

/**
 * A type of state which completes when a button is pressed.
 */
public class ButtonState extends State {
    public ButtonState(String name, ButtonHelper gamepad, int button) {
        super(name, () -> gamepad.held(button));
    }
}
