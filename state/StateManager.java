package org.firstinspires.ftc.teamcode.state;

import org.firstinspires.ftc.teamcode.modules.Tickable;

import java.util.*;

/**
 * A class providing management and control for running a sequence of states.
 *
 * @see Builder
 */
public class StateManager implements Tickable, Runnable {
    private int loopCount;
    private int currentLoopCount;
    private final List<State> states;
    private final Set<State> needsTicking = new HashSet<>();
    private int currentStateIndex;
    private boolean running;

    private StateManager(List<State> states) {
        Objects.requireNonNull(states, "states cannot be null");
        this.loopCount = 1;
        this.states = states;
        currentStateIndex = 0;
    }

    public State getCurrentState() {
        return states.get(currentStateIndex);
    }

    /**
     * Call this when {@link #isRunning() not running} to start running at a custom state index.
     *
     * @param currentStateIndex the current state and the index to start running at
     */
    public void setCurrentStateIndex(int currentStateIndex) {
        this.currentStateIndex = currentStateIndex;
    }

    public boolean isRunning() {
        return running;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    /**
     * Starts running the state manager.
     */
    @Override
    public void run() {
        running = true;
        states.get(currentStateIndex).run();
    }

    /**
     * Ticks all states that needs ticking, checks if the current state is finished, and starts running the next state.
     */
    @Override
    public void tick() {
        Iterator<State> iterator = needsTicking.iterator();
        while (iterator.hasNext()) {
            State state = iterator.next();
            state.tick();
            if (state.isFinished()) {
                iterator.remove();
            }
        }
        State currentState = states.get(currentStateIndex);
        currentState.tick();
        if (running) {
            if (!currentState.isFinished()) {
                if (!currentState.isWaitForCompletion()) {
                    needsTicking.add(currentState);
                } else {
                    return;
                }
            }
            if (++currentStateIndex >= states.size()) {
                if (++currentLoopCount < loopCount) {
                    reset();
                } else {
                    stopAndReset();
                    return;
                }
            }
            states.get(currentStateIndex).run();
        }
    }

    /**
     * Resets the state manager to the first state. Does not stop running the state manager.
     */
    public void reset() {
        currentStateIndex = 0;
    }

    /**
     * Stops running the state manager and resets it to the first state.
     */
    public void stopAndReset() {
        running = false;
        currentLoopCount = 0;
        reset();
    }

    /**
     * A class for building a {@link StateManager}. Call {@link #addState(State)} to add states to the state manager.
     * The state manager will start running at the first state added when {@link StateManager#run()} is called.
     */
    public static class Builder {
        private final List<State> states = new ArrayList<>();

        @SuppressWarnings("UnusedReturnValue")
        public Builder addState(State state) {
            states.add(state);
            return this;
        }

        public StateManager build() {
            return new StateManager(Collections.unmodifiableList(states));
        }
    }
}
