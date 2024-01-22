package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Navigation;
import org.firstinspires.ftc.teamcode.modules.Webcams.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.modules.Webcams.PlaceDetectionWebcam;
import org.firstinspires.ftc.teamcode.modules.output.LinearSlide;
import org.firstinspires.ftc.teamcode.modules.output.MotorOutputPivot;
import org.firstinspires.ftc.teamcode.modules.output.ServoToggle;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group = "opmode")
public class Autonomous extends LinearOpMode {
    protected static final boolean LEFT = false;
    protected static final boolean RIGHT = true;
    /**
     * Whether the robot is on the left or right side of the alliance station.
     */
    private final boolean sideOfField;

    private DriveTrain driveTrain;
    private LinearSlide linearSlide;
    private MotorOutputPivot pivot;
    private DcMotor intakeWheel;
    private ServoToggle firstPixel;
    private ServoToggle secondPixel;


    private PlaceDetectionWebcam randomizationWebcam;

    private Navigation navigation;


    public Autonomous(boolean sideOfField) {
        this.sideOfField = sideOfField;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        TelemetryWrapper.init(telemetry, 20);

        TelemetryWrapper.setLine(1, "Autonomous " + "\t Initializing");

        driveTrain = new DriveTrain(this);
        driveTrain.init(hardwareMap);
        linearSlide = new LinearSlide("linearSlide", 0.5);
        linearSlide.init(hardwareMap);
        pivot = new MotorOutputPivot("outputPivot", 0.3);
        pivot.init(hardwareMap);
        intakeWheel = hardwareMap.get(DcMotor.class, "intakeWheel");
        firstPixel = new ServoToggle();
        secondPixel = new ServoToggle();
        firstPixel.init(hardwareMap, "firstPixel", 0, 0.3, false);
        secondPixel.init(hardwareMap, "secondPixel", 0, 0.3, false);
        firstPixel.setAction(true);
        secondPixel.setAction(true);
//        randomizationWebcam = new PlaceDetectionWebcam();
//        randomizationWebcam.init(hardwareMap, "Blue.tflite");
//        int loc = 0;
//        while (opModeInInit()){
//            loc = detectTape(randomizationWebcam);
//        }
//        randomizationWebcam.stop();
        navigation = new Navigation(new double[]{12, 66}, 270, this, hardwareMap);
        pivot.startMoveToPos(false);
        while (opModeInInit()) {
            linearSlide.tickBeforeStart();
            pivot.tickBeforeStart();
            TelemetryWrapper.setLine(7, "x: " + navigation.getCurrentPos()[0] + " y: " + navigation.getCurrentPos()[1]);
            TelemetryWrapper.setLine(8, "Gyro bearing: " + navigation.getGyroBearing());
        }

        navigation.MoveToPosDirect(new double[]{12, 32});
        navigation.setBearing(0);
        while(opModeIsActive() && !navigation.tagCam.isDetecting){
            navigation.tagCam.detectIter(navigation.getGyroBearing());
            TelemetryWrapper.setLine(10, "Waiting for detection...");
        }
        navigation.MoveToPosDirect(new double[]{28.5, 32});
        navigation.setBearing(0);
        intakeWheel.setPower(-0.8);
        sleep(1000);
        intakeWheel.setPower(0);
        navigation.MoveToPosDirect(new double[]{45.5, 32});
        navigation.setBearing(0);
        linearSlide.startMoveToPos(1430);
        while(!linearSlide.isFinished()){
            linearSlide.tick();
        }
        sleep(500);
        pivot.startMoveToPos(true);
        while(pivot.isBusy()){
            pivot.tick();
        }
        sleep(1000);
        firstPixel.setAction(false);
        secondPixel.setAction(false);

        navigation.tagCam.close();

        while (opModeIsActive()) ;

        navigation.tagCam.close();

//        // Turn around
//        driveTrain.translate(0.8, 0, -32, 0, 10);
//
//        if (loc == PlaceDetectionWebcam.LEFT){
//            driveTrain.translate(0.8, 0, 0, -90, 10);
//            double[] currentPos = aprilTagWebcam.detectIter();
//            TelemetryWrapper.setLine(8, "Is detecting :" + aprilTagWebcam.isDetecting);
//            TelemetryWrapper.setLine(9, "x: " + currentPos[0] + ", " + "y: " + currentPos[1] + ", "+ "z: " + currentPos[2]);
//            while(!aprilTagWebcam.isDetecting) {
//                currentPos = aprilTagWebcam.detectIter();
//                TelemetryWrapper.setLine(9, "x: " + currentPos[0] + ", " + "y: " + currentPos[1] + ", "+ "z: " + currentPos[2]);
//                if (isStopRequested()){
//                    break;
//                }
//            }
//            while (currentPos[0] < 50.25){
//                currentPos = aprilTagWebcam.detectIter();
//                TelemetryWrapper.setLine(2, "x: " + currentPos[0] + ", " + "y: " + currentPos[1] + ", "+ "z: " + currentPos[2]);
//                driveTrain.move(0, 0.5, 0, 1.0);
//                if (isStopRequested()){
//                    break;
//                }
//            }
//            intakeWheel.setPower(-0.8);
//            sleep(1000);
//            intakeWheel.setPower(0);
//
//            while (Math.abs(currentPos[1] - (35.41-6*loc))< 0.5){
//                currentPos = aprilTagWebcam.detectIter();
//                TelemetryWrapper.setLine(9, "x: " + currentPos[0] + ", " + "y: " + currentPos[1] + ", "+ "z: " + currentPos[2]);
//                driveTrain.move(-0.5*loc, 0, 0, 1.0);
//                if (isStopRequested()){
//                    break;
//                }
//            }
//            sleep(5000);
//
////            driveTrain.translate(0.8, 0, -16, 0, 10);
//        }

//        // Turn around
//        driveTrain.translate(0.8, 0, 0, 180, 10);
////
////
//        // Move one tile
//        driveTrain.translate(0.8, 0, 17, 0, 10);
////
//        // Turn to place position
//        driveTrain.translate(0.8, 0, 0, 55*loc, 10);
//
//        // Spit out pixel
//        driveTrain.translate(0.8, 0, 10, 0, 10);
//        driveTrain.translate(0.8, 0, -5, 0, 10);
//        intakeWheel.setPower(-0.8);
//        sleep(1000);
//        intakeWheel.setPower(0);
//
//        // Turn to neutral position
//        driveTrain.translate(0.8, 0, 0, -45*loc, 10);
//
//        // Turn to backdrop
//        driveTrain.translate(0.8, 0, 0, 90, 10);
//
//        // Drive halfway to the backdrop
//        driveTrain.translate(0.8, 0, 40, 0, 10);
//
//        // Use april tag to navigate the rest of the way
//        driveTrain.move(0, 0.8, 0, 1);

//        double[] currentPos = aprilTagWebcam.detectIter();
//        while (currentPos[0] < 120){
//            currentPos = aprilTagWebcam.detectIter();
//        }
//
//        driveTrain.stop();
//
//        driveTrain.move(0.8 * loc, 0, 0, 1);
//
//        while(Math.abs(currentPos[1] - 36+loc*10) < 0.3){
//            currentPos = aprilTagWebcam.detectIter();
//        }
//
//        driveTrain.stopStayInPlace();


    }

    public int detectTape(PlaceDetectionWebcam webcam) {
        webcam.detect();
        return webcam.placeSpace;
    }
}
