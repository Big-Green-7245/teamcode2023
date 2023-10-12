package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.IntakeAndOutput;
import org.firstinspires.ftc.teamcode.modules.intake.IntakePivot;
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
        TelemetryWrapper.init(telemetry, 20);

        TelemetryWrapper.setLine(1, "TeleOp v" + programVer + "\t Initializing");

        // Robot modules initialization
        gp1 = new ButtonHelper(gamepad1);
        gp2 = new ButtonHelper(gamepad2);
        driveTrain = new DriveTrain(this);
        intakeAndOutput = new IntakeAndOutput(gp2, ButtonHelper.dpad_right);
        driveTrain.init(hardwareMap);
//        intakeAndOutput.init(hardwareMap);

        // Wait for start
        TelemetryWrapper.setLine(1, "TeleOp v" + programVer + "\t Press start to start >");

        // Move intake and output to starting position while waiting for start
        intakeAndOutput.startRetraction();
        while (!isStarted()) {
            intakeAndOutput.tickBeforeStart();
            TelemetryWrapper.setLine(2, "Intake Button: " + intakeAndOutput.intakeSlide.isElevatorBtnPressed());
            TelemetryWrapper.setLine(3, "Intake Slide Current Position" + intakeAndOutput.intakeSlide.getCurrentPosition());
            TelemetryWrapper.setLine(4, "Intake Slide Target Position" + intakeAndOutput.intakeSlide.getTargetPosition());
            TelemetryWrapper.setLine(5, "Output Button: " + intakeAndOutput.outputSlide.isElevatorBtnPressed());
            TelemetryWrapper.setLine(6, "Output Slide Current Position" + intakeAndOutput.outputSlide.getCurrentPosition());
            TelemetryWrapper.setLine(7, "Output Slide Target Position" + intakeAndOutput.outputSlide.getTargetPosition());
        }

        intakeAndOutput.intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL);
        intakeAndOutput.setIntakeClawOpen(true);

        while (opModeIsActive()) {
            // Update ButtonHelper
            gp1.update();
            gp2.update();

            // Tick modules
            intakeAndOutput.tick();

            // DriveTrain wheels
            driveTrain.move(-gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, speedMultiplier);

            if (Math.abs(gamepad2.left_stick_y) > 0.1) {
                int targetPosition = intakeAndOutput.intakeSlide.getTargetPosition() + (int) (-gamepad2.left_stick_y * 10);
                if (targetPosition >= 10) {
                    intakeAndOutput.intakeSlide.startMoveToPos(targetPosition);
                } else {
                    intakeAndOutput.intakeSlide.startRetraction();
                }
            }
            if (Math.abs(gamepad2.right_stick_y) > 0.1) {
                int targetPosition = intakeAndOutput.outputSlide.getTargetPosition() + (int) (-gamepad2.right_stick_y * 10);
                if (targetPosition >= 10) {
                    intakeAndOutput.outputSlide.startMoveToPos(targetPosition);
                } else {
                    intakeAndOutput.outputSlide.startRetraction();
                }
            }

            if (gp2.pressing(ButtonHelper.left_bumper)) {
                intakeAndOutput.intakeSlide.startRetraction();
            }
            if (gp2.pressing(ButtonHelper.right_bumper)) {
                intakeAndOutput.outputSlide.startRetraction();
            }

            // Move intake pivot
            if (gp1.pressed(ButtonHelper.dpad_left)) {
                intakeAndOutput.intakePivot.setTargetPosition(intakeAndOutput.intakePivot.getTargetPosition() + 10);
            }
            if (gp1.pressed(ButtonHelper.dpad_right)) {
                intakeAndOutput.intakePivot.setTargetPosition(intakeAndOutput.intakePivot.getTargetPosition() - 10);
            }
            if (gp1.pressing(ButtonHelper.x)) {
                intakeAndOutput.intakePivot.setTargetOrientation(IntakePivot.Orientation.INTAKE);
            }
            if (gp1.pressing(ButtonHelper.y)) {
                intakeAndOutput.intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL);
            }
            if (gp1.pressing(ButtonHelper.b)) {
                intakeAndOutput.intakePivot.setTargetOrientation(IntakePivot.Orientation.HOLDER);
            }
            if(gp1.pressing(ButtonHelper.a)){
                intakeAndOutput.intakePivot.stopAndReset();
            }

            // LinearSlide movement
            if (intakeAndOutput.intakePivot.getTargetPosition() >= IntakePivot.Orientation.VERTICAL.getPosition() && intakeAndOutput.outputClaw.getPosition() == Claw.CLOSED_POS) {
                if (gp2.pressing(ButtonHelper.x)) intakeAndOutput.startPlaceCone(IntakeAndOutput.GROUND);
                else if (gp2.pressing(ButtonHelper.a)) intakeAndOutput.startPlaceCone(IntakeAndOutput.LOW);
                else if (gp2.pressing(ButtonHelper.b)) intakeAndOutput.startPlaceCone(IntakeAndOutput.MID);
                else if (gp2.pressing(ButtonHelper.y)) intakeAndOutput.startPlaceCone(IntakeAndOutput.HIGH);
            }

            // Move the pivot and claw
            if (gp2.pressing(ButtonHelper.dpad_left)) {
                intakeAndOutput.toggleIntakeClaw();
            }
            if (gp2.pressing(ButtonHelper.dpad_down)) {
                intakeAndOutput.toggleOutputClaw();
            }
            if (gp2.pressing(ButtonHelper.dpad_up)) {
                intakeAndOutput.intakeClaw.setClawOpen(false);
                intakeAndOutput.startPickupCone();
            }
            if (gp2.pressing(ButtonHelper.dpad_right)) {
                intakeAndOutput.setCurrentAsIntakeSlideTarget();
            }

            // Move the robot around a point
            if (gp1.pressed(ButtonHelper.dpad_left)) {
                driveTrain.rotateAroundCenter(5, -0.2);
            }
            if (gp1.pressed(ButtonHelper.dpad_right)) {
                driveTrain.rotateAroundCenter(5, 0.2);
            }

            // Update Telemetry
            TelemetryWrapper.setLineNoRender(1, "TeleOpT1 v" + programVer);

            TelemetryWrapper.setLineNoRender(3, "Current State: " + intakeAndOutput.getCurrentState().getName());
            TelemetryWrapper.setLineNoRender(4, "Intake Button: " + intakeAndOutput.intakeSlide.isElevatorBtnPressed());
            TelemetryWrapper.setLineNoRender(5, "Intake Slide Current Position: " + intakeAndOutput.intakeSlide.getCurrentPosition());
            TelemetryWrapper.setLineNoRender(6, "Intake Slide Target Position: " + intakeAndOutput.intakeSlide.getTargetPosition());
            TelemetryWrapper.setLineNoRender(7, "Intake Slide Current: " + intakeAndOutput.intakeSlide.getCurrent());
            TelemetryWrapper.setLineNoRender(8, "Intake Pivot Current Position: " + intakeAndOutput.intakePivot.getCurrentPosition());
            TelemetryWrapper.setLineNoRender(9, "Intake Pivot Target Position: " + intakeAndOutput.intakePivot.getTargetPosition());
            TelemetryWrapper.setLineNoRender(10, "Output Button: " + intakeAndOutput.outputSlide.isElevatorBtnPressed());
            TelemetryWrapper.setLineNoRender(11, "Output Slide Current Position: " + intakeAndOutput.outputSlide.getCurrentPosition());
            TelemetryWrapper.setLineNoRender(12, "Output Slide Target Position: " + intakeAndOutput.outputSlide.getTargetPosition());
            TelemetryWrapper.setLineNoRender(13, "Output Slide Current: " + intakeAndOutput.outputSlide.getCurrent());
            TelemetryWrapper.setLineNoRender(14, "DriveTrain Encoders: " + Arrays.toString(driveTrain.getEncPos()));

            TelemetryWrapper.setLineNoRender(15, "Intake Claw Position: " + intakeAndOutput.intakeClaw.getPosition());
            TelemetryWrapper.setLineNoRender(16, "Output Claw Position: " + intakeAndOutput.outputClaw.getPosition());
            TelemetryWrapper.render();
        }
    }
}
