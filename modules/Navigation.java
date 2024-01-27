package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.modules.Webcams.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

import java.util.Arrays;

import static java.lang.Thread.sleep;

public class Navigation {
    private double[] currentPos;
    private double currentBearing;

    private double[] lastDriveTrainPos;

    private double[] lastAprilTagPos;

    private double lastDriveTrainBearing;

    private LinearOpMode opMode;

    private DriveTrain driveTrain;

    public AprilTagWebcam tagCam;

    BNO055IMU imu;
    private double initGyroReading;

    public Navigation(double[] initPos, double initBearing, LinearOpMode opMode, HardwareMap map) {
        currentPos = initPos;
        lastAprilTagPos = initPos;
        lastDriveTrainPos = initPos;
        lastDriveTrainBearing = initBearing;
        currentBearing = initBearing;
        this.opMode = opMode;
        driveTrain = new DriveTrain(opMode);
        driveTrain.init(map);
        tagCam = new AprilTagWebcam();
        tagCam.init(map);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        imu = map.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        while (opMode.opModeInInit() && !imu.isGyroCalibrated()) {
            TelemetryWrapper.setLine(2, "Calibrating gyro...");
        }
        TelemetryWrapper.setLine(2, "Gyro calibrated");
        initGyroReading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle - initBearing;
    }

    public double[] getCurrentPosition() {
        return currentPos;
    }

    public double getCurrentBearing() {
        return currentBearing;
    }

    public double getGyroBearing() {
        return ((imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle - initGyroReading % 360) + 360) % 360;
    }

    public double magnitude(double[] vector) {
        double sum = 0;
        for (double component : vector) {
            sum += Math.pow(component, 2);
        }
        return Math.sqrt(sum);
    }

    /**
     * Linear Algebra Three.VI.1.14
     * Magnitude of the projection of vector onto bearing.
     */
    public double projMag(double[] vector, double bearing) {
        // return the vector projected on to the vector cos(bearing), sin(bearing)
        double[] bearingVector = new double[]{Math.cos(Math.toRadians(bearing)), Math.sin(Math.toRadians(bearing))};
        double proj = vector[0] * bearingVector[0] + vector[1] * bearingVector[1];
        return proj / magnitude(bearingVector);
    }

    public double[] getCurrentPos() {
        lastAprilTagPos = tagCam.detectIter(currentBearing);
        if (tagCam.isDetecting) {
            return lastAprilTagPos;
        }
        return lastDriveTrainPos;
    }

    public double getMinAngle(double currentAngle, double targetAngle){
        double delta1 = targetAngle - currentAngle;
        double delta2 = targetAngle - currentAngle + 360;
        double delta3 = targetAngle - currentAngle - 360;
        if (Math.abs(delta1) < Math.abs(delta2) && Math.abs(delta1) < Math.abs(delta3)){
            return -delta1;
        }else if (Math.abs(delta2) < Math.abs(delta1) && Math.abs(delta2) < Math.abs(delta3)){
            return -delta2;
        }
        return -delta3;
    }
    public void setBearing(double targetBearing) {
        targetBearing = ((targetBearing % 360) + 360) % 360;
        currentBearing = getGyroBearing();
        double dAngle = getMinAngle(currentBearing, targetBearing);
        while (opMode.opModeIsActive() && Math.abs(dAngle) > 1) {
            TelemetryWrapper.setLine(6, "Target Bearing: " + targetBearing + " | " + "Current Bearing: " + currentBearing);
            driveTrain.move(0, 0, Math.signum(dAngle) * Range.clip(dAngle * dAngle / 1000, 0.1, 1), 1);
            currentBearing = getGyroBearing();
            dAngle = getMinAngle(currentBearing, targetBearing);
        }
        driveTrain.stopStayInPlace();
    }


    public void moveToPosDirect(double[] targetPos) {
        moveToPosDirect(targetPos, 1);
    }

    public void moveToPosDirectNoCorrection(double[] targetPos) {
        moveToPosDirect(targetPos, 1, false);
    }

    public void moveToPosDirect(double[] targetPos, int direction) {
        moveToPosDirect(targetPos, direction, true);
    }

