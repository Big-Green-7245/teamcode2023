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
        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void move(double power) {
        linearSlide.setTargetPosition(linearSlide.getCurrentPosition() + (int) (power * 100));
        linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlide.setPower(power);
    }

    public void altMove(double power) {
        if((int)(power) == 0) {
            linearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            linearSlide.setPower(0.05);
        }
        else{
            linearSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            linearSlide.setPower(power);
        }
    }

    public double getCurrent() {
        return linearSlide.getCurrent(CurrentUnit.AMPS);
    }
}
