package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw implements Modulable {
    private final double closedPos;
    private final double openedPos;
    private final String name;
    private Servo claw;
    private boolean open;

    protected Claw(String name, double closedPos, double openedPos) {
        this.name = name;
        this.closedPos = closedPos;
        this.openedPos = openedPos;
    }

    @Override
    public void init(HardwareMap map) {
        claw = map.get(Servo.class, name);
    }

    /**
     * Makes the claw start to move towards the specified position.
     *
     * @param open open or close the claw
     */
    public void setClawOpen(boolean open) {
        this.open = open;
        startMoveClaw();
    }

    /**
     * Start to move the claw opposite to the current state.
     */
    public void toggleClaw() {
        open = !open;
        startMoveClaw();
    }

    private void startMoveClaw() {
        claw.setPosition(open ? openedPos : closedPos);
    }
}

