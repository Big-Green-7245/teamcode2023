package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Rotation implements Modulable {
    private ElapsedTime runtime = new ElapsedTime();

    public HardwareMap hwMap;
    private DcMotor rotation;

    @Override
    public void init(HardwareMap hardwareMap) {
        hwMap = hardwareMap;
        rotation = hardwareMap.get(DcMotor.class, "rotation");
    }

    public void move(double power) {
        rotation.setPower(power);
    }
}
