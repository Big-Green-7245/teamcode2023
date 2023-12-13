package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ServoPixel implements Modulable {
    public static final double CLOSED_POS = 0;
    private static final double OPENED_POS = 0.3;
    private final String name;
    private Servo servo;
    private boolean open;

    public ServoPixel(String name) {
        this.name = name;
    }

    public double getPosition() {
        return servo.getPosition();
    }

    public void setPosition(double position) {
        servo.setPosition(position);
    }

    @Override
    public void init(HardwareMap map) {
        servo = map.get(Servo.class, name);
    }

    /**
     * Makes the servo start to move towards the specified position.
     *
     * @param open move towards open or close position
     */
    public void setOpen(boolean open) {
        this.open = open;
        startMove();
    }

    /**
     * Start to move the claw opposite to the current state.
     */
    public void toggle() {
        open = !open;
        startMove();
    }

    private void startMove() {
        servo.setPosition(open? OPENED_POS: CLOSED_POS);
    }
}

