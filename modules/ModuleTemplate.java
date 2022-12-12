package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ModuleTemplate implements Modulable
{
    private final ElapsedTime runtime = new ElapsedTime();

    public HardwareMap hwMap;

    @Override
    public void init(HardwareMap map)
    {
        hwMap = map;

        // Additional Initializations
    }

    // Additional methods for functionality
}

