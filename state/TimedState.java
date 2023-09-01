package org.firstinspires.ftc.teamcode.state;

import org.firstinspires.ftc.teamcode.modules.Tickable;
import org.firstinspires.ftc.teamcode.util.FinishCondition;

/**
 * A type of state providing timed functionality. The state completes after {@link #duration} milliseconds have passed since the state was started.
 * Some constructors have additional finish conditions that also needs to be fulfilled for the state to complete.
 */
public class TimedState extends State {
    private final long duration;
    private long startTime;

    public TimedState(String name, Runnable action, long millis) {
        this(name, action, millis, true);
    }

    public TimedState(String name, Runnable action, long millis, boolean waitForCompletion) {
        this(name, action, () -> {}, millis, () -> true, waitForCompletion);
    }

    public TimedState(String name, Runnable action, long millis, FinishCondition additionalFinishCondition) {
        this(name, action, () -> {}, millis, additionalFinishCondition);
    }

    public TimedState(String name, Runnable action, Tickable tickable, long millis, FinishCondition additionalFinishCondition) {
        this(name, action, tickable, millis, additionalFinishCondition, true);
    }

    public TimedState(String name, Runnable action, Tickable tickable, long millis, FinishCondition additionalFinishCondition, boolean waitForCompletion) {
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
