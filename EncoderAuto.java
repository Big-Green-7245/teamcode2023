package org.firstinspires.ftc.teamcode;

// Standard Lib

import android.annotation.SuppressLint;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.IntakeAndOutput;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class EncoderAuto extends LinearOpMode {
    // Define attributes
    private final String programVer = "2.0";
    private static final double SPEED = 0.5;
    protected static final boolean LEFT = false;
    protected static final boolean RIGHT = true;
    private int parkSpace = 2;
    /**
     * Whether the robot is on the left or right side of the alliance station.
     */
    private final boolean sideOfField;
    private DriveTrain driveTrain;
    private IntakeAndOutput intakeAndOutput;

    //Declare model for object detection
    @SuppressLint("SdCardPath")
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/signal.tflite";

    private static final String[] LABELS = {"1 Blue", "2 Green", "3 Red"};

    private static final String VUFORIA_KEY = "AQIK9eP/////AAABmSfuIJd+0UVOt6G7lBD1wM8kaTvNDhfhZpIcg4Pa/wr6OIq8nARnVDLguWK5ae82T2dSvfb8NkfNPauXPlSmvwsWWq7zq+BfO5BfhaOsn3SNZKpBGKm8i3KMBnp48rD6oz/nQ8FATjUNv7j0W/CdhgWbete4GpVS7FC0Cr+6/iJGF1mqCEsCgiWx02sLd5NFkYqp+uKh5uiEtA/CC3T86hR/khTaX3BsnnXG9hUGh0t+lwxzL9ontudjc1ldRIhylOGnPUB0v6ht4R/X9iprB9yc1Je0D0e/Ra8ysLGROAxf8SAbuotjU2J7qmam6en3b9X0A0FrVMX1zI7W2vAzmJgrEaXZ+NmjgvsKGjI16/Qe";

    /**
     * The instance of the Vuforia localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * The instance of the TensorFlow Object Detection engine.
     */
    private TFObjectDetector tfod;

    public EncoderAuto(boolean sideOfField) {
        this.sideOfField = sideOfField;
    }

    @Override
    public void runOpMode() {
        TelemetryWrapper.init(telemetry, 16);
        TelemetryWrapper.setLine(1, "Autonomous v" + programVer + "\t Initializing");
        driveTrain = new DriveTrain(this);
        driveTrain.init(hardwareMap);
        intakeAndOutput = new IntakeAndOutput();
        intakeAndOutput.init(hardwareMap);
        //        initVuforia();
        //        initTfod();
        //        if (tfod != null) {
        //            tfod.activate();
        //            tfod.setZoom(1.0, 16.0 / 9.0);
        //        }

        // Wait for start
        intakeAndOutput.startRetraction();
        intakeAndOutput.setIntakeClawOpen(true);
        intakeAndOutput.setOutputClawOpen(false);
        while (!this.isStarted()) {
            //            int newLabel = detectLabel();
            //            if (newLabel != 0) {
            //                parkSpace = newLabel;
            //            }
            intakeAndOutput.tickBeforeStart();
            TelemetryWrapper.setLine(3, "Park Space: " + parkSpace);
            TelemetryWrapper.setLine(4, "Intake LinearSlide EncoderTarget: " + intakeAndOutput.intakeSlide.getTargetPosition());
            TelemetryWrapper.setLine(5, "Intake LinearSlide Encoder: " + intakeAndOutput.intakeSlide.getCurrentPosition());
            TelemetryWrapper.setLine(6, "Output LinearSlide EncoderTarget: " + intakeAndOutput.outputSlide.getTargetPosition());
            TelemetryWrapper.setLine(7, "Output LinearSlide Encoder: " + intakeAndOutput.outputSlide.getCurrentPosition());
        }
        driveTrain.translate(SPEED, 0, 56, 0, 10);
        driveTrain.translate(SPEED, 0, 0, sideOfField ? 90 : -90, 10);
        driveTrain.translate(SPEED, sideOfField ? -10 : 10, 0, 0, 10);
        driveTrain.translate(SPEED, 0, 0, sideOfField ? 17 : -17, 10);
        driveTrain.translate(SPEED, 0, 2, 0, 10);
        intakeAndOutput.startPlaceCone(IntakeAndOutput.HIGH, 5);
        while (this.opModeIsActive() && intakeAndOutput.isRunning()) {
            intakeAndOutput.tick();
            TelemetryWrapper.setLine(1, "TeleOpT1 v" + programVer);

            TelemetryWrapper.setLine(3, "Current State: " + intakeAndOutput.getCurrentState().getName());
            TelemetryWrapper.setLine(4, "Intake Button: " + intakeAndOutput.intakeSlide.isElevatorBtnPressed());
            TelemetryWrapper.setLine(5, "Intake Slide Current Position: " + intakeAndOutput.intakeSlide.getCurrentPosition());
            TelemetryWrapper.setLine(6, "Intake Slide Target Position: " + intakeAndOutput.intakeSlide.getTargetPosition());
            TelemetryWrapper.setLine(7, "Intake Pivot Current Position: " + intakeAndOutput.intakePivot.getCurrentPosition());
            TelemetryWrapper.setLine(8, "Intake Pivot Target Position: " + intakeAndOutput.intakePivot.getTargetPosition());
            TelemetryWrapper.setLine(9, "Output Button: " + intakeAndOutput.outputSlide.isElevatorBtnPressed());
            TelemetryWrapper.setLine(10, "Output Slide Current Position: " + intakeAndOutput.outputSlide.getCurrentPosition());
            TelemetryWrapper.setLine(11, "Output Slide Target Position: " + intakeAndOutput.outputSlide.getTargetPosition());
            TelemetryWrapper.setLine(12, "DriveTrain Encoders: " + Arrays.toString(driveTrain.getEncPos()));

            TelemetryWrapper.setLine(13, "Intake Claw Position: " + intakeAndOutput.intakeClaw.getPosition());
            TelemetryWrapper.setLine(14, "Output Claw Position: " + intakeAndOutput.outputClaw.getPosition());
        }
        driveTrain.translate(SPEED, 0, -2, 0, 10);
        driveTrain.translate(SPEED, 0, 0, sideOfField ? -17 : 17, 10);
        driveTrain.translate(SPEED, sideOfField ? 10 : -10, 0, 0, 10);
        if (parkSpace == 1) {
            driveTrain.translate(SPEED, 0, sideOfField ? -24 : 24, 0, 10);
        } else if (parkSpace == 3) {
            driveTrain.translate(SPEED, 0, sideOfField ? 24 : -24, 0, 10);
        }
        //TODO face robot in direction favorable for teleop
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        //        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        //tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }

    private int detectLabel() {
        int detectedLabel = 0;
        if (tfod != null) {
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() == 1) {
                    Recognition recognition = updatedRecognitions.get(0);
                    TelemetryWrapper.setLine(2, "Detected: " + recognition.getLabel());
                    if (recognition.getLabel().equals("2 Green")) {
                        detectedLabel = 1;
                    } else if (recognition.getLabel().equals("1 Blue")) {
                        detectedLabel = 2;
                    } else if (recognition.getLabel().equals("3 Red")) {
                        detectedLabel = 3;
                    }
                } else {
                    TelemetryWrapper.setLine(2, "Detected: None");
                }
            }
        }
        return detectedLabel;
    }
}
