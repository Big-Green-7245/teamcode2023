package org.firstinspires.ftc.teamcode;

// Standard Lib

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Elevator;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Pivot;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;
import java.util.Arrays;

@Autonomous(name = "EncoderAuto", group = "opmode")
public class EncoderAuto extends LinearOpMode {
    // Define attributes
    final String programVer = "1.5";
    private int parkSpace = 0;
    final double speed = 0.5;
    private DriveTrain driveTrain;
    private Intake intake;

    public void init(HardwareMap hwMap){
        driveTrain = new DriveTrain();
        driveTrain.init(hwMap);
        intake = new Intake();
        intake.init(hwMap);
    }
    @Override
    public void runOpMode() {
        if (parkSpace == 1) {
            driveTrain.translate(speed, -22.75 - 11.375, 0, 0, 10);
            driveTrain.translate(speed, 0, 11.375 / 3, 0, 10);
            intake.placeCone(1);
            driveTrain.translate(speed, 0, -11.375 / 3, 0, 10);
            driveTrain.translate(speed, 11.375, 0, 0, 10);
            driveTrain.translate(speed, 0, 22.75, 0, 10);
        }else if (parkSpace == 2){
            driveTrain.translate(speed, 11.375, 0, 0, 10);
            driveTrain.translate(speed, 0, 11.375 / 3, 0, 10);
            intake.placeCone(1);
            driveTrain.translate(speed, 0, -11.375 / 3, 0, 10);
            driveTrain.translate(speed, -11.375, 0, 0, 10);
            driveTrain.translate(speed, 0, 22.75, 0, 10);
        }else if (parkSpace == 3){
            driveTrain.translate(speed, 11.375, 0, 0, 10);
            driveTrain.translate(speed, 0, 11.375 / 3, 0, 10);
            intake.placeCone(1);
            driveTrain.translate(speed, 0, -11.375 / 3, 0, 10);
            driveTrain.translate(speed, -11.375, 0, 0, 10);
            driveTrain.translate(speed, 0, 22.75, 0, 10);
            driveTrain.translate(speed, 22.75, 0, 0, 10);
        }

    }
}
