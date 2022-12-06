package org.firstinspires.ftc.teamcode;

// Standard Lib

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Elevator;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Pivot;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;
import java.util.Arrays;

@TeleOp(name = "TeleOpTest", group = "opmode")
public class TeleOpTest extends LinearOpMode {
    // Define attributes
    final String programVer = "1.5";
    final double speedMultiplier = 0.75;

    final double GROUND_LEVEL = 0;
    final double LOW_LEVEL = 0;
    final double MID_LEVEL = 0;
    final double HIGH_LEVEL = 0;


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

            // LinearSlide movement
            if (gp1.pressing(ButtonHelper.x))  intake.placeCone(0);
            else if (gp1.pressing(ButtonHelper.a))  intake.placeCone(1);
            else if (gp1.pressing(ButtonHelper.b))  intake.placeCone(2);
            else if (gp1.pressing(ButtonHelper.y))  intake.placeCone(3);

//            linearSlide.move(gamepad1.y ? 1 : gamepad1.a ? -0.7 : 0);
//
//            // Rotate
//            rotation.move(gamepad1.x ? 0.2 : gamepad1.b ? -0.2 : 0);

            // Move the claw
            TelemetryWrapper.setLine(3, "up" + gp1.pressed(ButtonHelper.dpad_up));
            TelemetryWrapper.setLine(4, "down" + gp1.pressed(ButtonHelper.dpad_down));
            TelemetryWrapper.setLine(5, "left" + gp1.pressed(ButtonHelper.dpad_left));
            TelemetryWrapper.setLine(6, "right" + gp1.pressed(ButtonHelper.dpad_right));

            TelemetryWrapper.setLine(7, "clawOpened: " + clawOpened);

            TelemetryWrapper.setLine(8, "CURRENTS");
            TelemetryWrapper.setLine(9, "DriveTrain Currents:" + driveTrain.getMotorCurrentsString());
            TelemetryWrapper.setLine(10, "LinearSlide Current: " + intake.elevator.getCurrent());
            TelemetryWrapper.setLine(11, "DriveTrain Encoders: " + Arrays.toString(driveTrain.getEncPos()));
            TelemetryWrapper.setLine(12, "LinearSlide Encoder: " + intake.elevator.getEncPos());
            if (gp1.pressing(ButtonHelper.dpad_up)) clawOpened = !clawOpened;
            intake.claw.clawOpen(clawOpened);

            // Display data for telemetry
            TelemetryWrapper.setLine(1, "TeleOpT1 v" + programVer);
            TelemetryWrapper.setLine(2, "Other info...");
        }

    }
}
