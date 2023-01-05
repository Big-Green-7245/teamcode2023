package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake implements Modulable, Tickable {
    private static final int[] LEVELS = new int[]{0, 9451, 14916, 21192};
    public static final int GROUND = 0;
    public static final int LOW = 1;
    public static final int MID = 2;
    public static final int HIGH = 3;

    private Claw claw;
    public Pivot pivot;
    public Elevator elevator;
    private State currentState = State.IDLE;
    private long time = 0;

    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void init(HardwareMap map) {
        elevator = new Elevator();
        pivot = new Pivot();
        claw = new Claw();
        elevator.init(map);
        pivot.init(map);
        claw.init(map);
    }

    /**
     * Start the pickup process with the pivot at the intake position.
     */
    public void startPickUp() {
    }

    /**
     * Start to place the cone with the pivot at either position.
     *
     * @param level the level to place the cone at
     */
    public void startPlaceCone(int level) {
        elevator.startMoveToPos(LEVELS[level]);
        currentState = State.ELEVATOR_MOVING_TO_PLACE_ORIENTATION;
    }

    public void togglePivot() {
        pivot.toggleTargetOrientation();
    }

    public void toggleClaw() {
        claw.toggleClaw();
    }

    @Override
    public void tickBeforeStart() {
        elevator.tickBeforeStart();
        pivot.tickBeforeStart();
    }

    @Override
    public void tick() {
        pivot.tick();
        elevator.tick();
        // Place the cone
        if (currentState == State.ELEVATOR_MOVING_TO_PLACE_ORIENTATION && elevator.isAtTargetPos()) {
            pivot.setTargetOrientation(Pivot.PLACE_ORIENTATION);
            currentState = State.PIVOT_MOVING_TO_PLACE_ORIENTATION;
        } else if (currentState == State.PIVOT_MOVING_TO_PLACE_ORIENTATION && pivot.isAtTargetPos()) {
            claw.setClawOpen(true);
            time = System.currentTimeMillis();
            currentState = State.OPENING_CLAW;
        } else if (currentState == State.OPENING_CLAW && System.currentTimeMillis() > time + 500) {
            pivot.setTargetOrientation(Pivot.INTAKE_ORIENTATION);
            currentState = State.PIVOT_MOVING_TO_INTAKE_ORIENTATION;
        } else if (currentState == State.PIVOT_MOVING_TO_INTAKE_ORIENTATION && pivot.isAtTargetPos()) {
            elevator.startMoveToGround();
            currentState = State.ELEVATOR_MOVING_TO_GROUND;
        } else if (currentState == State.ELEVATOR_MOVING_TO_GROUND && elevator.isAtTargetPos()) {
            currentState = State.IDLE;
        }
    }

    public enum State {
        IDLE,
        ELEVATOR_MOVING_TO_PLACE_ORIENTATION,
        PIVOT_MOVING_TO_PLACE_ORIENTATION,
        OPENING_CLAW,
        PIVOT_MOVING_TO_INTAKE_ORIENTATION,
        ELEVATOR_MOVING_TO_GROUND;

        public boolean isNotIdle() {
            return this != IDLE;
        }
    }
}

