package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;

public class HybridAuto extends LinearOpMode {

    private DriveTrain driveTrain;
    protected static final boolean LEFT = false;
    protected static final boolean RIGHT = true;
    /**
     * Whether the robot is on the left or right side of the alliance station.
     */
    private final boolean sideOfField;

    public HybridAuto(boolean sideOfField) {
        this.sideOfField = sideOfField;
    }

    @Override
    public void runOpMode() {
        DriveTrain drivetrain = new DriveTrain(this);
        driveTrain.init(hardwareMap);

    }
}
