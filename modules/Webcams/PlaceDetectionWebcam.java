package org.firstinspires.ftc.teamcode.modules.Webcams;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PlaceDetectionWebcam extends TFWebcam {

    public int placeSpace = -2;

    public final static int CENTER = 0;
    public final static int LEFT = -1;
    public final static int RIGHT = 1;

    private double leftThreshhold = 500;
    private double rightThreshhold = 1400;

    public void init(HardwareMap map, String modelName) {
        TFOD_MODEL = modelName;
        super.initTfod(map);
    }
    @Override
    public void init(HardwareMap map) {
        TFOD_MODEL = "DEFAULT MODEL NAME HERE";
        super.initTfod(map);
    }

    @Override
    public void detect() {
        if (super.tfod != null) {
            List<Recognition> updatedRecognitions = tfod.getRecognitions();
            if (updatedRecognitions != null) {
                updatedRecognitions.sort(Comparator.comparing(Recognition::getConfidence));
                Collections.reverse(updatedRecognitions);
                TelemetryWrapper.t.addData("# Object Detected", updatedRecognitions.size());
                if (!updatedRecognitions.isEmpty()) {
                    super.detectionComplete = true;
                    Recognition recognition = updatedRecognitions.get(0);
                    double xPos = (recognition.getLeft() + recognition.getRight()) / 2;
                    if (xPos < leftThreshhold) {
                        placeSpace = LEFT;
                        TelemetryWrapper.setLine(2, "Detected: Left " + xPos);
                    } else if (xPos > rightThreshhold) {
                        placeSpace = RIGHT;
                        TelemetryWrapper.setLine(2, "Detected: Right " + xPos);
                    } else {
                        placeSpace = CENTER;
                        TelemetryWrapper.setLine(2, "Detected: Center " + xPos);
                    }
                } else {
                    TelemetryWrapper.setLine(2, "Detected: None");
                    if (placeSpace == LEFT) {
                        TelemetryWrapper.setLine(3, "Current Place Space: Left");
                    } else if (placeSpace == RIGHT) {
                        TelemetryWrapper.setLine(3, "Current Place Space: Right");
                    } else if (placeSpace == CENTER) {
                        TelemetryWrapper.setLine(3, "Current Place Space: Center");
                    } else {
                        TelemetryWrapper.setLine(3, "Current Place Space: None");
                    }
                }
            }
        }
    }

}

