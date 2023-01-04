package org.firstinspires.ftc.teamcode.modules;

/**
 * See <a href="https://minecraft.fandom.com/wiki/Tick">Tick</a>
 */
public interface Tickable {
    /**
     * Called after initialization is complete and before start is pressed.
     */
    default void tickBeforeStart() {
    }

    /**
     * Called when the OpMode is running.
     */
    default void tick() {
    }
}
