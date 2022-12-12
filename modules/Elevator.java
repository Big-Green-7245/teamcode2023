package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

public class Elevator implements Modulable, Tickable {
    private ElapsedTime runtime = new ElapsedTime();

    public HardwareMap hwMap;
    private DcMotorEx elevator;

    public TouchSensor elevatorBtn;

    private final double POWER = 0.7;

    @Override
    public void init(HardwareMap hardwareMap) {
        hwMap = hardwareMap;
        elevator = (DcMotorEx) hardwareMap.get(DcMotor.class, "linearSlide");
        elevatorBtn = hardwareMap.get(RevTouchSensor.class, "elevatorBtn");
        elevator.setDirection(DcMotor.Direction.REVERSE);
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Moves the elevator down if it is not in the lowest position.
     */
    @Override
    public void tickBeforeStart() {
        if (elevatorBtn.isPressed()) {
            move(0);
        } else {
            move(-0.2);
        }
    }

    public void move(double power) {
        elevator.setPower(Math.abs(power) > 0.1 ? power : 0);
    }

    public void moveToPos(int position) {
        elevator.setTargetPosition((int) position);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setPower(POWER);
        while(elevator.isBusy()) {
            TelemetryWrapper.setLine(0, "Elevator running " + elevator.getCurrentPosition());
        }
        // Hold position
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double getCurrent() {
        return elevator.getCurrent(CurrentUnit.AMPS);
    }

    public double getPower(){return elevator.getPower();}

    public double getEncPos(){return (elevator.getCurrentPosition());}
}
