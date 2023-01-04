package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Pivot implements Modulable, Tickable {
    private final ElapsedTime runtime = new ElapsedTime();

    private static final double POWER = 0.3;

    public HardwareMap hwMap;
    private DcMotor rotation;

    private TouchSensor intakeButton;
    private TouchSensor placeButton;
    /**
     * The target orientation that the claw is currently moving to.
     */
    private boolean targetOrientation;
    public static final boolean INTAKE_ORIENTATION = false;
    public static final boolean PLACE_ORIENTATION = true;

    public TouchSensor getIntakeButton() {
        return intakeButton;
    }

    public TouchSensor getPlaceButton() {
        return placeButton;
    }

    @Override
    public void init(HardwareMap hardwareMap) {
        hwMap = hardwareMap;
        rotation = hardwareMap.get(DcMotor.class, "rotation"); // Expansion Hub 1
        intakeButton = hardwareMap.get(TouchSensor.class, "intakeBtn"); // Expansion Hub 1
        placeButton = hardwareMap.get(TouchSensor.class, "placeBtn"); // Expansion Hub 3
        rotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        setIntakeOrientation(INTAKE_ORIENTATION);
    }

    public void move(double power) {
        rotation.setPower(power);
    }

    public boolean getTargetOrientation() {
        return targetOrientation;
    }

    /**
     * @param orientation whether to be at intake orientation
     */
    public void setIntakeOrientation(boolean orientation) {
        targetOrientation = orientation;
    }

    /**
     * Toggles the intake orientation.
     */
    public void toggleIntakeOrientation() {
        targetOrientation = !targetOrientation;
    }

    @Override
    public void tickBeforeStart() {
        tick();
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

    public boolean isAtTargetPos() {
        return (targetOrientation && placeButton.isPressed()) || (!targetOrientation && intakeButton.isPressed());
    }
}
