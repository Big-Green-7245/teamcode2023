package org.firstinspires.ftc.teamcode;

// Standard Lib

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.LinearSlide;
import org.firstinspires.ftc.teamcode.modules.Rotation;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

@TeleOp(name = "TeleOpTest", group = "opmode")
public class TeleOpTest extends LinearOpMode {
    // Define attributes
    final String programVer = "1.4";
    final double speedMultiplier = 0.75;

    // Declare modules
    DriveTrain driveTrain;
    ButtonHelper gp1, gp2;
    LinearSlide linearSlide;
    Rotation rotation;
    Claw coneClaw;


    @Override
    public void runOpMode() {
        // Robot modules initialization
        driveTrain = new DriveTrain();
        gp1 = new ButtonHelper(gamepad1);
        gp2 = new ButtonHelper(gamepad2);
        linearSlide = new LinearSlide();
        rotation = new Rotation();
        coneClaw = new Claw();

        driveTrain.init(hardwareMap);
        linearSlide.init(hardwareMap);
        rotation.init(hardwareMap);
        coneClaw.init(hardwareMap);

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
            linearSlide.move(gamepad1.y ? 1 : gamepad1.a ? -0.7 : 0);

            // Rotate
            rotation.move(gamepad1.x ? 0.2 : gamepad1.b ? -0.2 : 0);

            // Move the claw
            TelemetryWrapper.setLine(3, "up" + gp1.pressed(ButtonHelper.dpad_up));
            TelemetryWrapper.setLine(4, "down" + gp1.pressed(ButtonHelper.dpad_down));
            TelemetryWrapper.setLine(5, "left" + gp1.pressed(ButtonHelper.dpad_left));
            TelemetryWrapper.setLine(6, "right" + gp1.pressed(ButtonHelper.dpad_right));

            TelemetryWrapper.setLine(7, "clawOpened: " + clawOpened);

            TelemetryWrapper.setLine(8, "CURRENTS");
            TelemetryWrapper.setLine(9, "DriveTrain Currents:" + driveTrain.getMotorCurrentsString());
            TelemetryWrapper.setLine(10, "LinearSlide Current: " + linearSlide.getCurrent());
            if (gp1.pressing(ButtonHelper.dpad_up)) clawOpened = !clawOpened;
            coneClaw.clawOpen(clawOpened);

            // Display data for telemetry
            TelemetryWrapper.setLine(1, "TeleOpT1 v" + programVer);
            TelemetryWrapper.setLine(2, "Other info...");
        }

    }
}
