package org.firstinspires.ftc.teamcode.modules.output;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.modules.Modulable;

public class OutputPivot implements Modulable {
    public static final int OUTPUT_POS = 0;
    private static final int RELOAD_POS = 0;
    private final String name;
    protected DcMotor pivot;
    private boolean atOutput;

    public OutputPivot(String name) {
        this.name = name;
    }

    public void moveUsingEncoder(double power){
        pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pivot.setPower(power);
    }

    public int getPosition() {
        return pivot.getCurrentPosition();
    }

    public void setPosition(int position) {
        pivot.setTargetPosition(position);
    }


    @Override
    public void init(HardwareMap map) {
        pivot = map.get(DcMotor.class, name);
    }

    public void startMovePivot(){
        pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /**
     * Makes the claw start to move towards the specified position. (Currently blocks)
     *
     * @param open open or close the claw
     */
    public void setOutputPosition(boolean atOutput) {
        this.atOutput = atOutput;
        if (atOutput) {
            pivot.setTargetPosition(OUTPUT_POS);
        }else{
            pivot.setTargetPosition(RELOAD_POS);
        }
        startMovePivot();
        while(pivot.isBusy()){}
        pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Start to move the claw opposite to the current state. (Currently blocks)
     */
    public void toggleClaw() {
        if (atOutput) {
            pivot.setTargetPosition(OUTPUT_POS);
        }else{
            pivot.setTargetPosition(RELOAD_POS);
        }
        startMovePivot();
        while(pivot.isBusy()){}
        pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
