package org.firstinspires.ftc.teamcode.modules.Webcams;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

import java.util.List;


public class PlaceDetectionWebcam extends Webcam {

    public int placeSpace = 2;

    public final int CENTER = 0;
    public final int LEFT = -1;
    public final int RIGHT = 1;

    private double leftThreshhold = 300;
    private double rightThreshhold = 800;

    public void init(HardwareMap map, String modelName) {
        TFOD_MODEL = modelName;
        super.getModelFromAsset = true;
        super.initTfod(map);
        super.toggleTfod();
    }
    @Override
    public void init(HardwareMap map) {
        TFOD_MODEL = "DEFAULT MODEL NAME HERE";
        super.getModelFromAsset = false;
        super.initTfod(map);
        super.toggleTfod();
    }

    @Override
    public void detect() {
        if (super.tfod != null) {
            List<Recognition> updatedRecognitions = tfod.getRecognitions();
            if (updatedRecognitions != null) {
                TelemetryWrapper.t.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() == 1) {
                    super.detectionComplete = true;
                    Recognition recognition = updatedRecognitions.get(0);
                    TelemetryWrapper.setLine(2, "Detected: " + recognition.getLabel());
                    if (recognition.getLeft() < leftThreshhold) {
                        placeSpace = LEFT;
                    } else if (recognition.getLeft() > rightThreshhold) {
                        placeSpace = RIGHT;
                    } else {
                        placeSpace = CENTER;
                    }
                } else {
                    TelemetryWrapper.setLine(2, "Detected: None");
                }
            }
        }
    }

}

