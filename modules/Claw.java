package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Claw implements Modulable
{
    private ElapsedTime runtime = new ElapsedTime();

    private final double clawClosedPos = 0.55;
    private final double clawOpenedPos = 0.7;

    Servo claw;

    public HardwareMap hwMap;

    @Override
    public void init(HardwareMap map)
    {
        hwMap = map;

        claw = map.get(Servo.class, "claw");

        // Additional Initializations
    }

    public void clawOpen(boolean state) {
        claw.setPosition(state ? clawOpenedPos : clawClosedPos);
    }

    // Additional methods for functionality
}

