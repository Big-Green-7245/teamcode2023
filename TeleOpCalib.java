package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Intake;
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
    private Intake intake;


    @Override
    public void runOpMode() {
        // Robot modules initialization
        gp1 = new ButtonHelper(gamepad1);
        gp2 = new ButtonHelper(gamepad2);
        driveTrain = new DriveTrain();
        intake = new Intake();
        intake.init(hardwareMap);
        driveTrain.init(hardwareMap);

        TelemetryWrapper.init(telemetry, 16);

        // Wait for start
        TelemetryWrapper.setLine(1, "TeleOpTest v" + programVer + "\t Press start to start >");
        waitForStart();

        while (opModeIsActive()) {
            // Update ButtonHelper
            gp1.update();
            gp2.update();

            // DriveTrain wheels
            driveTrain.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, speedMultiplier);

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
//            if (gp1.pressing(ButtonHelper.x))  TelemetryWrapper.setLine(3, "Level 1 is: " + intake.elevator.getEncPos());
//            else if (gp1.pressing(ButtonHelper.a))  TelemetryWrapper.setLine(4, "Level 2 is: " + intake.elevator.getEncPos());
//            else if (gp1.pressing(ButtonHelper.b))  TelemetryWrapper.setLine(5, "Level 3 is: " + intake.elevator.getEncPos());
//            else if (gp1.pressing(ButtonHelper.y))  TelemetryWrapper.setLine(6, "Level 4 is: " + intake.elevator.getEncPos());

            // Move LinearSlide
            intake.elevator.moveUsingEncoder(gp1.pressed(ButtonHelper.dpad_up) ? 1 : gp1.pressed(ButtonHelper.dpad_down) ? -0.7 : 0);
            TelemetryWrapper.setLine(15, "Elevator Power:" + intake.elevator.getPower());

            // Rotate
            intake.pivot.move(gp1.pressed(ButtonHelper.dpad_right) ? 0.2 : gp1.pressed(ButtonHelper.dpad_left) ? -0.2 : 0);

            if (gp1.pressing(ButtonHelper.a)) {
                intake.toggleClaw();
            }
            if (gp1.pressing(ButtonHelper.b)) {
                intake.toggleClaw();
            }

            // Update Telemetry
            TelemetryWrapper.setLine(1, "TeleOpT1 v" + programVer);
            TelemetryWrapper.setLine(2, "Other info...");

            TelemetryWrapper.setLine(8, "CURRENTS");
            TelemetryWrapper.setLine(9, "DriveTrain Currents:" + driveTrain.getMotorCurrentsString());
            TelemetryWrapper.setLine(10, "LinearSlide Current: " + intake.elevator.getCurrent());
            TelemetryWrapper.setLine(11, "DriveTrain Encoders: " + Arrays.toString(driveTrain.getEncPos()));
            TelemetryWrapper.setLine(12, "LinearSlide Encoder: " + intake.elevator.getEncPos());
        }
    }
}
