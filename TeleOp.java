package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.IntakeAndOutput;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

import java.util.Arrays;

@SuppressWarnings("FieldCanBeLocal")
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "opmode")
public class TeleOp extends LinearOpMode {
    // Define attributes
    private final String programVer = "2.0";
    private final double speedMultiplier = 0.55;

    // Declare modules
    private ButtonHelper gp1, gp2;
    private DriveTrain driveTrain;
    private IntakeAndOutput intakeAndOutput;


    @Override
    public void runOpMode() {
        TelemetryWrapper.init(telemetry, 16);

        TelemetryWrapper.setLine(1, "TeleOp v" + programVer + "\t Initializing");

        // Robot modules initialization
        gp1 = new ButtonHelper(gamepad1);
        gp2 = new ButtonHelper(gamepad2);
        driveTrain = new DriveTrain();
        intakeAndOutput = new IntakeAndOutput();
        driveTrain.init(hardwareMap);
        intakeAndOutput.init(hardwareMap);

        // Wait for start
        TelemetryWrapper.setLine(1, "TeleOp v" + programVer + "\t Press start to start >");

        // Move intakeSlide and intakePivot to starting position while waiting for start
        intakeAndOutput.outputSlide.startRetraction();
        while (!isStarted()) {
            intakeAndOutput.tickBeforeStart();
        }

        while (opModeIsActive()) {
            // Update ButtonHelper
            gp1.update();
            gp2.update();

            // Tick modules
            intakeAndOutput.tick();

            // DriveTrain wheels
            driveTrain.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, speedMultiplier);

            intakeAndOutput.outputSlide.moveUsingEncoder(-gamepad2.left_stick_y);

            // LinearSlide movement
            if (gp2.pressing(ButtonHelper.x)) intakeAndOutput.startPlaceCone(IntakeAndOutput.GROUND);
            else if (gp2.pressing(ButtonHelper.a)) intakeAndOutput.startPlaceCone(IntakeAndOutput.LOW);
            else if (gp2.pressing(ButtonHelper.b)) intakeAndOutput.startPlaceCone(IntakeAndOutput.MID);
            else if (gp2.pressing(ButtonHelper.y)) intakeAndOutput.startPlaceCone(IntakeAndOutput.HIGH);


            // Move the pivot and claw
            if (gp2.pressing(ButtonHelper.dpad_down)) {
                intakeAndOutput.toggleIntakeClaw();
            }
            if (gp2.pressing(ButtonHelper.dpad_up)) {
                intakeAndOutput.toggleOutputClaw();
            }
            if (gp2.pressing(ButtonHelper.dpad_right)) {
                intakeAndOutput.intakePivot.setTargetPosition(intakeAndOutput.intakePivot.getPosition() + 0.05);
            }
            if (gp2.pressing(ButtonHelper.dpad_left)) {
                intakeAndOutput.intakePivot.setTargetPosition(intakeAndOutput.intakePivot.getPosition() - 0.05);
            }

            // Update Telemetry
            TelemetryWrapper.setLine(1, "TeleOpT1 v" + programVer);
            TelemetryWrapper.setLine(2, "Other info...");

            TelemetryWrapper.setLine(3, "up" + gp1.pressed(ButtonHelper.dpad_up));
            TelemetryWrapper.setLine(4, "down" + gp1.pressed(ButtonHelper.dpad_down));
            TelemetryWrapper.setLine(5, "left" + gp1.pressed(ButtonHelper.dpad_left));
            TelemetryWrapper.setLine(6, "right" + gp1.pressed(ButtonHelper.dpad_right));

            TelemetryWrapper.setLine(8, "CURRENTS");
            TelemetryWrapper.setLine(9, "DriveTrain Currents:" + driveTrain.getMotorCurrentsString());
            TelemetryWrapper.setLine(10, "LinearSlide Current: " + intakeAndOutput.outputSlide.getCurrent());
            TelemetryWrapper.setLine(11, "DriveTrain Encoders: " + Arrays.toString(driveTrain.getEncPos()));
            TelemetryWrapper.setLine(12, "LinearSlide EncoderTarget: " + intakeAndOutput.outputSlide.getTargetPosition());
            TelemetryWrapper.setLine(14, "LinearSlide Encoder: " + intakeAndOutput.outputSlide.getCurrentPosition());
            TelemetryWrapper.setLine(13, "Current State: " + intakeAndOutput.getCurrentState().getName());
        }
    }
}
