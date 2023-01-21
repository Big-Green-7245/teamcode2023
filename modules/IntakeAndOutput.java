package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.modules.intake.IntakeClaw;
import org.firstinspires.ftc.teamcode.modules.intake.IntakePivot;
import org.firstinspires.ftc.teamcode.modules.intake.IntakeSlide;
import org.firstinspires.ftc.teamcode.modules.output.OutputClaw;
import org.firstinspires.ftc.teamcode.modules.output.OutputSlide;
import org.firstinspires.ftc.teamcode.state.State;
import org.firstinspires.ftc.teamcode.state.StateManager;

public class IntakeAndOutput implements Modulable, Tickable {
    private static final int INTAKE_SLIDE_EXTENDED_POS = 20000;
    private static final int[] OUTPUT_LEVELS = new int[]{0, 9451, 14916, 21200};
    public static final int GROUND = 0;
    public static final int LOW = 1;
    public static final int MID = 2;
    public static final int HIGH = 3;
    private IntakeClaw intakeClaw;
    public IntakePivot intakePivot;
    public IntakeSlide intakeSlide;

    private OutputClaw outputClaw;
    public OutputSlide outputSlide;
    private StateManager stateManager;
    private int targetLevel = HIGH;

    @Override
    public void init(HardwareMap map) {
        stateManager = initStates();
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
    }

    private StateManager initStates() {
        StateManager.Builder builder = new StateManager.Builder();
        //TODO Wait for driver confirmation to start placing
        builder.addState(new State("Intake slide extending", () -> intakeSlide.startMoveToPos(INTAKE_SLIDE_EXTENDED_POS), intakeSlide, intakeSlide, false));
        builder.addState(new State("Output slide extending", () -> outputSlide.startMoveToPos(OUTPUT_LEVELS[targetLevel]), outputSlide, outputSlide));
        builder.addState(State.createTimed("Intake pivot lowering to cone stack", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.INTAKE), 500, false));
        //TODO Wait for driver confirmation to place cone
        builder.addState(State.createTimed("Output claw opening", () -> outputClaw.setClawOpen(true), 500, false));
        //TODO Wait for driver confirmation to pickup cone
        builder.addState(State.createTimed("Intake claw closing", () -> intakeClaw.setClawOpen(false), 500));
        builder.addState(new State("Output slide retracting", () -> outputSlide.startRetraction(), outputSlide, outputSlide, false));
        builder.addState(State.createTimed("Intake pivot raising from cone stack", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL), 500));
        builder.addState(State.createTimed("Intake slide retracting", () -> intakeSlide.startRetraction(), intakeSlide, 1000, intakeSlide.and(outputSlide)));
        builder.addState(State.createTimed("Intake pivot lowering to holder", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.HOLDER), 500));
        builder.addState(State.createTimed("Intake claw opening at holder", () -> intakeClaw.setClawOpen(true), 500));
        builder.addState(State.createTimed("Intake pivot raising from holder", () -> intakePivot.setTargetOrientation(IntakePivot.Orientation.VERTICAL), 500, false));
        builder.addState(State.createTimed("Output claw closing", () -> outputClaw.setClawOpen(false), 500));
        return builder.build();
    }

    public boolean isRunning(){
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
     * Start to place the cone.
     *
     * @param level the level to place the cone at
     */
    public void startPlaceCone(int level) {
        startPlaceCone(level, 1);
    }

    public void startPlaceCone(int level, int loopCount) {
        if (!stateManager.isRunning()) {
            targetLevel = level;
            stateManager.setLoopCount(loopCount);
            stateManager.start();
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
    }
}

