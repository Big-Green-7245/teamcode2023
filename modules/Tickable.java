package org.firstinspires.ftc.teamcode.modules;

/**
 * See <a href="https://minecraft.fandom.com/wiki/Tick">Tick</a>
 */
@FunctionalInterface
public interface Tickable {
    /**
     * Called after initialization is complete and before start is pressed.
     */
    default void tickBeforeStart() {
        tick();
    }

    /**
     * Called when the OpMode is running.
     */
    void tick();
}
