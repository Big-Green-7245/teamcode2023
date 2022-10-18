package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class LinearSlide implements Modulable {
    private ElapsedTime runtime = new ElapsedTime();

    public HardwareMap hwMap;
    private DcMotor linearSlide;

    @Override
    public void init(HardwareMap hardwareMap) {
        hwMap = hardwareMap;
        linearSlide = hardwareMap.get(DcMotor.class, "linearSlide");
        linearSlide.setDirection(DcMotor.Direction.REVERSE);
        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void move(int target, double power) {
        linearSlide.setTargetPosition(linearSlide.getCurrentPosition() + target);
        linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlide.setPower(power);
    }
}