    /**
     * @param direction 1 for normal behavior, where the camera faces the robot motion direction, -1 for opposite behavior
     */
    public void moveToPosDirect(double[] targetPos, int direction, boolean correction) {
        double targetBearing = Math.toDegrees(Math.atan2(targetPos[1] - currentPos[1], targetPos[0] - currentPos[0]));
        setBearing(direction == 1 ? targetBearing : targetBearing + 180);
        double[] lastEncoderPos = driveTrain.getEncPos();
        double[] startPos = Arrays.copyOf(currentPos, 2);
        double[] displacement = new double[]{targetPos[0] - currentPos[0], targetPos[1] - currentPos[1]};
        double projectedDisplacementMagnitude;
        double speed = 0;
        long prevTime = System.currentTimeMillis(); // TODO debug remove

        while (opMode.opModeIsActive() && (projectedDisplacementMagnitude = projMag(displacement, currentBearing)) > (correction ? 2 : 4)) {
            // Dot product between the vector from targetPos to startPos and the vector targetPos to currentPos to calculate which direction to go towards
            double positivePower = direction * Math.signum((startPos[0] - targetPos[0]) * (currentPos[0] - targetPos[0]) + (startPos[1] - targetPos[1]) * (currentPos[1] - targetPos[1]));
            driveTrain.move(0, positivePower * (Range.clip(projectedDisplacementMagnitude * projectedDisplacementMagnitude / 200, 0.05, 0.7) - speed / 10), 0, 1);
            long curTime = System.currentTimeMillis(); // TODO debug remove
            TelemetryWrapper.setLine(11, "Navigation movement time for tick: " + (curTime - prevTime)); // TODO debug remove

            TelemetryWrapper.setLine(5, "x: " + currentPos[0] + "| y: " + currentPos[1] + "| theta: " + currentBearing);
            double[] currentEncPos = driveTrain.getEncPos();
            currentBearing = getGyroBearing();
            TelemetryWrapper.setLine(10, "Detecting april tags..."); // TODO debug remove
            lastAprilTagPos = tagCam.detectIter(currentBearing);
            if (tagCam.isDetecting) {
                currentPos = lastAprilTagPos;
                TelemetryWrapper.setLine(10, ""); // TODO debug remove
                TelemetryWrapper.setLine(9, "April tag detected."); // TODO debug remove
                TelemetryWrapper.setLine(7, "CAM");
            } else {
                TelemetryWrapper.setLine(7, "ENC");
                double delta = 0;
                for (int i = 0; i < lastEncoderPos.length; i++) {
                    delta += Math.abs(currentEncPos[i] - lastEncoderPos[i]) / driveTrain.COUNTS_PER_INCH;
                }
                delta /= lastEncoderPos.length;
                currentPos = new double[]{currentPos[0] + Math.cos(Math.toRadians(currentBearing)) * delta, currentPos[1] + Math.sin(Math.toRadians(currentBearing)) * delta};
            }
            double[] newDisplacement = new double[]{targetPos[0] - currentPos[0], targetPos[1] - currentPos[1]};
            speed = magnitude(new double[] {newDisplacement[0]-displacement[0], newDisplacement[1]-displacement[1]});
            displacement = new double[]{targetPos[0] - currentPos[0], targetPos[1] - currentPos[1]};
            lastEncoderPos = currentEncPos;
            prevTime = curTime; // TODO debug remove
        }
        driveTrain.stopStayInPlace();
        if (correction) {
            strafeToPos(targetPos);
        }
    }

    public void strafeToPos(double[] targetPos) {
        double[] displacement = new double[]{targetPos[0] - currentPos[0], targetPos[1] - currentPos[1]};
        double dx = Math.cos(Math.toRadians(-currentBearing - 90)) * (displacement[0]) - Math.sin(Math.toRadians(-currentBearing - 90)) * (displacement[1]);
        double dy = Math.sin(Math.toRadians(-currentBearing - 90)) * (displacement[0]) + Math.cos(Math.toRadians(-currentBearing - 90)) * (displacement[1]);

        driveTrain.translate(0.3, dx, 0, 0, 10);
        driveTrain.translate(0.3, 0, dy, 0, 10);
        double startTime = opMode.getRuntime();
        double time = startTime;
        lastAprilTagPos = tagCam.detectIter(currentBearing);
        while (time-startTime < 0.2 && !tagCam.isDetecting && opMode.opModeIsActive()){
            time = opMode.getRuntime();
            lastAprilTagPos = tagCam.detectIter(currentBearing);
        }
        if (tagCam.isDetecting) {
            currentPos = lastAprilTagPos;
            TelemetryWrapper.setLine(7, "CAM");
            TelemetryWrapper.setLine(5, "x: " + currentPos[0] + "| y: " + currentPos[1] + "| theta: " + currentBearing);
        } else {
            currentPos = targetPos;
            TelemetryWrapper.setLine(7, "ENC");
            TelemetryWrapper.setLine(5, "x: " + currentPos[0] + "| y: " + currentPos[1] + "| theta: " + currentBearing);
        }
        driveTrain.stopStayInPlace();
    }

