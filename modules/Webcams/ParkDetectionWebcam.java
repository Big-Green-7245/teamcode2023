package org.firstinspires.ftc.teamcode.modules.Webcams;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.util.TelemetryWrapper;

import java.util.List;


public class ParkDetectionWebcam extends Webcam{

    public int parkSpace = 2;

    @Override
    public void init(HardwareMap map){
        super.initVuforia(map, "/sdcard/FIRST/tflitemodels/signal.tflite", new String[]{"1 Blue", "2 Green", "3 Red"});
        super.initTfod(map);
        super.activateTfod();
        tfod.setZoom(1.0, 16.0 / 9.0);
    }

    public void detect() {
        if (super.tfod != null) {
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                TelemetryWrapper.t.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() == 1) {
                    super.detectionComplete = true;
                    Recognition recognition = updatedRecognitions.get(0);
                    TelemetryWrapper.setLine(2, "Detected: " + recognition.getLabel());
                    if (recognition.getLabel().equals("2 Green")) {
                        parkSpace = 1;
                    } else if (recognition.getLabel().equals("1 Blue")) {
                        parkSpace = 2;
                    } else if (recognition.getLabel().equals("3 Red")) {
                        parkSpace = 3;
                    }
                } else {
                    TelemetryWrapper.setLine(2, "Detected: None");
                }
            }
        }
    }

}

