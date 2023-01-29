package org.firstinspires.ftc.teamcode.state;

import org.firstinspires.ftc.teamcode.modules.Tickable;

import java.util.function.BooleanSupplier;

public class State implements Tickable, Runnable {
    private final String name;
    private final Runnable action;
    private final Tickable tickable;

    private final BooleanSupplier finishCondition;
    private final boolean waitForCompletion;

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
