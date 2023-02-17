package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.modules.Webcams.PoleAlignWebcam;
import org.firstinspires.ftc.teamcode.modules.intake.IntakeClaw;
import org.firstinspires.ftc.teamcode.modules.intake.IntakePivot;
import org.firstinspires.ftc.teamcode.modules.intake.IntakeSlide;
import org.firstinspires.ftc.teamcode.modules.output.OutputClaw;
import org.firstinspires.ftc.teamcode.modules.output.OutputSlide;
import org.firstinspires.ftc.teamcode.state.ButtonState;
import org.firstinspires.ftc.teamcode.state.State;
import org.firstinspires.ftc.teamcode.state.StateManager;
import org.firstinspires.ftc.teamcode.state.TimedState;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.EncoderConstants;

public class IntakeAndOutput implements Modulable, Tickable {
    private static final double SLIDE_ENCODER = EncoderConstants.YELLOW_JACKET_1150.getPulsesPerRevolution();
    private static final int AUTO_INTAKE_SLIDE_EXTENDED_POS = (int) (3.7195462154 * SLIDE_ENCODER);
    private static final int[] OUTPUT_LEVELS = new int[]{0, (int) (1.2 * SLIDE_ENCODER), (int) (3.75 * SLIDE_ENCODER), (int) (6.4 * SLIDE_ENCODER)};
    private static final int[][] CONE_STACK_LEVELS = new int[][]{{(int) (3.8422912405 * SLIDE_ENCODER), 840}, {(int) (3.6711921146 * SLIDE_ENCODER), 900}, {(int) (3.6711921146 * SLIDE_ENCODER), 940}, {(int) (3.7195462154 * SLIDE_ENCODER), 1000}, {(int) (3.7195462154 * SLIDE_ENCODER), 1050}, {(int) (3.7195462154 * SLIDE_ENCODER), 1050}}; // Last value is dummy value; it is not actually picking up a cone.
    public static final int GROUND = 0;
    public static final int LOW = 1;
    public static final int MID = 2;
    public static final int HIGH = 3;
    public IntakeClaw intakeClaw;
    public IntakePivot intakePivot;
    public IntakeSlide intakeSlide;
    public OutputClaw outputClaw;
    public OutputSlide outputSlide;
    private StateManager stateManager;
    private final boolean autonomous;
    private final ButtonHelper gamepad;
    private final int placeConfirmationButton;
    private int intakeConeStackIndex;
    private int intakeSlideTarget = AUTO_INTAKE_SLIDE_EXTENDED_POS;
    private int targetLevel = HIGH;

    private PoleAlignWebcam poleAlignCam;

    public IntakeAndOutput() {
        this(true, null, 0);
    }

    public IntakeAndOutput(ButtonHelper gamepad, int placeConfirmationButton) {
        this(false, gamepad, placeConfirmationButton);
    }

    private IntakeAndOutput(boolean autonomous, ButtonHelper gamepad, int placeConfirmationButton) {
        this.autonomous = autonomous;
        this.gamepad = gamepad;
        this.placeConfirmationButton = placeConfirmationButton;
    }

    public void setCurrentAsIntakeSlideTarget() {
        this.intakeSlideTarget = intakeSlide.getCurrentPosition();
    }

    @Override
    public void init(HardwareMap map) {
        intakeClaw = new IntakeClaw();
        intakePivot = new IntakePivot();
        intakeSlide = new IntakeSlide();
        outputClaw = new OutputClaw();
        outputSlide = new OutputSlide();
        intakeClaw.init(map);
        intakePivot.init(map);
        intakeSlide.init(map);
        outputClaw.init(map);
        outputSlide.init(map);
        //        poleAlignCam = new PoleAlignWebcam();
        //        poleAlignCam.init(map);
        stateManager = initConeStates();
    }

