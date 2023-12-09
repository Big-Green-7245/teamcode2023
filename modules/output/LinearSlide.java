package org.firstinspires.ftc.teamcode.modules.output;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
        //   elevatorBtn = map.get(RevTouchSensor.class, name + "Btn");
        elevatorLeft = (DcMotorEx) map.get(DcMotor.class, name+"Left");

        elevatorLeft.setDirection(DcMotor.Direction.REVERSE);
        elevatorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elevatorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevatorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevatorRight = (DcMotorEx) map.get(DcMotor.class, name+"Right");

        elevatorRight.setDirection(DcMotor.Direction.FORWARD);
        elevatorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elevatorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevatorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveUsingEncoder(double power) {
        elevatorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevatorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevatorLeft.setPower(power);
        elevatorRight.setPower(power);
    }

    public void startMoveToPos(int position) {
        elevatorLeft.setTargetPosition(position);
        elevatorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevatorLeft.setPower(power);
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
            elevatorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            elevatorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            elevatorLeft.setTargetPosition(2);
            elevatorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            retracting = false;
        }
    }

    /**
     * @return true if the elevator is at the target position
     * @implNote manually check the elevator position and the button because {@link DcMotor#isBusy()} has a lot of delay.
     */
    @Override
    public boolean isFinished() {
        return Math.abs(elevatorLeft.getTargetPosition() - elevatorLeft.getCurrentPosition()) <= 20;
    }

    public double getCurrent() {
        return elevatorLeft.getCurrent(CurrentUnit.AMPS);
    }

    public double getPower() {
        return elevatorLeft.getPower();
    }

    public int getTargetPosition() {
        return elevatorLeft.getTargetPosition();
    }

    /**
     * Encoders may not work when the wire is not compatible with the motor.
     *
     * @return the current reading of the encoder for this motor
     * @see DcMotor#getCurrentPosition()
     */
    public int[] getCurrentPosition() {
        return new int[] {elevatorLeft.getCurrentPosition(), elevatorRight.getCurrentPosition()};
    }
}
