package org.firstinspires.ftc.teamcode.modules.intake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.modules.Modulable;

public class IntakePivot implements Modulable {
    private Servo pivot;

    @Override
    public void init(HardwareMap hardwareMap) {
        pivot = hardwareMap.get(Servo.class, "pivot"); // Control Hub 0
        setTargetOrientation(Orientation.VERTICAL);
    }

    public double getPosition() {
        return pivot.getPosition();
    }

    public void setTargetOrientation(Orientation orientation) {
        pivot.setPosition(orientation.position);
    }

    public void setTargetPosition(double position) {
        pivot.setPosition(position);
    }

    public enum Orientation {
        INTAKE(0.5), VERTICAL(0.6), HOLDER(0.7);

        private final double position;

        Orientation(double position) {
            this.position = position;
        }
    }
}
