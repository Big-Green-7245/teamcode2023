package org.firstinspires.ftc.teamcode.modules.Webcams;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

public class PoleAlignWebcam extends Webcam{

    public double[] x_pos;
    public int poleWidth;
    public int numDetected;

    @Override
    public void init(HardwareMap map){
        super.initTfod(map);
        //Change the tfod model file to the correct one
        super.initVuforia(map, "/sdcard/FIRST/tflitemodels/signal.tflite", new String[]{"junction"});
    }

    public void detect() {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                 numDetected = updatedRecognitions.size();

                // step through the list of recognitions and display image position/size information for each one
                // Note: "Image number" refers to the randomized image orientation/number
                x_pos = new double[]{updatedRecognitions.get(0).getLeft(), updatedRecognitions.get(0).getRight()};
                poleWidth = updatedRecognitions.get(0).getImageWidth();
            }
        }
    }
}
