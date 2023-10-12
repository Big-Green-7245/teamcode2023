package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.IntakeAndOutput;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

import java.util.Arrays;

@SuppressWarnings("FieldCanBeLocal")
@TeleOp(name = "TeleOpCalib", group = "opmode")
public class TeleOpCalib extends LinearOpMode {
    // Define attributes
    private final String programVer = "1.5";
    private final double speedMultiplier = 0.75;


    // Declare modules
    private DriveTrain driveTrain;
    private ButtonHelper gp1, gp2;
    private IntakeAndOutput intakeAndOutput;


    @Override
    public void runOpMode() {
        // Robot modules initialization
        gp1 = new ButtonHelper(gamepad1);
        gp2 = new ButtonHelper(gamepad2);
        driveTrain = new DriveTrain(this);
        intakeAndOutput = new IntakeAndOutput(gp2, ButtonHelper.dpad_right);
//        intakeAndOutput.init(hardwareMap);
        driveTrain.init(hardwareMap);

        TelemetryWrapper.init(telemetry, 16);

        // Wait for start
        TelemetryWrapper.setLine(1, "TeleOp v" + programVer + "\t Press start to start >");
        waitForStart();

        while (opModeIsActive()) {
            // Update ButtonHelper
            gp1.update();
            gp2.update();

            // DriveTrain wheels
            driveTrain.move(-gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, speedMultiplier);

            // DriveEnc Calib
            if (gp1.pressing(ButtonHelper.left_stick_button)) {
                driveTrain.setModeToAllDriveMotors(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                driveTrain.setModeToAllDriveMotors(DcMotor.RunMode.RUN_USING_ENCODER);
                driveTrain.translate(0.4, 10, 0, 0, 10);
            }
            if (gp1.pressing(ButtonHelper.right_stick_button)) {
                driveTrain.setModeToAllDriveMotors(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                driveTrain.setModeToAllDriveMotors(DcMotor.RunMode.RUN_USING_ENCODER);
                driveTrain.translate(0.4, 0, 10, 0, 10);
            }

            // LinearSlide Calib
            //            if (gp1.pressing(ButtonHelper.x))  TelemetryWrapper.setLine(3, "Level 1 is: " + intakeAndOutput.intakeSlide.getEncPos());
            //            else if (gp1.pressing(ButtonHelper.a))  TelemetryWrapper.setLine(4, "Level 2 is: " + intakeAndOutput.intakeSlide.getEncPos());
            //            else if (gp1.pressing(ButtonHelper.b))  TelemetryWrapper.setLine(5, "Level 3 is: " + intakeAndOutput.intakeSlide.getEncPos());
            //            else if (gp1.pressing(ButtonHelper.y))  TelemetryWrapper.setLine(6, "Level 4 is: " + intakeAndOutput.intakeSlide.getEncPos());

            // Move LinearSlide
            intakeAndOutput.outputSlide.moveUsingEncoder(gp1.pressed(ButtonHelper.y) ? 1 : gp1.pressed(ButtonHelper.a) ? -0.7 : 0);
            TelemetryWrapper.setLine(15, "IntakeSlide Power:" + intakeAndOutput.outputSlide.getPower());

            // Move the pivot and claw
            if (gp2.pressing(ButtonHelper.dpad_up)) {
                intakeAndOutput.toggleIntakeClaw();
            }
            if (gp2.pressing(ButtonHelper.dpad_down)) {
                intakeAndOutput.toggleOutputClaw();
            }
            if (gp2.pressing(ButtonHelper.dpad_right)) {
                intakeAndOutput.intakePivot.setTargetPosition(intakeAndOutput.intakePivot.getTargetPosition() + 10);
            }
            if (gp2.pressing(ButtonHelper.dpad_left)) {
                intakeAndOutput.intakePivot.setTargetPosition(intakeAndOutput.intakePivot.getTargetPosition() - 10);
            }

            // Update Telemetry
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
    }
}
