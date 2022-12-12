package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Claw implements Modulable {
    private static final double CLOSED_POS = 0.55;
    private static final double OPENED_POS = 0.7;

    private final ElapsedTime runtime = new ElapsedTime();
    public HardwareMap hwMap;
    private Servo claw;

    @Override
    public void init(HardwareMap map) {
        hwMap = map;

        claw = map.get(Servo.class, "claw");

        // Additional Initializations
    }

    public void clawOpen(boolean state) {
        claw.setPosition(state ? OPENED_POS : CLOSED_POS);
    }

    // Additional methods for functionality
}

