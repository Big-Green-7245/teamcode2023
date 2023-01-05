package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Elevator implements Modulable, Tickable {
    private static final double POWER = 0.7;

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
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setTargetPosition(position);
        elevator.setPower(POWER);
    }

    /**
     * Starts to move the elevator to the ground position.
     * Remember to call {@link #tick()} to stop the elevator when it reaches the ground.
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
            elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            elevator.setTargetPosition(Math.max(elevator.getTargetPosition(), 0));
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

    /**
     * Only works when {@link #elevator} is in {@link DcMotor.RunMode#RUN_USING_ENCODER}!.
     * @return the current reading of the encoder for this motor
     * @see DcMotor#getCurrentPosition()
     */
    public double getEncPos() {
        return elevator.getCurrentPosition();
    }
}
