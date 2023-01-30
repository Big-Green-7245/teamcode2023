package org.firstinspires.ftc.teamcode.util;

import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
@FunctionalInterface
public interface ChainableBooleanSupplier extends BooleanSupplier {
    default ChainableBooleanSupplier and(BooleanSupplier other) {
        return () -> this.getAsBoolean() && other.getAsBoolean();
    }

    default ChainableBooleanSupplier or(BooleanSupplier other) {
        return () -> this.getAsBoolean() || other.getAsBoolean();
    }

    default ChainableBooleanSupplier negate() {
        return () -> !this.getAsBoolean();
    }
}
