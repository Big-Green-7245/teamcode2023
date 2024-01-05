package org.firstinspires.ftc.teamcode.modules.output;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.Modulable;

public class ServoToggle implements Modulable  {
    private double IDLE_POS = 0;
    private double ACTION_POS = 0.3;
    private String name;
    protected Servo servo;
    private boolean action = false;




    public double getPosition() {
        return servo.getPosition();
    }

    public void setActionPos(double pos){
        ACTION_POS = pos;
    }

    public void setIdlePos(double pos){
        IDLE_POS = pos;
    }

    public void setPosition(double position) {
        servo.setPosition(position);
    }

    public void init(HardwareMap map, String servoName, double actionPos, double idlePos, boolean isReversed) {
        name = servoName;
        servo = map.get(Servo.class, name);
        if (isReversed) {
            servo.setDirection(Servo.Direction.REVERSE);
        }else{
            servo.setDirection(Servo.Direction.FORWARD);
        }
        ACTION_POS = actionPos;
        IDLE_POS = idlePos;
        setAction(action);

    }

    @Override
    public void init(HardwareMap map) {
        name = "outputClaw";
        servo = map.get(Servo.class, name);
        servo.setDirection(Servo.Direction.REVERSE);
    }

    /**
     * Makes the claw start to move towards the specified position.
     *
     * @param isAction open or close the claw
     */
    public void setAction(boolean isAction) {
        this.action = isAction;
        if (action){
            servo.setPosition(ACTION_POS);
        }else{
            servo.setPosition(IDLE_POS);
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

