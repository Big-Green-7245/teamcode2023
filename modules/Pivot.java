package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Pivot implements Modulable, Tickable {
    private final ElapsedTime runtime = new ElapsedTime();

    private static final double POWER = 0.2;

    public HardwareMap hwMap;
    private DcMotor rotation;

    public TouchSensor intakeButton;
    public TouchSensor placeButton;
    /**
     * The target orientation that the claw is currently moving to.
     */
    private boolean targetOrientation;


    @Override
    public void init(HardwareMap hardwareMap) {
        hwMap = hardwareMap;
        rotation = hardwareMap.get(DcMotor.class, "rotation");
        intakeButton = hardwareMap.get(TouchSensor.class, "intakeBtn");
        placeButton = hardwareMap.get(TouchSensor.class, "placeBtn");
        rotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        setIntakeOrientation(true);
    }

    public void move(double power) {
        rotation.setPower(power);
    }

    /**
     * @param orientation whether to be at intake orientation
     */
    public void setIntakeOrientation(boolean orientation) {
        targetOrientation = orientation;
    }

    @Override
    public void tickBeforeStart() {
        tick();
    }

    /**
     * Ticks the pivot to move towards the target orientation.
     */
    public void tick() {
        if (targetOrientation && !intakeButton.isPressed()) {
            move(POWER);
        } else if (!targetOrientation && !placeButton.isPressed()) {
            move(-POWER);
        } else {
            move(0);
        }
    }
}
