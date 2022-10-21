package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.util.*;


public class DriveTrain implements Modulable
{
    private ElapsedTime runtime = new ElapsedTime();

    final double XY_CORRECTION = 1;
    final double COUNTS_PER_INCH = 1;
    final double COUNTS_PER_DEGREE = 1;

    public HardwareMap hwMap;

    public DcMotorEx backLeft;
    public DcMotorEx frontLeft;
    public DcMotorEx backRight;
    public DcMotorEx frontRight;

    @Override
    public void init(HardwareMap map)
    {
        hwMap = map;

        backLeft = (DcMotorEx) hwMap.get(DcMotor.class, "leftBack");
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        backRight = (DcMotorEx) hwMap.get(DcMotor.class, "rightBack");
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        frontLeft = (DcMotorEx) hwMap.get(DcMotor.class, "leftFront");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);

        frontRight = (DcMotorEx) hwMap.get(DcMotor.class, "rightFront");
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);

        setModeToAllDriveMotors(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModeToAllDriveMotors(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Sets the power to the motors directly
     *
     * @param powerx power for horizontal movement, positive right
     * @param powery power for vertical movement, positive up
     * @param turn power for rotational movement, positive is CCW
     * @param factor applied to all power
     */
    public void move(double powerx, double powery, double turn, double factor) {
        double speedx = factor * powerx;
        double speedy = factor * powery;
        double offset = factor * turn;

        frontLeft.setPower(Range.clip(speedy+speedx+offset,-1,1));
        frontRight.setPower(Range.clip(speedy-speedx-offset,-1,1));
        backLeft.setPower(Range.clip(speedy-speedx+offset,-1,1));
        backRight.setPower(Range.clip(speedy+speedx-offset,-1,1));
    }

    /**
     * Translate robot by given displacement
     * @param power the power when running to target
     * @param dX horizontal displacement, positive is right
     * @param dY vertical displacement, positive is up
     * @param dTheta rotational displacement, positive is CCW
     * @param timeout time before terminating RUN_TO_POSITION
     */
    public void translate(double power, double dX, double dY, double dTheta, double timeout)
    {
        int newFLTarget, newFRTarget, newBLTarget, newBRTarget;
        int dFL, dFR, dBL, dBR;

        // Determine new target position, and pass to motor controller
        dFL = (int)((-dY +dX * XY_CORRECTION) * COUNTS_PER_INCH - dTheta * COUNTS_PER_DEGREE);
        dFR = (int)((-dY -dX * XY_CORRECTION) * COUNTS_PER_INCH + dTheta * COUNTS_PER_DEGREE);
        dBL = (int)((-dY -dX * XY_CORRECTION) * COUNTS_PER_INCH - dTheta * COUNTS_PER_DEGREE);
        dBR = (int)((-dY +dX * XY_CORRECTION) * COUNTS_PER_INCH + dTheta * COUNTS_PER_DEGREE);

        setModeToAllDriveMotors(DcMotor.RunMode.RUN_USING_ENCODER);
        setModeToAllDriveMotors(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        newFLTarget = frontLeft.getCurrentPosition() + dFL;
        newFRTarget = frontRight.getCurrentPosition() + dFR;
        newBLTarget = backLeft.getCurrentPosition() + dBL;
        newBRTarget = backRight.getCurrentPosition() + dBR;

        frontLeft.setTargetPosition(newFLTarget);
        frontRight.setTargetPosition(newFRTarget);
        backLeft.setTargetPosition(newBLTarget);
        backRight.setTargetPosition(newBRTarget);

        // Turn On RUN_TO_POSITION
        setModeToAllDriveMotors(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion
        runtime.reset();

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);

        TelemetryWrapper.setLine(0,  "Running to (x:y:r)=("+dX+":"+dY +":"+dTheta+")");
        TelemetryWrapper.setLine(1,  "Running delta (dFL:dFR:dBL:dBR)=("+dFL+":"+dFR +":"+dBL +":"+dBR+")");
        TelemetryWrapper.setLine(2,  "Wheels to (lf:rf:lr:rr)=("+newFLTarget+":"+newFRTarget +":"+newBLTarget+":"+newBRTarget+")");
        while ((runtime.seconds() < timeout) && (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy())) {
            TelemetryWrapper.setLine(3,  "Running @ ("+frontLeft.getCurrentPosition()+":"+frontRight.getCurrentPosition()
                    +":"+backLeft.getCurrentPosition()+":"+backRight.getCurrentPosition()+")");
        }

        // Stop all motion
        setZeroPowerBehaviorToAllDriveMotors(DcMotor.ZeroPowerBehavior.BRAKE);
        setPowerToAllDriveMotors(0);
        TelemetryWrapper.setLine(10, "Motor power 0");

        // Turn off RUN_TO_POSITION
        setModeToAllDriveMotors(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Returns encoder positions of encoders
     * @return array containing position of each wheel
     */
    public double[] getEncPos() {
        return new double[]{frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(),
                backLeft.getCurrentPosition(), backRight.getCurrentPosition()};
    }

    /**
     * Returns string containing current positions of ecoders
     * @return string containing position of each wheel
     */
    public String getEncPosStr() {
        return "FL=" + frontLeft.getCurrentPosition() + " FR" + frontRight.getCurrentPosition() +
                " BL" + backLeft.getCurrentPosition() + " BR" + backRight.getCurrentPosition();
    }

    /**
     *
     */
    public double[] getMotorCurrents() {
        return new double[]{frontLeft.getCurrent(CurrentUnit.AMPS), frontRight.getCurrent(CurrentUnit.AMPS),
                backLeft.getCurrent(CurrentUnit.AMPS), backRight.getCurrent(CurrentUnit.AMPS)};
    }

    public String getMotorCurrentsString() {
        return "FL=" + frontLeft.getCurrent(CurrentUnit.AMPS) + " FR" + frontRight.getCurrent(CurrentUnit.AMPS) +
                " BL" + backLeft.getCurrent(CurrentUnit.AMPS) + " BR" + backRight.getCurrent(CurrentUnit.AMPS);
    }

        /**
         * Set power to all motors
         * @param powerForAll the power for all wheels
         */
    public void setPowerToAllDriveMotors(double powerForAll) {
        frontLeft.setPower(powerForAll);
        frontRight.setPower(powerForAll);
        backLeft.setPower(powerForAll);
        backRight.setPower(powerForAll);
    }

    /**
     * Set mode to all motors
     * @param runModeForAll desired mode for motors
     */
    public void setModeToAllDriveMotors(DcMotor.RunMode runModeForAll) {
        frontLeft.setMode(runModeForAll);
        frontRight.setMode(runModeForAll);
        backLeft.setMode(runModeForAll);
        backRight.setMode(runModeForAll);
    }

    /**
     * Set zero power behavior to all motors
     * @param zeroPowerBehaviorForAll desired zeroPowerBehavior for motors
     */
    public void setZeroPowerBehaviorToAllDriveMotors(DcMotor.ZeroPowerBehavior zeroPowerBehaviorForAll) {
        frontLeft.setZeroPowerBehavior(zeroPowerBehaviorForAll);
        frontRight.setZeroPowerBehavior(zeroPowerBehaviorForAll);
        backLeft.setZeroPowerBehavior(zeroPowerBehaviorForAll);
        backRight.setZeroPowerBehavior(zeroPowerBehaviorForAll);
    }

    /**
     * Wrapper method for stopping the robot, sets 0 power to all motors
     */
    public void stop() {
        setPowerToAllDriveMotors(0);
    }
}
