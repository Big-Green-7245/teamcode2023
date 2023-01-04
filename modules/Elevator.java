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

    private boolean isGrounding;


    @Override
    public void init(HardwareMap hardwareMap) {
        hwMap = hardwareMap;
        isGrounding = false;
        elevator = (DcMotorEx) hardwareMap.get(DcMotor.class, "linearSlide"); // Expansion Hub 0
        elevatorBtn = hardwareMap.get(RevTouchSensor.class, "elevatorBtn"); // Control Hub 1
        elevator.setDirection(DcMotor.Direction.REVERSE);
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setTargetPosition(10);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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
            isGrounding = false;
            elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            elevator.setTargetPosition(Math.max(elevator.getTargetPosition(), 0));
//            elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }else if (isGrounding){
            move(-0.2);
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
    public void move(double power) {
        elevator.setMode((DcMotor.RunMode.RUN_USING_ENCODER));
        elevator.setPower(power);

    }

    public void moveToPos(int position) {
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setTargetPosition(position);
        elevator.setPower(POWER);
    }

    public void moveToGround() {

        isGrounding = true;
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
