package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;

public interface Modulable {
    /**
     * Initializes a module by passing specific hardwareMap of
     * current class
     *
     * @param map Hardware map within the current context
     */
    void init(HardwareMap map);
}
