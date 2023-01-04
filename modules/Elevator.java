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
        elevator = (DcMotorEx) hardwareMap.get(DcMotor.class, "linearSlide"); // Expansion Hub 0
        elevatorBtn = hardwareMap.get(RevTouchSensor.class, "elevatorBtn"); // Control Hub 1
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
        tick();
    }

    /**
     * Check if the slider is on the ground. If it is, reset the encoder.
     */
    @Override
    public void tick() {
        if (elevatorBtn.isPressed()) {
            elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            elevator.setTargetPosition(Math.max(elevator.getTargetPosition(), 0));
            elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

//    public void move(boolean direction, double power) {
//        if (power == 0) {
//            moveToPos(elevator.getCurrentPosition());
//            elevator.setPower(POWER);
//        } else {
//            moveToPos(elevator.getCurrentPosition() + (direction ? 100 : -100));
//            elevator.setPower(power);
//        }
//    }
    public void moveUsingEncoder(double power) {
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevator.setPower(power);
    }

    public void moveToPos(int position) {
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setTargetPosition(position);
        elevator.setPower(POWER);
    }

    public void moveToGround() {
        moveToPos(-100000);
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

    public double getEncPos() {
        return elevator.getCurrentPosition();
    }
}
