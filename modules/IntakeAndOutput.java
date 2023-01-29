package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.modules.intake.IntakeClaw;
import org.firstinspires.ftc.teamcode.modules.intake.IntakePivot;
import org.firstinspires.ftc.teamcode.modules.intake.IntakeSlide;
import org.firstinspires.ftc.teamcode.modules.output.OutputClaw;
import org.firstinspires.ftc.teamcode.modules.output.OutputSlide;
import org.firstinspires.ftc.teamcode.state.State;
import org.firstinspires.ftc.teamcode.state.StateManager;
import org.firstinspires.ftc.teamcode.state.TimedState;

public class IntakeAndOutput implements Modulable, Tickable {
    private static final int AUTO_INTAKE_SLIDE_EXTENDED_POS = 2000;
    private static final int[] OUTPUT_LEVELS = new int[]{0, 1500, 2250, 3300};
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
    private int intakeSlideTarget = AUTO_INTAKE_SLIDE_EXTENDED_POS;
    private int targetLevel = HIGH;

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
        //TODO Wait for driver confirmation to start placing
        builder.addState(new State("Intake slide extending", () -> intakeSlide.startMoveToPos(intakeSlideTarget), intakeSlide, intakeSlide, false));
        builder.addState(new State("Output slide extending", () -> outputSlide.startMoveToPos(OUTPUT_LEVELS[targetLevel]), outputSlide, outputSlide.and(intakePivot)));
        builder.addState(new State("Intake pivot lowering to cone stack", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.INTAKE), intakePivot, false));
        //TODO Wait for driver confirmation to place cone
        builder.addState(new TimedState("Output claw opening", () -> outputClaw.setClawOpen(true), 500, false));
        //TODO Wait for driver confirmation to pickup cone
        builder.addState(new TimedState("Intake claw closing", () -> intakeClaw.setClawOpen(false), 500));
        builder.addState(new State("Output slide retracting", () -> outputSlide.startRetraction(), outputSlide, outputSlide, false));
        builder.addState(new TimedState("Intake pivot raising from cone stack", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL), 250));
        builder.addState(new TimedState("Intake slide retracting", () -> intakeSlide.startRetraction(), intakeSlide, 1000, intakeSlide.and(outputSlide)));
        builder.addState(new State("Intake pivot lowering to holder", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.HOLDER), intakePivot));
        builder.addState(new TimedState("Intake claw opening at holder", () -> intakeClaw.setClawOpen(true), 500));
        builder.addState(new State("Intake pivot raising from holder", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL), intakePivot, false));
        builder.addState(new TimedState("Output claw closing", () -> outputClaw.setClawOpen(false), 500));
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

    public void startPlaceCone(int targetLevel, int intakeSlideTarget, int loopCount) {
        this.intakeSlideTarget = intakeSlideTarget;
        startPlaceCone(targetLevel, loopCount);
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

