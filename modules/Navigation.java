package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.modules.Webcams.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

public class Navigation {
    private double[] currentPos;
    private double currentBearing;

    private double[] lastDriveTrainPos;

    private double[] lastAprilTagPos;

    private double lastDriveTrainBearing;

    private DriveTrain driveTrain;

    private AprilTagWebcam tagCam;

    private double lastAprilTagBearing;
    public Navigation (double[] initPos, double initBearing, LinearOpMode opMode, HardwareMap map){
        currentPos = initPos;
        currentBearing = initBearing;
        driveTrain = new DriveTrain(opMode);
        driveTrain.init(map);
        tagCam = new AprilTagWebcam();
        tagCam.init(map);
    }

    public double[] getCurrentPosition(){
        return currentPos;
    }

    public double getCurrentBearing() {
        return currentBearing;
    }

    public double magnitude(double[] vector){
        double sum = 0;
        for (double component:vector){
            sum += Math.pow(component, 2);
        }
        return Math.sqrt(sum);
    }
    public void MoveToPosDirect(double[] targetPos){
        double targetBearing = Math.toDegrees(Math.atan2(targetPos[1]-currentPos[1], targetPos[0]-currentPos[0]));
        targetBearing = (360+targetBearing)%360;
        TelemetryWrapper.setLine(6, "Target Bearing: " + targetBearing + " | " + "Delta Bearing: " + -(targetBearing - currentBearing));
        driveTrain.translate(0.2, 0,0,  -(targetBearing-currentBearing), 10);
        currentBearing = targetBearing;
        double[] lastEncoderPos = driveTrain.getEncPos();
        double[] displacement = new double[]{targetPos[0]- currentPos[0], targetPos[1] - currentPos[1]};
        driveTrain.move(0,0.2, 0, 1);
        while (magnitude(displacement) > 0.5){
            TelemetryWrapper.setLine(5, "x: "+ currentPos[0] + "| y: " + currentPos[1] + "| theta: " + currentBearing);
//            if (tagCam.isDetecting){
//                currentPos = tagCam.detectIter();
//                TelemetryWrapper.setLine(7, "CAM");
//            }else {
                TelemetryWrapper.setLine(7, "ENC");
                double delta = 0;
                for (int i = 0; i < lastEncoderPos.length; i++) {
                    delta += Math.abs(driveTrain.getEncPos()[i] - lastEncoderPos[i]) / driveTrain.COUNTS_PER_INCH;
                }
                delta /= lastEncoderPos.length;
                currentPos = new double[]{currentPos[0] + Math.cos(Math.toRadians(currentBearing)) * delta, currentPos[1] + Math.sin(Math.toRadians(currentBearing)) * delta};
//            }
            displacement = new double[]{targetPos[0]- currentPos[0], targetPos[1] - currentPos[1]};
            lastEncoderPos = driveTrain.getEncPos();
        }
        driveTrain.stop();
    }
}
