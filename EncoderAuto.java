package org.firstinspires.ftc.teamcode;

// Standard Lib

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.IntakeAndOutput;
import org.firstinspires.ftc.teamcode.modules.Webcams.ParkDetectionWebcam;
import org.firstinspires.ftc.teamcode.modules.intake.IntakePivot;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

import java.util.Arrays;

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

    private ParkDetectionWebcam parkCam;
    private int counter = 0;


    public EncoderAuto(boolean sideOfField) {
        this.sideOfField = sideOfField;
    }

    @Override
    public void runOpMode() {
        TelemetryWrapper.init(telemetry, 20);
        TelemetryWrapper.setLine(1, "Autonomous v" + programVer + "\t Initializing");
        driveTrain = new DriveTrain(this);
        driveTrain.init(hardwareMap);
        intakeAndOutput = new IntakeAndOutput(sideOfField);
        intakeAndOutput.init(hardwareMap);
        parkCam = new ParkDetectionWebcam();
        parkCam.init(hardwareMap);

        // Wait for start
        intakeAndOutput.startRetraction();
        intakeAndOutput.setOutputClawOpen(false);
        while (!this.isStarted()) {
            intakeAndOutput.tickBeforeStart();
            parkCam.tick();
            parkSpace = parkCam.parkSpace;
            TelemetryWrapper.setLine(3, "Park Space: " + parkSpace);
            TelemetryWrapper.setLine(4, "Intake LinearSlide EncoderTarget: " + intakeAndOutput.intakeSlide.getTargetPosition());
            TelemetryWrapper.setLine(5, "Intake LinearSlide Encoder: " + intakeAndOutput.intakeSlide.getCurrentPosition());
            TelemetryWrapper.setLine(6, "Output LinearSlide EncoderTarget: " + intakeAndOutput.outputSlide.getTargetPosition());
            TelemetryWrapper.setLine(7, "Output LinearSlide Encoder: " + intakeAndOutput.outputSlide.getCurrentPosition());
        }
        intakeAndOutput.intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL);
        intakeAndOutput.setIntakeClawOpen(true);
        driveTrain.translate(SPEED, 0, 56, 0, 10);
        driveTrain.translate(SPEED, 0, 0, sideOfField ? 90 : -90, 10);
        driveTrain.translate(SPEED, sideOfField ? -8.5 : 7.5, 0, 0, 10);
        driveTrain.translate(SPEED, 0, 0, sideOfField ? 15.5 : -15, 10);
        driveTrain.translate(SPEED, 0, sideOfField ? 3 : 1, 0, 10);
        driveTrain.stayInPlace();
        intakeAndOutput.startPlaceCone(IntakeAndOutput.HIGH, 4);
        while (!this.isStopRequested() && this.isStarted() && intakeAndOutput.isRunning()) {
            intakeAndOutput.tick();
            TelemetryWrapper.setLineNoRender(1, "TeleOpT1 v" + programVer);

            TelemetryWrapper.setLineNoRender(3, "Current State: " + intakeAndOutput.getCurrentState().getName());
            TelemetryWrapper.setLineNoRender(4, "Intake Button: " + intakeAndOutput.intakeSlide.isElevatorBtnPressed());
            TelemetryWrapper.setLineNoRender(5, "Intake Slide Current Position: " + intakeAndOutput.intakeSlide.getCurrentPosition());
            TelemetryWrapper.setLineNoRender(6, "Intake Slide Target Position: " + intakeAndOutput.intakeSlide.getTargetPosition());
            TelemetryWrapper.setLineNoRender(7, "Intake Pivot Current Position: " + intakeAndOutput.intakePivot.getCurrentPosition());
            TelemetryWrapper.setLineNoRender(8, "Intake Pivot Target Position: " + intakeAndOutput.intakePivot.getTargetPosition());
            TelemetryWrapper.setLineNoRender(9, "Intake Pivot Current: " + intakeAndOutput.intakePivot.getCurrent());
            TelemetryWrapper.setLineNoRender(10, "Output Button: " + intakeAndOutput.outputSlide.isElevatorBtnPressed());
            TelemetryWrapper.setLineNoRender(11, "Output Slide Current Position: " + intakeAndOutput.outputSlide.getCurrentPosition());
            TelemetryWrapper.setLineNoRender(12, "Output Slide Target Position: " + intakeAndOutput.outputSlide.getTargetPosition());
            TelemetryWrapper.setLineNoRender(13, "Output Slide Current: " + intakeAndOutput.outputSlide.getCurrent());
            TelemetryWrapper.setLineNoRender(14, "DriveTrain Encoders: " + Arrays.toString(driveTrain.getEncPos()));

            TelemetryWrapper.setLineNoRender(15, "Intake Claw Position: " + intakeAndOutput.intakeClaw.getPosition());
            TelemetryWrapper.setLineNoRender(16, "Output Claw Position: " + intakeAndOutput.outputClaw.getPosition());
            TelemetryWrapper.setLineNoRender(17, "Counter: " + counter++);
            TelemetryWrapper.render();
        }
        intakeAndOutput.intakePivot.setTargetOrientation(IntakePivot.Orientation.HOLDER);
        driveTrain.translate(SPEED, 0, sideOfField ? -3 : -1, 0, 10);
        driveTrain.translate(SPEED, 0, 0, sideOfField ? -16.5 : 16, 10);
        driveTrain.translate(SPEED, sideOfField ? 9.5 : -8.5, 0, 0, 10);
        if (parkSpace == 1) {
            driveTrain.translate(SPEED, 0, sideOfField ? -24 : 24, 0, 10);
        } else if (parkSpace == 3) {
            driveTrain.translate(SPEED, 0, sideOfField ? 24 : -24, 0, 10);
        }
        driveTrain.translate(SPEED, sideOfField ? 10 : -10, 0, 0, 10);
        //TODO face robot in direction favorable for teleop
    }

}
