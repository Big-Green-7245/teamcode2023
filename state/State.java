package org.firstinspires.ftc.teamcode.state;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.modules.Tickable;

import java.util.function.BooleanSupplier;

/**
 * A state representing an action which can be blocking or non-blocking, specified by {@link #waitForCompletion}.
 * {@link #action} is run at the start of the state, and {@link #tickable} should be run as many times as possibly before the state completes.
 * The state completes when {@link #finishCondition} returns true.
 */
public class State implements Tickable, Runnable {
    private final String name;
    /**
     * The action to run at the start of the state. This should not block and should call methods such as {@link DcMotor#setTargetPosition(int)} to start moving a motor.
     */
    private final Runnable action;
    /**
     * Run as many times as possible while the state is not completed. This can be used to update things. Should not block.
     */
    private final Tickable tickable;
    /**
     * The condition that needs to be fulfilled for the state to complete. Should not block.
     */
    private final BooleanSupplier finishCondition;
    /**
     * Whether to wait for this state to complete before starting the next state.
     */
    private final boolean waitForCompletion;


    public State(String name, Runnable action) {
        this(name, action, () -> true);
    }


    public State(String name, BooleanSupplier finishCondition) {
        this(name, () -> {}, finishCondition);
    }

    public State(String name, Runnable action, BooleanSupplier finishCondition) {
        this(name, action, finishCondition, true);
    }

    public State(String name, Runnable action, BooleanSupplier finishCondition, boolean waitForCompletion) {
        this(name, action, () -> {}, finishCondition, waitForCompletion);
    }

    public State(String name, Runnable action, Tickable tickable, BooleanSupplier finishCondition) {
        this(name, action, tickable, finishCondition, true);
    }

    public State(String name, Runnable action, Tickable tickable, BooleanSupplier finishCondition, boolean waitForCompletion) {
        this.name = name;
        this.action = action;
        this.tickable = tickable;
        this.finishCondition = finishCondition;
        this.waitForCompletion = waitForCompletion;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isWaitForCompletion() {
        return waitForCompletion;
    }

    @Override
    public void tick() {
        tickable.tick();
    }

    public boolean isFinished() {
        return finishCondition.getAsBoolean();
    }

    @Override
    public void run() {
        action.run();
    }
}
