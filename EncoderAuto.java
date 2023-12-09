package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Webcams.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.modules.Webcams.Webcam;
import org.firstinspires.ftc.teamcode.modules.output.LinearSlide;
import org.firstinspires.ftc.teamcode.modules.output.OutputPivot;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

@Autonomous(name = "EncoderAuto", group = "opmode")
public class EncoderAuto extends LinearOpMode {
    protected static final boolean LEFT = false;
    protected static final boolean RIGHT = true;
    /**
     * Whether the robot is on the left or right side of the alliance station.
     */
    private final boolean sideOfField;

    private DriveTrain driveTrain;
    private LinearSlide linearSlide;
    private OutputPivot pivot;
    private DcMotor intakeWheel;
    private Servo firstPixel;
    private Servo secondPixel;



    private Webcam randomizationWebcam;

    private AprilTagWebcam aprilTagWebcam;


    public EncoderAuto(boolean sideOfField) {
        this.sideOfField = sideOfField;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        TelemetryWrapper.init(telemetry, 20);

        TelemetryWrapper.setLine(1, "Autonomous " + "\t Initializing");

        driveTrain = new DriveTrain(this);
        driveTrain.init(hardwareMap);
        linearSlide = new LinearSlide("OutputSlide", 0.5);
//        pivot = new OutputPivot("OutputPivot");
        intakeWheel = hardwareMap.get(DcMotor.class, "intakeWheel");
//        firstPixel = hardwareMap.get(Servo.class, "FirstPixel");
//        secondPixel = hardwareMap.get(Servo.class, "SecondPixel");
        aprilTagWebcam = new AprilTagWebcam();
        aprilTagWebcam.init(hardwareMap);

        waitForStart();

        int loc = detectTape(randomizationWebcam);
        // Move one tile
        driveTrain.translate(0.8, 0, 24, 0, 10);

        // Turn to place position
        driveTrain.translate(0.8, 0, 0, 45*loc, 10);

        // Spit out pixel
        intakeWheel.setPower(-0.5);
        wait(1);
        intakeWheel.setPower(0);

        // Turn to neutral position
        driveTrain.translate(0.8, 0, 0, -45*loc, 10);

        // Turn to backdrop
        driveTrain.translate(0.8, 0, 0, 90, 10);

        // Drive halfway to the backdrop
        driveTrain.translate(0.8, 0, 40, 0, 10);

        // Use april tag to navigate the rest of the way
        driveTrain.move(0, 0.8, 0, 1);

        double[] currentPos = aprilTagWebcam.detectIter();
        while (currentPos[0] < 120){
            currentPos = aprilTagWebcam.detectIter();
        }

        driveTrain.stop();

        driveTrain.move(0.8 * loc, 0, 0, 1);

        while(Math.abs(currentPos[1] - 36+loc*10) < 0.3){
            currentPos = aprilTagWebcam.detectIter();
        }

        driveTrain.stopStayInPlace();




    }

    public int detectTape(Webcam webcam){
        return 0;
    }
}
