package org.firstinspires.ftc.teamcode.state;

import org.firstinspires.ftc.teamcode.modules.Tickable;
import org.firstinspires.ftc.teamcode.util.ChainableBooleanSupplier;

public class TimedState extends State {
    private final long duration;
    private long startTime;

    public TimedState(String name, Runnable action, long millis) {
        this(name, action, millis, true);
    }

    public TimedState(String name, Runnable action, long millis, boolean waitForCompletion) {
        this(name, action, () -> {}, millis, () -> true, waitForCompletion);
    }

    public TimedState(String name, Runnable action, Tickable tickable, long millis, ChainableBooleanSupplier additionalFinishCondition) {
        this(name, action, tickable, millis, additionalFinishCondition, true);
    }

    public TimedState(String name, Runnable action, Tickable tickable, long millis, ChainableBooleanSupplier additionalFinishCondition, boolean waitForCompletion) {
        super(name, action, tickable, additionalFinishCondition, waitForCompletion);
        this.duration = millis;
    }

    @Override
    public boolean isFinished() {
        return startTime + duration < System.currentTimeMillis() && super.isFinished();
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        super.run();
    }
}
