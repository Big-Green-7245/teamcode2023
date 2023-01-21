package org.firstinspires.ftc.teamcode.state;

import org.firstinspires.ftc.teamcode.modules.Tickable;

import java.util.*;

public class StateManager implements Tickable {
    private int loopCount;
    private int currentLoopCount;
    private final List<State> states;
    private final List<State> needsTicking = new ArrayList<>();
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

    public boolean isRunning() {
        return running;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    public void start() {
        running = true;
        states.get(currentStateIndex).run();
    }

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
        if (running && (currentState.isFinished() || !currentState.isWaitForCompletion())) {
            if (!currentState.isWaitForCompletion()) {
                needsTicking.add(currentState);
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

    public void reset() {
        currentStateIndex = 0;
    }

    public void stopAndReset() {
        running = false;
        currentLoopCount = 0;
        reset();
    }

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
