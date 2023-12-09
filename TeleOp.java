package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.output.Claw;
import org.firstinspires.ftc.teamcode.modules.output.LinearSlide;
import org.firstinspires.ftc.teamcode.modules.output.OutputPivot;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

@SuppressWarnings("FieldCanBeLocal")
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "opmode")
public class TeleOp extends LinearOpMode {
    // Define attributes
    private final String programVer = "2.0";
    private final double speedMultiplier = 0.95;

    // Declare modules
    private ButtonHelper gp1, gp2;
    private DriveTrain driveTrain;
    private DcMotor intakeWheel;
    private LinearSlide outputSlide;
    private OutputPivot pivot;
    private Claw outputClaw;

    @Override
    public void runOpMode() {
        TelemetryWrapper.init(telemetry, 20);

        TelemetryWrapper.setLine(1, "TeleOp v" + programVer + "\t Initializing");

        // Robot modules initialization
        gp1 = new ButtonHelper(gamepad1);
        gp2 = new ButtonHelper(gamepad2);
        driveTrain = new DriveTrain(this);
        intakeWheel = hardwareMap.get(DcMotor.class, "intakeWheel");
        outputSlide = new LinearSlide("linearSlide", 0.5);
//        pivot = new OutputPivot("outputPivot");
        outputClaw = new Claw("outputClaw");
        driveTrain.init(hardwareMap);
        outputSlide.init(hardwareMap);
//        pivot.init(hardwareMap);
        outputClaw.init(hardwareMap);


        // Wait for start
        TelemetryWrapper.setLine(1, "TeleOp v" + programVer + "\t Press start to start >");
        waitForStart();

        while (opModeIsActive()) {
            TelemetryWrapper.setLine(2, "LeftSlidePos" + outputSlide.getCurrentPosition()[0]);
            TelemetryWrapper.setLine(3, "RightSlidePos" + outputSlide.getCurrentPosition()[1]);
            TelemetryWrapper.setLine(4, "PivotServoPos" + outputClaw.getPosition());
            // Update ButtonHelper
            gp1.update();
            gp2.update();

            // DriveTrain wheels
            driveTrain.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, speedMultiplier);

            outputSlide.moveUsingEncoder(gamepad2.left_stick_y * 0.5);
            intakeWheel.setPower((gamepad2.right_trigger - gamepad2.left_trigger) * 0.8);
//            pivot.moveUsingEncoder(-gamepad2.right_stick_y * 0.5);

//            if (gp2.pressing(ButtonHelper.dpad_up)) {
//                outputClaw.toggleClaw();
//            }

            if (gp2.pressed(ButtonHelper.dpad_up)) {
                outputClaw.moveClaw(0.3);
            }else if (gp2.pressed(ButtonHelper.dpad_down)){
                outputClaw.moveClaw(-0.3);
            }else{
                outputClaw.moveClaw(0);
            }
        }
    }
}
