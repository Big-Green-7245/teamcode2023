package org.firstinspires.ftc.teamcode.modules.output;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.modules.Modulable;
import org.firstinspires.ftc.teamcode.modules.Tickable;
import org.firstinspires.ftc.teamcode.util.FinishCondition;

public class LinearSlide implements Modulable, Tickable, FinishCondition {
    private final String name;
    private final double power;
    private DcMotorEx elevator;
    private TouchSensor elevatorBtn;
    private boolean retracting;

    public LinearSlide(String name, double power) {
        this.name = name;
        this.power = power;
    }

    public boolean isElevatorBtnPressed() {
        return elevatorBtn.isPressed();
    }

    @Override
    public void init(HardwareMap map) {
        elevator = (DcMotorEx) map.get(DcMotor.class, name);
        elevatorBtn = map.get(RevTouchSensor.class, name + "Btn");
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
        elevator.setPower(power);
    }

    /**
     * Starts to move the intakeSlide to the ground position.
     * ONLY call this function once for every move to ground!
     * YOU MUST call {@link #tick()} in a loop to stop the intakeSlide when it reaches the ground.
     */
    public void startRetraction() {
        retracting = true;
        startMoveToPos(-100000);
    }

    /**
     * Checks if the slider is retracting and pressing the button. If it is, reset the encoder.
     */
    @Override
    public void tick() {
        if (retracting && elevatorBtn.isPressed()) {
            elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            elevator.setTargetPosition(2);
            elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            retracting = false;
        }
    }

    /**
     * @return true if the elevator is at the target position
     * @implNote manually check the elevator position and the button because {@link DcMotor#isBusy()} has a lot of delay.
     */
    @Override
    public boolean isFinished() {
        return Math.abs(elevator.getTargetPosition() - elevator.getCurrentPosition()) <= 20;
    }

    public double getCurrent() {
        return elevator.getCurrent(CurrentUnit.AMPS);
    }

    public double getPower() {
        return elevator.getPower();
    }

    public int getTargetPosition() {
        return elevator.getTargetPosition();
    }

    /**
     * Encoders may not work when the wire is not compatible with the motor.
     *
     * @return the current reading of the encoder for this motor
     * @see DcMotor#getCurrentPosition()
     */
    public int getCurrentPosition() {
        return elevator.getCurrentPosition();
    }
}