    private StateManager initConeStates() {
        StateManager.Builder builder = new StateManager.Builder();
        builder.addState(new State("Output slide extending", () -> outputSlide.startMoveToPos(OUTPUT_LEVELS[targetLevel]))); // Intake slide, output slide, and intake pivot start at the same time. Do not wait for any to finish.
        builder.addState(new State("Intake slide extending", () -> intakeSlide.startMoveToPos(autonomous ? CONE_STACK_LEVELS[intakeConeStackIndex][0] : intakeSlideTarget)));
        builder.addState(new State("Intake pivot lowering", () -> intakePivot.setTargetPosition(autonomous ? CONE_STACK_LEVELS[intakeConeStackIndex][1] - 200 : IntakePivot.Orientation.INTAKE.getPosition() - 200), intakeSlide)); // Wait for intake slide to finish so the pivot can be lowered further.
        builder.addState(new State("Intake pivot lowering to cone stack", () -> intakePivot.setTargetPosition(autonomous ? CONE_STACK_LEVELS[intakeConeStackIndex][1] : IntakePivot.Orientation.INTAKE.getPosition())));
        if (!autonomous) {
            builder.addState(new ButtonState("Waiting for place cone confirmation", gamepad, placeConfirmationButton));
        }
        builder.addState(new State("Waiting for intake pivot to lower to position", intakePivot.and(outputSlide))); // Wait for intake pivot and output slide to finish.
        builder.addState(new State("Output slide retracting", () -> outputSlide.startRetraction(), outputSlide, outputSlide, false));
        builder.addState(new TimedState("Output claw opening", () -> outputClaw.setClawOpen(true), 160, false));
        builder.addState(new TimedState("Intake claw closing part 1", () -> intakeClaw.setClawOpen(false), 160));
        builder.addState(new TimedState("Output claw closing", () -> outputClaw.setPosition(0.05), 110, false));
        builder.addState(new TimedState("Intake claw closing part 2", () -> intakeClaw.setClawOpen(false), 140));
        builder.addState(new TimedState("Intake pivot raising from cone stack", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL), 200, autonomous));
        builder.addState(new State("Intake slide retracting", () -> intakeSlide.startRetraction(), intakeSlide, outputSlide));
        builder.addState(new TimedState("Output claw opening", () -> outputClaw.setClawOpen(true), 110, intakeSlide));
        builder.addState(new State("Intake pivot lowering to holder", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.HOLDER), intakePivot));
        builder.addState(new TimedState("Output claw closing", () -> outputClaw.setClawOpen(false), 160, false));
        builder.addState(new TimedState("Intake claw opening at holder", () -> intakeClaw.setPosition(0.1), 250));
        builder.addState(new State("Intake pivot raising from holder", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL), intakePivot));
        builder.addState(new TimedState("Intake claw opening at vertical", () -> intakeClaw.setClawOpen(true), 0, false));
        if (autonomous) {
            builder.addState(new State("Decrementing encoder targets to account for decremented cone stack", () -> intakeConeStackIndex++, () -> true));
        }
        return builder.build();
    }

    //Not Ready
    //    private StateManager initPoleAlignStates(){
    //
    //        StateManager.Builder builder = new StateManager.Builder();
    //        builder.addState(new TimedState("Waiting for pole detection", ()->{}, poleAlignCam, 3000, poleAlignCam));
    //        builder.addState(new TimedState("Aligning robot to pole", () -> {}, poleAlignCam, 5000, poleAlignCam));
    //        return builder.build();
    //    }

    public boolean isRunning() {
        return stateManager.isRunning();
    }

    public State getCurrentState() {
        return stateManager.getCurrentState();
    }

    public void startRetraction() {
        intakeSlide.startRetraction();
        outputSlide.startRetraction();
    }

    public void startPickupCone() {
        if (!stateManager.isRunning()) {
            setCurrentAsIntakeSlideTarget();
            stateManager.setCurrentStateIndex(4);
            stateManager.run();
        }
    }

    /**
     * Starts to place the cone. Uses {@link #intakeSlideTarget} as intake slide position.
     *
     * @param targetLevel the level to place the cone at
     */
    public void startPlaceCone(int targetLevel) {
        startPlaceCone(targetLevel, 1);
    }

    /**
     * Starts to place the cone.
     *
     * @param targetLevel the level to place the cone at
     * @param loopCount   the number of times to repeat placing cones
     */
    public void startPlaceCone(int targetLevel, int loopCount) {
        if (!stateManager.isRunning()) {
            this.targetLevel = targetLevel;
            stateManager.setLoopCount(loopCount);
            stateManager.run();
        }
    }

    public void setIntakeClawOpen(boolean open) {
        intakeClaw.setClawOpen(open);
    }

    public void setOutputClawOpen(boolean open) {
        outputClaw.setClawOpen(open);
    }

    public void toggleIntakeClaw() {
        intakeClaw.toggleClaw();
    }

    public void toggleOutputClaw() {
        outputClaw.toggleClaw();
    }

    @Override
    public void tickBeforeStart() {
        intakeSlide.tickBeforeStart();
        outputSlide.tickBeforeStart();
    }

    @Override
    public void tick() {
        stateManager.tick();
        intakeSlide.tick();
        outputSlide.tick();
        //        if (intakePivot.getCurrent() > 100000) {
        //            intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL);
        //            stateManager.stopAndReset();
        //        }
        //        if (intakeSlide.getCurrent() > 100000) {
        //            intakeSlide.startMoveToPos(intakeSlide.getCurrentPosition());
        //            stateManager.stopAndReset();
        //        }
        //        if (outputSlide.getCurrent() > 100000) {
        //            outputSlide.startMoveToPos(outputSlide.getCurrentPosition());
        //            stateManager.stopAndReset();
        //        }
    }
}

