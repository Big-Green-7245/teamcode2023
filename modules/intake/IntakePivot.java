package org.firstinspires.ftc.teamcode.modules.intake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.modules.Modulable;
import org.firstinspires.ftc.teamcode.util.ChainableBooleanSupplier;

public class IntakePivot implements Modulable, ChainableBooleanSupplier {
    private DcMotorEx pivot;

    @Override
    public void init(HardwareMap hardwareMap) {
        pivot = hardwareMap.get(DcMotorEx.class, "intakePivot"); // Expansion Hub 1
        pivot.setDirection(DcMotor.Direction.FORWARD);
        pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pivot.setTargetPosition(0);
        pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pivot.setPower(1);
    }

    public void setTargetOrientation(Orientation orientation) {
        setTargetPosition(orientation.position);
    }

    public void setTargetPosition(int position) {
        pivot.setTargetPosition(position);
    }

    /**
     * @return true if the elevator is at the target position
     */
    @Override
    public boolean getAsBoolean() {
        return !pivot.isBusy();
    }

    public double getCurrent() {
        return pivot.getCurrent(CurrentUnit.AMPS);
    }

    public double getPower() {
        return pivot.getPower();
    }

    public int getTargetPosition() {
        return pivot.getTargetPosition();
    }

    /**
     * Encoders may not work when the wire is not compatible with the motor.
     *
     * @return the current reading of the encoder for this motor
     * @see DcMotor#getCurrentPosition()
     */
    public int getCurrentPosition() {
        return pivot.getCurrentPosition();
    }

    public enum Orientation {
        HOLDER(0), VERTICAL(60), INTAKE(210);

        private final int position;

        Orientation(int position) {
            this.position = position;
        }
    }
}
