package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake implements Modulable, Tickable {
    private static final int GROUND_LEVEL_POSITION = 0;
    private static final int LOW_LEVEL_POSITION = 0;
    private static final int MID_LEVEL_POSITION = 0;
    private static final int HIGH_LEVEL_POSITION = 0;

    private static final int[] LEVELS = new int[]{0, 9451, 14916, 21192};
    private static final int SAFE_ROT_LEVEL = 0;

    public static final int GROUND = 0;
    public static final int LOW = 1;
    public static final int MID = 2;
    public static final int HIGH = 3;

    private final ElapsedTime runtime = new ElapsedTime();

    public HardwareMap hwMap;
    public Claw claw;
    public Pivot pivot;
    public Elevator elevator;
    private int targetLevel = 0;
    private State currentState = State.IDLE;
    private long time = 0;

    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void init(HardwareMap map) {
        hwMap = map;
        elevator = new Elevator();
        pivot = new Pivot();
        claw = new Claw();
        elevator.init(hwMap);
        pivot.init(hwMap);
        claw.init(hwMap);

        // Additional Initializations
    }

    /**
     * Start the pickup process with the pivot at the intake position.
     */
    public void startPickUp() {
        elevator.moveToGround();
        //wait
        claw.clawOpen(false);
    }

    /**
     * Start to place the cone with the pivot at either position.
     *
     * @param level the level to place the cone at
     */
    public void startPlaceCone(int level) {
        elevator.moveToPos(LEVELS[targetLevel]);
        currentState = State.ELEVATOR_MOVING_TO_SAFE_ROT_LEVEL;
        targetLevel = level;
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
        if (currentState == State.ELEVATOR_MOVING_TO_SAFE_ROT_LEVEL /*&& elevator.elevator.getCurrentPosition() > SAFE_ROT_LEVEL*/) {
            pivot.setIntakeOrientation(Pivot.PLACE_ORIENTATION);
            currentState = State.PIVOT_AND_ELEVATOR_MOVING_TO_PLACE_ORIENTATION;
        } else if (currentState == State.PIVOT_AND_ELEVATOR_MOVING_TO_PLACE_ORIENTATION && pivot.isAtTargetPos() && elevator.isAtTargetPos()) {
            claw.clawOpen(true);
            time = System.currentTimeMillis();
            currentState = State.OPENING_CLAW;
        } else if (currentState == State.OPENING_CLAW && System.currentTimeMillis() > time + 500) {
            pivot.setIntakeOrientation(Pivot.INTAKE_ORIENTATION);
            currentState = State.PIVOT_MOVING_TO_INTAKE_ORIENTATION;
        } else if (currentState == State.PIVOT_MOVING_TO_INTAKE_ORIENTATION && pivot.isAtTargetPos()) {
            elevator.moveToGround();
            currentState = State.ELEVATOR_MOVING_TO_GROUND;
        } else if (currentState == State.ELEVATOR_MOVING_TO_GROUND && elevator.isAtTargetPos()) {
            currentState = State.IDLE;
        }
    }

    public enum State {
        IDLE,
        ELEVATOR_MOVING_TO_SAFE_ROT_LEVEL,
        PIVOT_AND_ELEVATOR_MOVING_TO_PLACE_ORIENTATION,
        OPENING_CLAW,
        PIVOT_MOVING_TO_INTAKE_ORIENTATION,
        ELEVATOR_MOVING_TO_GROUND
    }
}

