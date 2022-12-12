package org.firstinspires.ftc.teamcode;

// Standard Lib

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

import java.util.Arrays;

@TeleOp(name = "TeleOpCalib", group = "opmode")
public class TeleOpCalib extends LinearOpMode {
    // Define attributes
    final String programVer = "1.5";
    final double speedMultiplier = 0.75;


    // Declare modules
    DriveTrain driveTrain;
    ButtonHelper gp1, gp2;
    //    Elevator linearSlide;
//    Pivot rotation;
//    Claw coneClaw;
    Intake intake;


    @Override
    public void runOpMode() {
        // Robot modules initialization
        driveTrain = new DriveTrain();
        gp1 = new ButtonHelper(gamepad1);
        gp2 = new ButtonHelper(gamepad2);
//        linearSlide = new Elevator();
//        rotation = new Pivot();
//        coneClaw = new Claw();
        intake = new Intake();
        intake.init(hardwareMap);
        driveTrain.init(hardwareMap);

//        driveTrain.init(hardwareMap);
//        linearSlide.init(hardwareMap);
//        rotation.init(hardwareMap);
//        coneClaw.init(hardwareMap);

        TelemetryWrapper.init(telemetry, 16);

        // Wait for start
        TelemetryWrapper.setLine(1, "TeleOpTest v" + programVer + "\t Press start to start >");
        waitForStart();

        boolean clawOpened = false;

        while (opModeIsActive()) {
            // Update ButtonHelper
            gp1.update();
            gp2.update();

            // DriveTrain wheels
            driveTrain.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, speedMultiplier);

            // LinearSlide Calib
            if (gp1.pressing(ButtonHelper.x))  TelemetryWrapper.setLine(3, "Level 1 is: " + intake.elevator.getEncPos());
            else if (gp1.pressing(ButtonHelper.a))  TelemetryWrapper.setLine(4, "Level 2 is: " + intake.elevator.getEncPos());
            else if (gp1.pressing(ButtonHelper.b))  TelemetryWrapper.setLine(5, "Level 3 is: " + intake.elevator.getEncPos());
            else if (gp1.pressing(ButtonHelper.y))  TelemetryWrapper.setLine(6, "Level 4 is: " + intake.elevator.getEncPos());

            // Move LinearSlide
            intake.elevator.move(gp1.pressed(ButtonHelper.dpad_up) ? 1 : gp1.pressed(ButtonHelper.dpad_down) ? -0.7 : 0);
            TelemetryWrapper.setLine(15, "Elevator Power:" + intake.elevator.getPower());

            // Rotate
            intake.pivot.move(gp1.pressed(ButtonHelper.dpad_right) ? 0.2 : gp1.pressed(ButtonHelper.dpad_left) ? -0.2 : 0);


            TelemetryWrapper.setLine(7, "clawOpened: " + clawOpened);

            TelemetryWrapper.setLine(8, "CURRENTS");
            TelemetryWrapper.setLine(9, "DriveTrain Currents:" + driveTrain.getMotorCurrentsString());
            TelemetryWrapper.setLine(10, "LinearSlide Current: " + intake.elevator.getCurrent());
            TelemetryWrapper.setLine(11, "DriveTrain Encoders: " + Arrays.toString(driveTrain.getEncPos()));
            TelemetryWrapper.setLine(12, "LinearSlide Encoder: " + intake.elevator.getEncPos());
            if (gp1.pressing(ButtonHelper.left_bumper)) clawOpened = !clawOpened;
            intake.claw.clawOpen(clawOpened);

            // Display data for telemetry
            TelemetryWrapper.setLine(1, "TeleOpT1 v" + programVer);
            TelemetryWrapper.setLine(2, "Other info...");
        }

    }
}
