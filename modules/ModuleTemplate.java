package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

import org.firstinspires.ftc.teamcode.util.*;

import java.util.*;

public class ModuleTemplate implements Modulable
{
    private ElapsedTime runtime = new ElapsedTime();

    public HardwareMap hwMap;

    @Override
    public void init(HardwareMap map)
    {
        hwMap = map;

        // Additional Initializations
    }

    // Additional methods for functionality
}

