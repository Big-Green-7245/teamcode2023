package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
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

public class IntakeAndOutput implements Modulable, Tickable {
    private static final int AUTO_INTAKE_SLIDE_EXTENDED_POS = 2000;
    private static final int[] OUTPUT_LEVELS = new int[]{0, 1500, 2250, 3450};
    private static final int[][] CONE_STACK_LEVELS = new int[][]{{2066, 840}, {1974, 900}, {1974, 940}, {2000, 1000}, {2000, 1050}, {2000, 1050}}; // Last value is dummy value; it is not actually picking up a cone.
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
        stateManager = initStates();
    }

    private StateManager initStates() {
        StateManager.Builder builder = new StateManager.Builder();
        builder.addState(new State("Intake slide extending", () -> intakeSlide.startMoveToPos(autonomous ? CONE_STACK_LEVELS[intakeConeStackIndex][0] : intakeSlideTarget), intakeSlide, intakeSlide, false));
        builder.addState(new State("Output slide extending", () -> outputSlide.startMoveToPos(OUTPUT_LEVELS[targetLevel]), outputSlide, outputSlide.and(intakePivot)));
        builder.addState(new State("Intake pivot lowering to cone stack", () -> intakePivot.setTargetPosition(autonomous ? CONE_STACK_LEVELS[intakeConeStackIndex][1] : IntakePivot.Orientation.INTAKE.getPosition()), intakePivot, false));
        if (!autonomous) {
            builder.addState(new ButtonState("Waiting for place cone confirmation", gamepad, placeConfirmationButton));
        }
        builder.addState(new TimedState("Output claw opening", () -> outputClaw.setClawOpen(true), 0, intakePivot));
        builder.addState(new State("Output slide retracting", () -> outputSlide.startRetraction(), outputSlide, outputSlide, false));
        builder.addState(new TimedState("Intake claw closing", () -> intakeClaw.setClawOpen(false), 500));
        builder.addState(new TimedState("Output claw closing", () -> outputClaw.setPosition(0.05), 500, false));
        builder.addState(new TimedState("Intake pivot raising from cone stack", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL), 250, autonomous));
        builder.addState(new TimedState("Intake slide retracting", () -> intakeSlide.startRetraction(), intakeSlide, 500, intakeSlide.and(outputSlide)));
        builder.addState(new TimedState("Output claw opening", () -> outputClaw.setClawOpen(true), 250));
        builder.addState(new State("Intake pivot lowering to holder", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.HOLDER), intakePivot));
        builder.addState(new TimedState("Intake claw opening at holder", () -> intakeClaw.setClawOpen(true), 500));
        builder.addState(new State("Intake pivot raising from holder", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL), intakePivot, false));
        builder.addState(new TimedState("Output claw closing", () -> outputClaw.setClawOpen(false), 500));
        if (autonomous) {
            builder.addState(new State("Decrementing encoder targets to account for decremented cone stack", () -> intakeConeStackIndex++, () -> true));
        }
        return builder.build();
    }

    public boolean isRunning() {
        return stateManager.isRunning();
    }

    public State getCurrentState() {
        return stateManager.getCurrentState();
    }

    public void startRetraction() {
        intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL);
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
    }
}

