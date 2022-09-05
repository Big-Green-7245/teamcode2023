package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * TelemetryWrapper - Makes the telemetry readouts a bit more readable and manageable.
 */

public class TelemetryWrapper {
    private static Telemetry t;
    private static String[] lines;


    /**
     * Initialize telemetry for TelemetryWrapper
     * @param t Telemetry object from TeleOp
     * @param nlines Number of lines TelemetryWrapper
     * is to be initialized with.
     */
    public static void init(Telemetry t, int nlines) {
        t.clear();
        TelemetryWrapper.t = t;
        lines = new String[nlines];
        render();
    }

    /**
     * Replace all null lines as empty and render all
     * lines of data
     */
    public static void render() {
        for (int i = 0; i < lines.length; i++) {
            if (lines[i] == null)
                lines[i] = "";
            t.addData("" + i, lines[i]);
        }
        t.update();
    }

    /**
     * Set value for line and render
     * @param l Line number
     * @param message Message for line
     */
    public static void setLine(int l, String message) {
        if (l < 0 || l >= lines.length) return;
        lines[l] = message;
        render();
    }

    /**
     * Set value for line without rendering
     * @param l Line number
     * @param message Message for line
     */
    public static void setLineNoRender(int l, String message) {
        if (l < 0 || l >= lines.length) return;
        lines[l] = message;
    }

    /**
     * Reset total number of lines to new value
     * @param l New number of lines for TelemetryWrapper
     */
    public static void setLines(int l) {
        t.clear();
        lines = new String[l];
        render();
    }

    /**
     * Clear all saved data in TelemetryWrapper
     */
    public static void clear() {
        for (int i = 0; i < lines.length; i++) {
            lines[i] = "";
        }
    }
}