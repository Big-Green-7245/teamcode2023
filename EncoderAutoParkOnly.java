package org.firstinspires.ftc.teamcode;

// Standard Lib

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.IntakeAndOutput;
import org.firstinspires.ftc.teamcode.modules.intake.IntakePivot;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

@SuppressWarnings("FieldCanBeLocal")
public class EncoderAutoParkOnly extends LinearOpMode {
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


    public EncoderAutoParkOnly(boolean sideOfField) {
        this.sideOfField = sideOfField;
    }

    @Override
    public void runOpMode() {
        TelemetryWrapper.init(telemetry, 20);
        TelemetryWrapper.setLine(1, "Autonomous v" + programVer + "\t Initializing");
        driveTrain = new DriveTrain(this);
        driveTrain.init(hardwareMap);
        intakeAndOutput = new IntakeAndOutput(sideOfField);
//        intakeAndOutput.init(hardwareMap);
//        parkCam = new ParkDetectionWebcam();
//        parkCam.init(hardwareMap);

        // Wait for start
        intakeAndOutput.startRetraction();
        intakeAndOutput.setOutputClawOpen(false);
        while (!this.isStarted()) {
            intakeAndOutput.tickBeforeStart();
            TelemetryWrapper.setLine(3, "Park Space: " + parkSpace);
            TelemetryWrapper.setLine(4, "Intake LinearSlide EncoderTarget: " + intakeAndOutput.intakeSlide.getTargetPosition());
            TelemetryWrapper.setLine(5, "Intake LinearSlide Encoder: " + intakeAndOutput.intakeSlide.getCurrentPosition());
            TelemetryWrapper.setLine(6, "Output LinearSlide EncoderTarget: " + intakeAndOutput.outputSlide.getTargetPosition());
            TelemetryWrapper.setLine(7, "Output LinearSlide Encoder: " + intakeAndOutput.outputSlide.getCurrentPosition());
        }
        intakeAndOutput.setIntakeClawOpen(true);
        intakeAndOutput.intakePivot.setTargetOrientation(IntakePivot.Orientation.HOLDER);
        driveTrain.translate(SPEED, 2, 32, 0, 10);
        if (parkSpace == 1) {
            driveTrain.translate(SPEED, 0, sideOfField ? -24 : 24, 0, 10);
        } else if (parkSpace == 3) {
            driveTrain.translate(SPEED, 0, sideOfField ? 24 : -24, 0, 10);
        }
        //TODO face robot in direction favorable for teleop
    }

}
