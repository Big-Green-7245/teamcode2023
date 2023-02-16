package org.firstinspires.ftc.teamcode.util;

@SuppressWarnings("unused")
public enum EncoderConstants {
    YELLOW_JACKET_6000(28), YELLOW_JACKET_1150(145.1), YELLOW_JACKET_435(384.5), YELLOW_JACKET_312(537.7), YELLOW_JACKET_223(751.8),YELLOW_JACKET_84(1993.6),YELLOW_JACKET_60(2786.2), YELLOW_JACKET_30(5281.1);
    private final double pulsesPerRevolution;

    EncoderConstants(double pulsesPerRevolution) {
        this.pulsesPerRevolution = pulsesPerRevolution;
    }

    public double getPulsesPerRevolution() {
        return pulsesPerRevolution;
    }
}
