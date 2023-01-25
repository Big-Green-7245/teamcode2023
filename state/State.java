package org.firstinspires.ftc.teamcode.state;

import org.firstinspires.ftc.teamcode.modules.Tickable;
import org.firstinspires.ftc.teamcode.util.ChainableBooleanSupplier;

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

    public static State createTimed(String name, Runnable action, long millis) {
        return createTimed(name, action, millis, true);
    }

    public static State createTimed(String name, Runnable action, long millis, boolean waitForCompletion) {
        return createTimed(name, action, () -> {}, millis, () -> true, waitForCompletion);
    }

    public static State createTimed(String name, Runnable action, Tickable tickable, long millis, ChainableBooleanSupplier additionalFinishCondition) {
        return createTimed(name, action, tickable, millis, additionalFinishCondition, true);
    }

    public static State createTimed(String name, Runnable action, Tickable tickable, long millis, ChainableBooleanSupplier additionalFinishCondition, boolean waitForCompletion) {
        long startTime = System.currentTimeMillis();
        return new State(name, action, tickable, additionalFinishCondition.and(() -> startTime + millis > System.currentTimeMillis()), waitForCompletion);
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
