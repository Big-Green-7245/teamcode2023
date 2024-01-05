package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.output.LinearSlide;
import org.firstinspires.ftc.teamcode.modules.output.MotorOutputPivot;
import org.firstinspires.ftc.teamcode.modules.output.ServoOutputPivot;
import org.firstinspires.ftc.teamcode.modules.output.ServoToggle;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

@SuppressWarnings("FieldCanBeLocal")
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "opmode")
public class TeleOp extends LinearOpMode {
    // Define attributes
    private final String programVer = "2.0";
    private final double speedMultiplier = 0.99;

    // Declare modules
    private ButtonHelper gp1, gp2;
    private DriveTrain driveTrain;
    private DcMotor intakeWheel;
    private LinearSlide outputSlide;
    private MotorOutputPivot pivot;
    private ServoOutputPivot servoOutputPivot;

    private ServoToggle firstPixel;
    private ServoToggle secondPixel;

    private ServoToggle planeLaunch;


    private boolean[][] lockStates = new boolean[4][];

    private int currentLockState = 0;

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
        servoOutputPivot = new ServoOutputPivot("outputClaw");
        driveTrain.init(hardwareMap);
        outputSlide.init(hardwareMap);
        servoOutputPivot.init(hardwareMap);
        lockStates[0] = new boolean[]{false, false};
        lockStates[1] = new boolean[]{true, true};
        lockStates[2] = new boolean[]{false, true};
        firstPixel = new ServoToggle();
        secondPixel = new ServoToggle();
        firstPixel.init(hardwareMap, "firstPixel", 0, 0.3, false);
        secondPixel.init(hardwareMap, "secondPixel", 0, 0.3, false);
        firstPixel.setAction(false);
        secondPixel.setAction(false);

        planeLaunch = new ServoToggle();
        planeLaunch.init(hardwareMap, "launcher", 0.3, 0, true);


        // Wait for start
        TelemetryWrapper.setLine(1, "TeleOp v" + programVer + "\t Press start to start >");
        waitForStart();

        while (opModeIsActive()) {
            TelemetryWrapper.setLine(2, "Output Box Lock State " + currentLockState);
            TelemetryWrapper.setLine(3, "LeftSlidePos" + outputSlide.getCurrentPosition()[0]);
            TelemetryWrapper.setLine(4, "RightSlidePos" + outputSlide.getCurrentPosition()[1]);
            // Update ButtonHelper
            gp1.update();
            gp2.update();

            // DriveTrain wheels
            driveTrain.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, speedMultiplier);

            intakeWheel.setPower((gamepad2.right_trigger - gamepad2.left_trigger) * 0.8);

            if (Math.abs(gamepad2.left_stick_y) > 0.001) {
                outputSlide.startMoveToRelativePos((int) -gamepad2.left_stick_y * 100);
            }
            if (gp2.pressing(ButtonHelper.b)) {
                outputSlide.startMoveToPos(1430);
            }
            if (gp2.pressing(ButtonHelper.a)) {
                outputSlide.startRetraction();
            }
            outputSlide.tick();

//            if (gp2.pressing(ButtonHelper.dpad_left)) {
//                servoOutputPivot.togglePivot();
//            } else
            if (gp1.pressed(ButtonHelper.right_bumper)) {
                servoOutputPivot.setPower(1);
            } else if (gp1.pressed(ButtonHelper.left_bumper) && !servoOutputPivot.intakeButton.isPressed()) {
                servoOutputPivot.setPower(-1);
            } else if (servoOutputPivot.isFinished()) {
                servoOutputPivot.setPower(0);
            }
            if (gp2.pressed(ButtonHelper.dpad_up)) {
                servoOutputPivot.togglePivot();
            }
            servoOutputPivot.tick();

            if (gp2.pressing(ButtonHelper.left_bumper)) {
                firstPixel.toggleAction();
                currentLockState = 0;
            }

            if (gp2.pressing(ButtonHelper.right_bumper)) {
                secondPixel.toggleAction();
                currentLockState = 0;
            }

            if (gp2.pressing(ButtonHelper.dpad_right)) {
                currentLockState = (currentLockState + 1) % 3;
                firstPixel.setAction(lockStates[currentLockState][0]);
                secondPixel.setAction(lockStates[currentLockState][1]);
            }

            if (gp2.pressing(ButtonHelper.x)) {
                planeLaunch.toggleAction();
            }
        }
    }
}
