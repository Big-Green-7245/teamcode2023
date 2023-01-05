package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw implements Modulable {
    private static final double CLOSED_POS = 0.5;
    private static final double OPENED_POS = 0.7;
    private Servo claw;
    private boolean open;

    @Override
    public void init(HardwareMap map) {
        claw = map.get(Servo.class, "claw"); // Control Hub 5
    }

    /**
     * Makes the claw start to move towards the specified position.
     *
     * @param state open or close the claw
     */
    public void setClawOpen(boolean state) {
        open = state;
        startMoveClaw();
    }

    /**
     * Start to move the claw opposite to the current state.
     */
    public void toggleClaw() {
        open = !open;
        startMoveClaw();
    }

    private void startMoveClaw() {
        claw.setPosition(open ? OPENED_POS : CLOSED_POS);
    }
}

