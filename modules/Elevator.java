package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Elevator implements Modulable, Tickable {
    private static final double POWER = 0.7;
    private final ElapsedTime runtime = new ElapsedTime();

    public HardwareMap hwMap;
    public DcMotorEx elevator;

    public TouchSensor elevatorBtn;


    @Override
    public void init(HardwareMap hardwareMap) {
        hwMap = hardwareMap;
        elevator = (DcMotorEx) hardwareMap.get(DcMotor.class, "linearSlide");
        elevatorBtn = hardwareMap.get(RevTouchSensor.class, "elevatorBtn");
        elevator.setDirection(DcMotor.Direction.REVERSE);
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Moves the elevator down if it is not in the lowest position.
     */
    @Override
    public void tickBeforeStart() {
        moveToGround();
    }

    public void move(double power) {
        elevator.setPower(Math.abs(power) > 0.1 ? power : 0);
    }

    public void moveToPos(int position) {
        elevator.setTargetPosition(position);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setPower(POWER);
    }

    public void moveToGround() {
        if (elevatorBtn.isPressed()) {
            elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        elevator.setTargetPosition(0);
    }

    public boolean isAtTargetPos() {
        return Math.abs(elevator.getTargetPosition() - elevator.getCurrentPosition()) < elevator.getTargetPositionTolerance();
    }

    public double getCurrent() {
        return elevator.getCurrent(CurrentUnit.AMPS);
    }

    public double getPower() {
        return elevator.getPower();
    }

    public double getEncPos() {
        return elevator.getCurrentPosition();
    }
}
