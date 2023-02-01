package org.firstinspires.ftc.teamcode.modules.intake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.modules.Claw;

public class IntakeClaw extends Claw {
    public IntakeClaw() {
        super("intakeClaw"); // Control Hub 0
    }

    @Override
    public void init(HardwareMap map) {
        super.init(map);
        claw.setDirection(Servo.Direction.REVERSE);
    }
}