    public void calibrate() throws InterruptedException {
        lastAprilTagPos = tagCam.detectIter(getGyroBearing());
        while(!tagCam.isDetecting && opMode.opModeIsActive()){
            lastAprilTagPos = tagCam.detectIter(getGyroBearing());
        }
        double[] startPos = new double[]{lastAprilTagPos[0], lastAprilTagPos[1]};
        driveTrain.translate(0.7, 0, -20, 0, 10);
        sleep(3000);
        lastAprilTagPos = tagCam.detectIter(getGyroBearing());
        while(!tagCam.isDetecting && opMode.opModeIsActive()){
            lastAprilTagPos = tagCam.detectIter(getGyroBearing());
        }
        TelemetryWrapper.setLine(11, "Correction: " + 20/(startPos[0]-lastAprilTagPos[0]));
        TelemetryWrapper.setLine(12, "x: " + startPos[0] + "y: "+ startPos[1]);
        TelemetryWrapper.setLine(13, "x: " + lastAprilTagPos[0] + "y: "+ lastAprilTagPos[1]);
    }

    public void moveToPosAtAngle(double[] targetPos, double angle) {
        double directAngle = Math.toDegrees(Math.atan2(targetPos[1] - currentPos[1], targetPos[0] - currentPos[0]));
        directAngle = ((directAngle % 360) + 360) % 360;
        double deltaTheta = directAngle - angle;
        if (deltaTheta < 45 && deltaTheta > -45) {
            driveTrain.move(0, 0.5, 0, 0);
        } else if (deltaTheta > 45 && deltaTheta < 135) {
            setBearing(directAngle - 90);
            driveTrain.move(-0.5, 0, 0, 0);
        } else if (deltaTheta < -45 && deltaTheta > -135) {
            setBearing(directAngle + 90);
            driveTrain.move(0.5, 0, 0, 0);
        } else {
            driveTrain.move(0, -0.5, 0, 0);
        }

        double[] lastEncoderPos = driveTrain.getEncPos();
        double[] displacement = new double[]{targetPos[0] - currentPos[0], targetPos[1] - currentPos[1]};

        while (opMode.opModeIsActive() && magnitude(displacement) > 4) {
            TelemetryWrapper.setLine(5, "x: " + currentPos[0] + "| y: " + currentPos[1] + "| theta: " + currentBearing);
            double[] currentEncPos = driveTrain.getEncPos();
            currentBearing = getGyroBearing();
            lastAprilTagPos = tagCam.detectIter(currentBearing);
            if (tagCam.isDetecting) {
                currentPos = lastAprilTagPos;
                TelemetryWrapper.setLine(9, "CAM");
                TelemetryWrapper.setLine(7, "CAM");
            } else {
                TelemetryWrapper.setLine(7, "ENC");
                double delta = 0;
                for (int i = 0; i < lastEncoderPos.length; i++) {
                    delta += Math.abs(currentEncPos[i] - lastEncoderPos[i]) / driveTrain.COUNTS_PER_INCH;
                }
                delta /= lastEncoderPos.length;
                currentPos = new double[]{currentPos[0] + Math.cos(Math.toRadians(currentBearing)) * delta, currentPos[1] + Math.sin(Math.toRadians(currentBearing)) * delta};
            }
            displacement = new double[]{targetPos[0] - currentPos[0], targetPos[1] - currentPos[1]};
            lastEncoderPos = currentEncPos;
        }
        driveTrain.stopStayInPlace();
        strafeToPos(targetPos);
        setBearing(angle);
    }

}
