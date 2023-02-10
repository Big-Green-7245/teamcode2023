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
        parkCam.init(hardwareMap);

        // Wait for start
        intakeAndOutput.startRetraction();
        intakeAndOutput.setIntakeClawOpen(true);
        intakeAndOutput.setOutputClawOpen(false);
        intakeAndOutput.intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL);
        while (!this.isStarted()) {
            parkSpace = parkCam.parkSpace;
            intakeAndOutput.tickBeforeStart();
            TelemetryWrapper.setLine(3, "Park Space: " + parkSpace);
            TelemetryWrapper.setLine(4, "Intake LinearSlide EncoderTarget: " + intakeAndOutput.intakeSlide.getTargetPosition());
            TelemetryWrapper.setLine(5, "Intake LinearSlide Encoder: " + intakeAndOutput.intakeSlide.getCurrentPosition());
            TelemetryWrapper.setLine(6, "Output LinearSlide EncoderTarget: " + intakeAndOutput.outputSlide.getTargetPosition());
            TelemetryWrapper.setLine(7, "Output LinearSlide Encoder: " + intakeAndOutput.outputSlide.getCurrentPosition());
        }
        driveTrain.translate(SPEED, 0, 56, 0, 10);
        driveTrain.translate(SPEED, 0, 0, sideOfField ? 90 : -90, 10);
        driveTrain.translate(SPEED, sideOfField ? -9 : 9, 0, 0, 10);
        driveTrain.translate(SPEED, 0, 0, sideOfField ? 19 : -19, 10);
        driveTrain.translate(SPEED, 0, 3, 0, 10);
        intakeAndOutput.startPlaceCone(IntakeAndOutput.HIGH, 6);
        while (!this.isStopRequested() && this.isStarted() && intakeAndOutput.isRunning()) {
            intakeAndOutput.tick();
            parkCam.tick();
            TelemetryWrapper.setLineNoRender(1, "TeleOpT1 v" + programVer);

            TelemetryWrapper.setLineNoRender(3, "Current State: " + intakeAndOutput.getCurrentState().getName());
            TelemetryWrapper.setLineNoRender(4, "Intake Button: " + intakeAndOutput.intakeSlide.isElevatorBtnPressed());
            TelemetryWrapper.setLineNoRender(5, "Intake Slide Current Position: " + intakeAndOutput.intakeSlide.getCurrentPosition());
            TelemetryWrapper.setLineNoRender(6, "Intake Slide Target Position: " + intakeAndOutput.intakeSlide.getTargetPosition());
            TelemetryWrapper.setLineNoRender(7, "Intake Pivot Current Position: " + intakeAndOutput.intakePivot.getCurrentPosition());
            TelemetryWrapper.setLineNoRender(8, "Intake Pivot Target Position: " + intakeAndOutput.intakePivot.getTargetPosition());
            TelemetryWrapper.setLineNoRender(9, "Output Button: " + intakeAndOutput.outputSlide.isElevatorBtnPressed());
            TelemetryWrapper.setLineNoRender(10, "Output Slide Current Position: " + intakeAndOutput.outputSlide.getCurrentPosition());
            TelemetryWrapper.setLineNoRender(11, "Output Slide Target Position: " + intakeAndOutput.outputSlide.getTargetPosition());
            TelemetryWrapper.setLineNoRender(12, "DriveTrain Encoders: " + Arrays.toString(driveTrain.getEncPos()));

            TelemetryWrapper.setLineNoRender(13, "Intake Claw Position: " + intakeAndOutput.intakeClaw.getPosition());
            TelemetryWrapper.setLineNoRender(14, "Output Claw Position: " + intakeAndOutput.outputClaw.getPosition());
            TelemetryWrapper.render();
        }
        driveTrain.translate(SPEED, 0, -3, 0, 10);
        driveTrain.translate(SPEED, 0, 0, sideOfField ? -19 : 19, 10);
        driveTrain.translate(SPEED, sideOfField ? 9 : -9, 0, 0, 10);
        if (parkSpace == 1) {
            driveTrain.translate(SPEED, 0, sideOfField ? -24 : 24, 0, 10);
        } else if (parkSpace == 3) {
            driveTrain.translate(SPEED, 0, sideOfField ? 24 : -24, 0, 10);
        }
        //TODO face robot in direction favorable for teleop
    }

}
