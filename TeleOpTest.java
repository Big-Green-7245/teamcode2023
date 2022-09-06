package org.firstinspires.ftc.teamcode;

// Standard Lib
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;

import java.util.*;

// Class import
import org.firstinspires.ftc.teamcode.util.*;
import org.firstinspires.ftc.teamcode.modules.*;


@TeleOp(name="TeleOpTest", group="opmode")
public class TeleOpTest extends LinearOpMode {
    // Define attributes
    final String programVer = "1.0";
    final double speedMultiplier = 1.0;

    // Declare modules
    DriveTrain driveTrain;
    ButtonHelper gp1, gp2;

    @Override
    public void runOpMode(){
        // Robot modules initialization
        driveTrain = new DriveTrain();
        gp1 = new ButtonHelper(gamepad1);
        gp2 = new ButtonHelper(gamepad2);

        driveTrain.init(hardwareMap);
        TelemetryWrapper.init(telemetry, 16);

        // Wait for start
        TelemetryWrapper.setLine(1, "TeleOpT1 v" + programVer + "\t Press start to start >");
        waitForStart();

        while(opModeIsActive()) {
            // Update ButtonHelper
            gp1.update();
            gp2.update();

            // DriveTrain wheels
            driveTrain.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, speedMultiplier);

            // Other control methods
            if(gp2.pressing(gp2.x)) {
                // Do something
            }

            // Display data for telemetry
            TelemetryWrapper.setLine(1, "TeleOpT1 v" + programVer);
            TelemetryWrapper.setLine(2, "Other info...");
        }

    }
}
