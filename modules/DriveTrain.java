package org.firstinspires.ftc.teamcode.modules;

import androidx.annotation.NonNull;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;


public class DriveTrain implements Modulable {
    private final ElapsedTime runtime = new ElapsedTime();

    final double XY_CORRECTION = 3.06*(10/9.5)*(20/19)/(24/33.25)*(8.5/10) / 2.85;
    final double COUNTS_PER_INCH = 54*(10/8.5)*(24/33.25) * 2.54 / 3.06;
    final double COUNTS_PER_DEGREE = 11.5;
    private final LinearOpMode opMode;

    public HardwareMap hwMap;

    public DcMotorEx backRight;
    public DcMotorEx frontRight;
    public DcMotorEx backLeft;
    public DcMotorEx frontLeft;


    public DriveTrain(@NonNull LinearOpMode opMode) {
        this.opMode = opMode;
    }

    @Override
    public void init(HardwareMap map) {
        hwMap = map;

        frontLeft = hwMap.get(DcMotorEx.class, "frontLeft"); // Control Hub 0
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);

        frontRight = hwMap.get(DcMotorEx.class, "frontRight"); // Control Hub 1
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);

        backLeft = hwMap.get(DcMotorEx.class, "backLeft"); // Control Hub 2
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);

        backRight = hwMap.get(DcMotorEx.class, "backRight"); // Control Hub 3
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        setModeToAllDriveMotors(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModeToAllDriveMotors(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Sets the power to the motors directly
     *
     * @param powerx power for horizontal movement, positive right
     * @param powery power for vertical movement, positive up
     * @param turn   power for rotational movement, positive is CCW
     * @param factor applied to all power
     */
    public void move(double powerx, double powery, double turn, double factor) {
        setModeToAllDriveMotors(DcMotor.RunMode.RUN_USING_ENCODER);
        double speedx = factor * powerx;
        double speedy = factor * powery;
        double offset = factor * turn;



        frontRight.setPower(Range.clip(speedy + speedx + offset, -1, 1));
        frontLeft.setPower(Range.clip(speedy - speedx - offset, -1, 1));
        backRight.setPower(Range.clip(speedy - speedx + offset, -1, 1));
        backLeft.setPower(Range.clip(speedy + speedx - offset, -1, 1));
    }

    /**
     * Translate robot by given displacement
     *
     * @param power   the power when running to target
     * @param dX      horizontal displacement, positive is right
     * @param dY      vertical displacement, positive is up
     * @param dTheta  rotational displacement, positive is CW
     * @param timeout time before terminating RUN_TO_POSITION
     */
    public void translate(double power, double dX, double dY, double dTheta, double timeout) {
        int newFLTarget, newFRTarget, newBLTarget, newBRTarget;
        int dFL, dFR, dBL, dBR;
        // Determine new target position, and pass to motor controller
        dFL = (int) ((-dY + dX * XY_CORRECTION) * COUNTS_PER_INCH + dTheta * COUNTS_PER_DEGREE );
        dFR = (int) ((-dY - dX * XY_CORRECTION) * COUNTS_PER_INCH - dTheta * COUNTS_PER_DEGREE);
        dBL = (int) ((-dY - dX * XY_CORRECTION) * COUNTS_PER_INCH + dTheta * COUNTS_PER_DEGREE);
        dBR = (int) ((-dY + dX * XY_CORRECTION) * COUNTS_PER_INCH - dTheta * COUNTS_PER_DEGREE);

        setModeToAllDriveMotors(DcMotor.RunMode.RUN_USING_ENCODER);
        setModeToAllDriveMotors(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        newFLTarget = frontRight.getCurrentPosition() + dFL;
        newFRTarget = frontLeft.getCurrentPosition() + dFR;
        newBLTarget = backRight.getCurrentPosition() + dBL;
        newBRTarget = backLeft.getCurrentPosition() + dBR;

        frontRight.setTargetPosition(newFLTarget);
        frontLeft.setTargetPosition(newFRTarget);
        backRight.setTargetPosition(newBLTarget);
        backLeft.setTargetPosition(newBRTarget);

        // Turn On RUN_TO_POSITION
        setModeToAllDriveMotors(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion
        runtime.reset();

        frontRight.setPower(power);
        frontLeft.setPower(power);
        backRight.setPower(power);
        backLeft.setPower(power);

        TelemetryWrapper.setLine(0, "Running to (x:y:r)=(" + dX + ":" + dY + ":" + dTheta + ")");
        TelemetryWrapper.setLine(1, "Running delta (dFL:dFR:dBL:dBR)=(" + dFL + ":" + dFR + ":" + dBL + ":" + dBR + ")");
        TelemetryWrapper.setLine(2, "Wheels to (lf:rf:lr:rr)=(" + newFLTarget + ":" + newFRTarget + ":" + newBLTarget + ":" + newBRTarget + ")");
        while (opMode.opModeIsActive() && (runtime.seconds() < timeout) && (frontRight.isBusy() && frontLeft.isBusy() && backRight.isBusy() && backLeft.isBusy())) {
            TelemetryWrapper.setLine(3, "Running @ (" + frontRight.getCurrentPosition() + ":" + frontLeft.getCurrentPosition() + ":" + backRight.getCurrentPosition() + ":" + backLeft.getCurrentPosition() + ")");
        }

        // Stop all motion
        setZeroPowerBehaviorToAllDriveMotors(DcMotor.ZeroPowerBehavior.BRAKE);
        setPowerToAllDriveMotors(0);
        TelemetryWrapper.setLine(10, "Motor power 0");

        // Turn off RUN_TO_POSITION
        setModeToAllDriveMotors(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void stayInPlace(double power) {
        setModeToAllDriveMotors(DcMotor.RunMode.RUN_USING_ENCODER);
        setModeToAllDriveMotors(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setTargetPosition(frontRight.getCurrentPosition());
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition());
        backRight.setTargetPosition(backRight.getCurrentPosition());
        backLeft.setTargetPosition(backLeft.getCurrentPosition());
        setModeToAllDriveMotors(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerToAllDriveMotors(power);
    }

    public void stopStayInPlace() {
        setPowerToAllDriveMotors(0);
        setModeToAllDriveMotors(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Rotate robot around center at given power
     *
     * @param radius radius of circle robot rotates around in inches
     * @param power  speed of rotation
     */
    public void rotateAroundCenter(double radius, double power) {
        double inchPerDegrees = COUNTS_PER_DEGREE / COUNTS_PER_INCH * (radius * Math.PI) / 180;
        move(power * inchPerDegrees, 0, power, 0);
    }

    /**
     * Returns encoder positions of encoders
     *
     * @return array containing position of each wheel
     */
    public double[] getEncPos() {
        return new double[]{frontRight.getCurrentPosition(), frontLeft.getCurrentPosition(), backRight.getCurrentPosition(), backLeft.getCurrentPosition()};
    }

    /**
     * Returns string containing current positions of ecoders
     *
     * @return string containing position of each wheel
     */
    public String getEncPosStr() {
        return "FL=" + frontRight.getCurrentPosition() + " FR" + frontLeft.getCurrentPosition() + " BL" + backRight.getCurrentPosition() + " BR" + backLeft.getCurrentPosition();
    }

    /**
     *
     */
    public double[] getMotorCurrents() {
        return new double[]{frontRight.getCurrent(CurrentUnit.AMPS), frontLeft.getCurrent(CurrentUnit.AMPS), backRight.getCurrent(CurrentUnit.AMPS), backLeft.getCurrent(CurrentUnit.AMPS)};
    }

    public String getMotorCurrentsString() {
        return "FL=" + frontRight.getCurrent(CurrentUnit.AMPS) + " FR" + frontLeft.getCurrent(CurrentUnit.AMPS) + " BL" + backRight.getCurrent(CurrentUnit.AMPS) + " BR" + backLeft.getCurrent(CurrentUnit.AMPS);
    }

    /**
     * Set power to all motors
     *
     * @param powerForAll the power for all wheels
     */
    public void setPowerToAllDriveMotors(double powerForAll) {
        frontRight.setPower(powerForAll);
        frontLeft.setPower(powerForAll);
        backRight.setPower(powerForAll);
        backLeft.setPower(powerForAll);
    }

    /**
     * Set mode to all motors
     *
     * @param runModeForAll desired mode for motors
     */
    public void setModeToAllDriveMotors(DcMotor.RunMode runModeForAll) {
        frontRight.setMode(runModeForAll);
        frontLeft.setMode(runModeForAll);
        backRight.setMode(runModeForAll);
        backLeft.setMode(runModeForAll);
    }

    /**
     * Set zero power behavior to all motors
     *
     * @param zeroPowerBehaviorForAll desired zeroPowerBehavior for motors
     */
    public void setZeroPowerBehaviorToAllDriveMotors(DcMotor.ZeroPowerBehavior zeroPowerBehaviorForAll) {
        frontRight.setZeroPowerBehavior(zeroPowerBehaviorForAll);
        frontLeft.setZeroPowerBehavior(zeroPowerBehaviorForAll);
        backRight.setZeroPowerBehavior(zeroPowerBehaviorForAll);
        backLeft.setZeroPowerBehavior(zeroPowerBehaviorForAll);
    }

    /**
     * Wrapper method for stopping the robot, sets 0 power to all motors
     */
    public void stop() {
        setPowerToAllDriveMotors(0);
    }
}
