package org.firstinspires.ftc.teamcode.modules.output;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.Modulable;

public class ServoToggle implements Modulable  {
    private double IDLE_POS = 0;
    private double ACTION_POS = 0.3;
    private String name;
    protected Servo pivot;
    private boolean action = false;



    public double getPosition() {
        return pivot.getPosition();
    }

    public void setActionPos(double pos){
        ACTION_POS = pos;
    }

    public void setIdlePos(double pos){
        IDLE_POS = pos;
    }

    public void setPosition(double position) {
        pivot.setPosition(position);
    }

    public void init(HardwareMap map, String servoName, double actionPos, double idlePos) {
        name = servoName;
        pivot = map.get(Servo.class, name);
        pivot.setDirection(Servo.Direction.REVERSE);
        ACTION_POS = actionPos;
        IDLE_POS = idlePos;
    }

    @Override
    public void init(HardwareMap map) {
        name = "outputClaw";
        pivot = map.get(Servo.class, name);
        pivot.setDirection(Servo.Direction.REVERSE);
    }

    /**
     * Makes the claw start to move towards the specified position.
     *
     * @param isAction open or close the claw
     */
    public void setAction(boolean isAction) {
        this.action = isAction;
        if (action){
            pivot.setPosition(ACTION_POS);
        }else{
            pivot.setPosition(IDLE_POS);
        }
    }

    /**
     * Start to move the claw opposite to the current state.
     */
    public void toggleAction() {
        action = !action;
        setAction(action);
    }
}

