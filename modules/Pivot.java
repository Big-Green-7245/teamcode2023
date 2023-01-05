package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Pivot implements Modulable, Tickable {
    private static final double POWER = 0.3;
    public static final boolean INTAKE_ORIENTATION = false;
    public static final boolean PLACE_ORIENTATION = true;
    private DcMotor rotation;
    private TouchSensor intakeButton;
    private TouchSensor placeButton;
    /**
     * The target orientation that the claw is currently moving to.
     */
    private boolean targetOrientation;

    @Override
    public void init(HardwareMap hardwareMap) {
        rotation = hardwareMap.get(DcMotor.class, "rotation"); // Expansion Hub 1
        intakeButton = hardwareMap.get(TouchSensor.class, "intakeBtn"); // Expansion Hub 1
        placeButton = hardwareMap.get(TouchSensor.class, "placeBtn"); // Expansion Hub 3
        rotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        setTargetOrientation(INTAKE_ORIENTATION);
    }

    public void move(double power) {
        rotation.setPower(power);
    }

    /**
     * @param orientation whether to be at intake orientation, use {@link #INTAKE_ORIENTATION} or {@link #PLACE_ORIENTATION}
     */
    public void setTargetOrientation(boolean orientation) {
        targetOrientation = orientation;
    }

    /**
     * Toggles the target orientation.
     */
    public void toggleTargetOrientation() {
        targetOrientation = !targetOrientation;
    }

    public boolean isAtTargetPos() {
        return (targetOrientation && placeButton.isPressed()) || (!targetOrientation && intakeButton.isPressed());
    }

    /**
     * Ticks the pivot to move towards the target orientation.
     */
    public void tick() {
        if (!targetOrientation && !intakeButton.isPressed()) {
            move(POWER);
        } else if (targetOrientation && !placeButton.isPressed()) {
            move(-POWER);
        } else {
            move(0);
        }
    }
}
