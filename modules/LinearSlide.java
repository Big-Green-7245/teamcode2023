package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class LinearSlide implements Modulable {
    private ElapsedTime runtime = new ElapsedTime();

    public HardwareMap hwMap;
    private DcMotorEx linearSlide;

    @Override
    public void init(HardwareMap hardwareMap) {
        hwMap = hardwareMap;
        linearSlide = (DcMotorEx) hardwareMap.get(DcMotor.class, "linearSlide");
        linearSlide.setDirection(DcMotor.Direction.REVERSE);
        linearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void move(double power) {
        linearSlide.setPower(Math.abs(power) > 0.1 ? power : 0.1);
    }

    public double getCurrent() {
        return linearSlide.getCurrent(CurrentUnit.AMPS);
    }
}
