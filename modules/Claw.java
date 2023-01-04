package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Claw implements Modulable {
    private static final double CLOSED_POS = 0.5;
    private static final double OPENED_POS = 0.7;

    private final ElapsedTime runtime = new ElapsedTime();
    public HardwareMap hwMap;
    public Servo claw;
    private boolean open;

    @Override
    public void init(HardwareMap map) {
        hwMap = map;

        claw = map.get(Servo.class, "claw"); // Control Hub 5

        // Additional Initializations
    }

    public void clawOpen(boolean state) {
        open = state;
        startMoveClaw();
    }

    public void toggleClaw() {
        open = !open;
        startMoveClaw();
    }

    private void startMoveClaw() {
        claw.setPosition(open ? OPENED_POS : CLOSED_POS);
    }

    // Additional methods for functionality
}

