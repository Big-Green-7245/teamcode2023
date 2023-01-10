package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Elevator implements Modulable, Tickable {
    private static final double POWER = 0.8;

    public HardwareMap hwMap;
    private DcMotorEx elevator;
    private TouchSensor elevatorBtn;

    @Override
    public void init(HardwareMap hardwareMap) {
        hwMap = hardwareMap;
        elevator = (DcMotorEx) hardwareMap.get(DcMotor.class, "linearSlide"); // Expansion Hub 0
        elevatorBtn = hardwareMap.get(RevTouchSensor.class, "elevatorBtn"); // Control Hub 1
        elevator.setDirection(DcMotor.Direction.REVERSE);
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveUsingEncoder(double power) {
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevator.setPower(power);
    }

    public void startMoveToPos(int position) {
        elevator.setTargetPosition(position);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setPower(POWER);
    }

    /**
     * Starts to move the elevator to the ground position.
     * ONLY call this function once for every move to ground!
     * YOU MUST call {@link #tick()} in a loop to stop the elevator when it reaches the ground.
     */
    public void startMoveToGround() {
        startMoveToPos(-100000);
    }

    /**
     * Checks if the slider is pressing the button. If it is, reset the encoder.
     */
    @Override
    public void tick() {
        if (elevatorBtn.isPressed()) {
            elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            elevator.setTargetPosition(10);
            elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public boolean isAtTargetPos() {
        return !elevator.isBusy();
    }

    public double getCurrent() {
        return elevator.getCurrent(CurrentUnit.AMPS);
    }

    public double getPower() {
        return elevator.getPower();
    }

    public double getCurrentTarget() {
        return elevator.getTargetPosition();
    }

    /**
     * Encoders may not work when the wire is not compatible with the motor.
     *
     * @return the current reading of the encoder for this motor
     * @see DcMotor#getCurrentPosition()
     */
    public double getEncPos() {
        return elevator.getCurrentPosition();
    }
}
