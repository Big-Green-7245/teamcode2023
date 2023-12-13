package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.ServoPixel;
import org.firstinspires.ftc.teamcode.modules.output.ServoOutputPivot;
import org.firstinspires.ftc.teamcode.modules.output.LinearSlide;
import org.firstinspires.ftc.teamcode.modules.output.MotorOutputPivot;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

import com.qualcomm.robotcore.util.ElapsedTime;

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
    private MotorOutputPivot pivot;
    private ServoOutputPivot servoOutputPivot;

    private ServoPixel firstPixel;
    private ServoPixel secondPixel;

    private double openPos = 0.3;

    private double closedPos = 0;

    private ElapsedTime runtime = new ElapsedTime();

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
        servoOutputPivot = new ServoOutputPivot("outputClaw");
        driveTrain.init(hardwareMap);
        outputSlide.init(hardwareMap);
//        pivot.init(hardwareMap);
        servoOutputPivot.init(hardwareMap);
        firstPixel = new ServoPixel("firstPixel");
        secondPixel = new ServoPixel("secondPixel");
        firstPixel.init(hardwareMap);
        secondPixel.init(hardwareMap);
        firstPixel.setOpen(true);
        secondPixel.setOpen(true);

        // Wait for start
        TelemetryWrapper.setLine(1, "TeleOp v" + programVer + "\t Press start to start >");
        waitForStart();

        while (opModeIsActive()) {
            TelemetryWrapper.setLine(2, "LeftSlidePos" + outputSlide.getCurrentPosition()[0]);
            TelemetryWrapper.setLine(3, "RightSlidePos" + outputSlide.getCurrentPosition()[1]);
            TelemetryWrapper.setLine(4, "PivotServoPos" + servoOutputPivot.getPosition());
            // Update ButtonHelper
            gp1.update();
            gp2.update();

            // DriveTrain wheels
            driveTrain.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, speedMultiplier);

            intakeWheel.setPower((gamepad2.right_trigger - gamepad2.left_trigger) * 0.8);

            if (Math.abs(gamepad2.left_stick_y) > 0.001) {
                outputSlide.startMoveToRelativePos((int) (-gamepad2.left_stick_y * 100));
            }
            if (gp2.pressing(ButtonHelper.b)) {
                outputSlide.startMoveToPos(1430);
            }
            if (gp2.pressing(ButtonHelper.a)) {
                outputSlide.startRetraction();
            }
            outputSlide.tick();

            if (gp2.pressing(ButtonHelper.dpad_left)) {
                servoOutputPivot.togglePivot();
            } else if (gp2.pressed(ButtonHelper.dpad_up)) {
                servoOutputPivot.movePivot(1);
            } else if (gp2.pressed(ButtonHelper.dpad_down)) {
                servoOutputPivot.movePivot(-1);
            } else if (servoOutputPivot.isFinished()) {
                servoOutputPivot.movePivot(0);
            }
            servoOutputPivot.tick();

            if (gp2.pressing(ButtonHelper.left_bumper)) {
                firstPixel.toggle();
            }
            if (gp2.pressing(ButtonHelper.right_bumper)) {
                secondPixel.toggle();
            }
        }
    }
}
