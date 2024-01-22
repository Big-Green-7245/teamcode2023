package org.firstinspires.ftc.teamcode.modules.output;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.teamcode.modules.Modulable;
import org.firstinspires.ftc.teamcode.modules.Tickable;

public class MotorOutputPivot implements Modulable, Tickable {
    /**
     * The position to start moving towards when moving to intake.
     * -1000 because this ensures the pivot moves all the way back and activates the button,
     * no matter where the pivot was initialized.
     * The encoder is then reset to 0 when the button is pressed and {@link #tick()} is called.
     */
    private static final int INTAKE_TARGET_POS = -1000;
    private static final int INTAKE_POS = -2;
    public static final int OUTPUT_POS = 480;
    private final String name;
    private final double power;
    private DcMotor pivot;
    private TouchSensor intakeButton;
    private boolean atOutput;

    public MotorOutputPivot(String name, double power) {
        this.name = name;
        this.power = power;
    }

    public void moveUsingEncoder(double power) {
        pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pivot.setPower(power);
    }

    public int getCurrentPosition() {
        return pivot.getCurrentPosition();
    }

    public int getTargetPosition() {
        return pivot.getTargetPosition();
    }

    public boolean isBusy() {
        return pivot.isBusy();
    }

    public boolean isPressed() {
        return intakeButton.isPressed();
    }

    @Override
    public void init(HardwareMap map) {
        pivot = map.get(DcMotor.class, name);
        pivot.setDirection(DcMotor.Direction.FORWARD);
        pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeButton = map.get(TouchSensor.class, "intakeButton");
    }

    public void startMovePivot() {
        pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /**
     * Makes the pivot start to move towards the specified position.
     *
     * @param atOutput start move towards the intake or output position of the pivot
     */
    public void startMoveToPos(boolean atOutput) {
        this.atOutput = atOutput;
        startMoveToPos(atOutput ? OUTPUT_POS : INTAKE_TARGET_POS);
    }

    public void startMoveToPosToggle() {
        startMoveToPos(atOutput = !atOutput);
    }

    public void startMoveToRelativePos(int relativePosition) {
        startMoveToPos(Math.max(pivot.getCurrentPosition() + relativePosition, INTAKE_TARGET_POS));
    }

    public void startMoveToPos(int position) {
        pivot.setTargetPosition(position);
        pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pivot.setPower(power);
    }

    @Override
    public void tick() {
        if (pivot.getTargetPosition() <= INTAKE_POS && !intakeButton.isPressed()) {
            startMoveToPos(false);
        }
        if (pivot.isBusy() && pivot.getTargetPosition() < INTAKE_POS && intakeButton.isPressed()) {
            int targetPos = pivot.getTargetPosition();
            double power = pivot.getPower();
            pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            pivot.setTargetPosition(Math.max(targetPos, INTAKE_POS));
            pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            pivot.setPower(power);
        }
    }
}
