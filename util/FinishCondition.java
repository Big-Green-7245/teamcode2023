package org.firstinspires.ftc.teamcode.util;

import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
@FunctionalInterface
public interface FinishCondition extends BooleanSupplier {
    /**
     * Do not override, override {@link #isFinished()} instead.
     */
    @Deprecated
    default boolean getAsBoolean() {
        return this.isFinished();
    }

    boolean isFinished();

    default FinishCondition and(BooleanSupplier other) {
        return () -> this.isFinished() && other.getAsBoolean();
    }

    default FinishCondition or(BooleanSupplier other) {
        return () -> this.isFinished() || other.getAsBoolean();
    }

    default FinishCondition negate() {
        return () -> !this.isFinished();
    }
}
