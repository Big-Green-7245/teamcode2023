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
    private DcMotorEx elevatorLeft;
    private DcMotorEx elevatorRight;
    private TouchSensor elevatorBtnLeft;
    private TouchSensor elevatorBtnRight;
    private boolean isBusy;

    public LinearSlide(String name, double power) {
        this.name = name;
        this.power = power;
    }

    public boolean[] isElevatorBtnPressed() {
        return new boolean[]{elevatorBtnLeft.isPressed(), elevatorBtnRight.isPressed()};
    }

    @Override
    public void init(HardwareMap map) {
        elevatorBtnLeft = map.get(RevTouchSensor.class, "leftBtn");
        elevatorBtnRight = map.get(RevTouchSensor.class, "rightBtn");
        elevatorLeft = (DcMotorEx) map.get(DcMotor.class, name + "Left");
        elevatorLeft.setDirection(DcMotor.Direction.FORWARD);
        elevatorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elevatorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevatorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        elevatorRight = (DcMotorEx) map.get(DcMotor.class, name + "Right");
        elevatorRight.setDirection(DcMotor.Direction.REVERSE);
        elevatorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elevatorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevatorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    /**
     * Warning: DO NOT use this if motor is currently running to position. Undefined behavior.
     */
    public void moveUsingEncoder(double power) {
        elevatorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevatorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevatorLeft.setPower(power);
        elevatorRight.setPower(power);
    }

    public void startMoveToRelativePos(int relativePosition) {
        startMoveToPos(Math.max((elevatorLeft.getCurrentPosition() + elevatorRight.getCurrentPosition()) / 2 + relativePosition, 10));
    }

    public void startMoveToPosSetBusy(int position) {
        startMoveToPos(position);
        isBusy = true;
    }

    public void startMoveToPos(int position) {
        isBusy = false;
        elevatorLeft.setTargetPosition(position);
        elevatorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevatorLeft.setPower(power);
        elevatorRight.setTargetPosition(position);
        elevatorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevatorRight.setPower(power);
    }

    /**
     * Starts to move the intakeSlide to the ground position.
     * ONLY call this function once for every move to ground!
     * YOU MUST call {@link #tick()} in a loop to stop the intakeSlide when it reaches the ground.
     */
    public void startRetraction() {
        startMoveToPosSetBusy(-1000);
    }

    /**
     * Checks if the slider is retracting and pressing the button. If it is, reset the encoder.
     */
    @Override
    public void tick() {
        if ((elevatorBtnLeft.isPressed() && elevatorLeft.isBusy()) || (elevatorBtnRight.isPressed() && elevatorRight.isBusy())) {
            int targetPosLeft = elevatorLeft.getTargetPosition();
            int targetPosRight = elevatorRight.getTargetPosition();
            double powerLeft = elevatorLeft.getPower();
            double powerRight = elevatorRight.getPower();
            elevatorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            elevatorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            elevatorLeft.setTargetPosition(Math.max(targetPosLeft, 0));
            elevatorRight.setTargetPosition(Math.max(targetPosRight, 0));
            elevatorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            elevatorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            elevatorLeft.setPower(powerLeft);
            elevatorRight.setPower(powerRight);
        }
        if (isBusy && !elevatorLeft.isBusy() && !elevatorRight.isBusy()) {
            isBusy = false;
        }
    }

    /**
     * @return true if the elevator is at the target position
     * @implNote manually check the elevator position and the button because {@link DcMotor#isBusy()} has a lot of delay.
     */
    @Override
    public boolean isFinished() {
        return !isBusy;
    }

    public void stop() {
        isBusy = false;
    }

    public double getCurrent() {
        return elevatorLeft.getCurrent(CurrentUnit.AMPS);
    }

    public double getPower() {
        return elevatorLeft.getPower();
    }

    public int[] getTargetPosition() {
        return new int[]{elevatorLeft.getTargetPosition(), elevatorRight.getTargetPosition()};
    }

    /**
     * Encoders may not work when the wire is not compatible with the motor.
     *
     * @return the current reading of the encoder for this motor
     * @see DcMotor#getCurrentPosition()
     */
    public int[] getCurrentPosition() {
        return new int[]{elevatorLeft.getCurrentPosition(), elevatorRight.getCurrentPosition()};
    }
}
