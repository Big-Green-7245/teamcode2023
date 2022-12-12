package org.firstinspires.ftc.teamcode;

// Standard Lib

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Intake;

@Autonomous(name = "EncoderAuto", group = "opmode")
public class EncoderAuto extends LinearOpMode {
    // Define attributes
    final String programVer = "1.5";
    private static final double SPEED = 0.5;
    private int parkSpace = 0;
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
            driveTrain.translate(SPEED, -22.75 - 11.375, 0, 0, 10);
            driveTrain.translate(SPEED, 0, 11.375 / 3, 0, 10);
            intake.placeCone(1);
            driveTrain.translate(SPEED, 0, -11.375 / 3, 0, 10);
            driveTrain.translate(SPEED, 11.375, 0, 0, 10);
            driveTrain.translate(SPEED, 0, 22.75, 0, 10);
        } else if (parkSpace == 2) {
            driveTrain.translate(SPEED, 11.375, 0, 0, 10);
            driveTrain.translate(SPEED, 0, 11.375 / 3, 0, 10);
            intake.placeCone(1);
            driveTrain.translate(SPEED, 0, -11.375 / 3, 0, 10);
            driveTrain.translate(SPEED, -11.375, 0, 0, 10);
            driveTrain.translate(SPEED, 0, 22.75, 0, 10);
        } else if (parkSpace == 3) {
            driveTrain.translate(SPEED, 11.375, 0, 0, 10);
            driveTrain.translate(SPEED, 0, 11.375 / 3, 0, 10);
            intake.placeCone(1);
            driveTrain.translate(SPEED, 0, -11.375 / 3, 0, 10);
            driveTrain.translate(SPEED, -11.375, 0, 0, 10);
            driveTrain.translate(SPEED, 0, 22.75, 0, 10);
            driveTrain.translate(SPEED, 22.75, 0, 0, 10);
        }

    }
}
