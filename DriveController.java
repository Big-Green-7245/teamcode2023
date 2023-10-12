package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

import java.util.Arrays;


@TeleOp(name = "DriveController", group = "opmode")
    public class DriveController extends LinearOpMode {
        private final String programVer = "1.2";
        private final double speedMuliplier = 1.0;

        private ButtonHelper gp1;
        private DriveTrain driveTrain;

    @Override
    public void runOpMode(){
        TelemetryWrapper .init(telemetry, 16);
        TelemetryWrapper .setLine(1, "TeleOp v" + programVer + "\t Initizling");

        gp1= new ButtonHelper(gamepad1);
        driveTrain = new DriveTrain(this);
        driveTrain.init(hardwareMap);

        waitForStart();
        while(opModeIsActive()){
            gp1.update();
            driveTrain.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, speedMuliplier);
            if (gp1.pressed(gp1.a)){
                driveTrain.setModeToAllDriveMotors(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                driveTrain.setModeToAllDriveMotors(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            if (gp1.pressed(gp1.dpad_up)){
                driveTrain.move(0, -1.0, 0, 0.2);
            } else if (gp1.pressed(gp1.dpad_down)){
                driveTrain.move(0, 1.0, 0, 0.2);
            }
            TelemetryWrapper .setLine(2, "Encoder Pos: " + driveTrain.getEncPos()[0]);

        }
    }
}
