package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;


@TeleOp(name = "DriveController", group = "opmode")
    public class DriveController extends LinearOpMode {
        private final String programVer = "1.2";
        private final double speedMultiplier = 0.9d;

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
            driveTrain.move(Math.pow(-gamepad1.left_stick_x, 3), Math.pow(-gamepad1.left_stick_y, 3), Math.pow(-gamepad1.right_stick_x, 3), speedMultiplier);
            if (gp1.pressed(ButtonHelper.a)){
                driveTrain.setModeToAllDriveMotors(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                driveTrain.setModeToAllDriveMotors(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            if (gp1.pressed(ButtonHelper.dpad_up)){
                driveTrain.move(0, -1.0, 0, 0.2);
            } else if (gp1.pressed(ButtonHelper.dpad_down)){
                driveTrain.move(0, 1.0, 0, 0.2);
            }
            TelemetryWrapper .setLine(2, "Encoder Pos: " + driveTrain.getEncPos()[0]);

        }
    }
}
