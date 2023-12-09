package org.firstinspires.ftc.teamcode.modules.output;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.teamcode.modules.Modulable;

public class Claw implements Modulable {
    public static final double CLOSED_POS = 0;
    private static final double OPENED_POS = 0.5;
    private final String name;
    protected CRServo claw;

    private ServoController controller;
    private boolean open;

    public Claw(String name) {
        this.name = name;
    }

    public double getPosition() {
        return controller.getServoPosition(0);
    }

    public void setPosition(double position) {
        controller.setServoPosition(0, position);;
    }


    @Override
    public void init(HardwareMap map) {
        claw = map.get(CRServo.class, name);
        claw.setDirection(CRServo.Direction.FORWARD);
        controller = claw.getController();
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
        controller.setServoPosition(0, open ? OPENED_POS : CLOSED_POS);
    }
    public void moveClaw(double power){
        claw.setPower(power);
    }
}

