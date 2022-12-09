package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Pivot implements Modulable {
    private ElapsedTime runtime = new ElapsedTime();

    public HardwareMap hwMap;
    private DcMotor rotation;

    public TouchSensor intakeButton;
    public TouchSensor placeButton;

    private final double POWER = 0.2;



    @Override
    public void init(HardwareMap hardwareMap) {
        hwMap = hardwareMap;
        rotation = hardwareMap.get(DcMotor.class, "rotation");
        intakeButton = hardwareMap.get(TouchSensor.class, "intakeBtn");
        placeButton = hardwareMap.get(TouchSensor.class, "placeBtn");
        setIntakeOrientation(true);
    }

    public void move(double power) {
        rotation.setPower(power);
    }

    /**
     *
     * @param orientation whether to be at intake orientation
     */
    public void setIntakeOrientation (boolean orientation) {
        if (orientation)
        {
            while(!intakeButton.isPressed())
            {
                move(POWER);
            }

        }else{
            while(!placeButton.isPressed())
            {
                move(-POWER);
            }
        }
        rotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
