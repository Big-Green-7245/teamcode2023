package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

@Autonomous(name = "autoPrac", group = "opmode")
public class autoPrac extends LinearOpMode {

    private final String programVer = "1.2";
    private final double speedMuliplier = 0.6;
    private DriveTrain driveTrain;

    @Override
    public void runOpMode() {
        TelemetryWrapper.init(telemetry, 16);
        TelemetryWrapper .setLine(1, "TeleOp v" + programVer + "\t Initizling");


        driveTrain = new DriveTrain(this);
        driveTrain.init(hardwareMap);
        waitForStart();
        driveTrain.translate(speedMuliplier, 0, 20, 0, 5);
//        driveTrain.translate(speedMuliplier, 0, 0, 90, 5);
//        driveTrain.translate(speedMuliplier, 0, 20, 0, 5);
    }

}
