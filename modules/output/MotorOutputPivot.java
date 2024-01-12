package org.firstinspires.ftc.teamcode.modules.output;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.teamcode.modules.Modulable;
import org.firstinspires.ftc.teamcode.modules.Tickable;

public class MotorOutputPivot implements Modulable, Tickable {
    public static final int OUTPUT_POS = 0;
    private static final int RELOAD_POS = 0;
    private final String name;
    private final double power;
    protected DcMotor pivot;
    public TouchSensor intakeButton;
    private boolean atOutput;

    public MotorOutputPivot(String name, double power) {
        this.name = name;
        this.power = power;
    }

    public void moveUsingEncoder(double power){
        pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pivot.setPower(power);
    }

    public int getPosition() {
        return pivot.getCurrentPosition();
    }

    public void setPosition(int position) {
        pivot.setTargetPosition(position);
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

    public void startMovePivot(){
        pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /**
     * Makes the pivot start to move towards the specified position.
     *
     * @param atOutput start move towards the intake or output position of the pivot
     */
    public void startMoveToPos(boolean atOutput) {
        startMoveToPos(atOutput ? OUTPUT_POS : RELOAD_POS);
    }

    public void startMoveToPosToggle() {
        startMoveToPos(atOutput = !atOutput);
    }

    public void startMoveToRelativePos(int relativePosition) {
        startMoveToPos(Math.max(pivot.getCurrentPosition() + relativePosition, 10));
    }

    public void startMoveToPos(int position) {
        pivot.setTargetPosition(position);
        pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pivot.setPower(power);
    }

    @Override
    public void tick() {
        if (pivot.isBusy() && intakeButton.isPressed()) {
            int targetPos = pivot.getTargetPosition();
            double power = pivot.getPower();
            pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            pivot.setTargetPosition(Math.max(targetPos, 0));
            pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            pivot.setPower(power);
        }
    }
}
