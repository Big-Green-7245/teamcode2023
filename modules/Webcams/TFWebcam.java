package org.firstinspires.ftc.teamcode.modules.Webcams;

import android.annotation.SuppressLint;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.modules.Modulable;
import org.firstinspires.ftc.teamcode.modules.Tickable;
import org.firstinspires.ftc.teamcode.util.FinishCondition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import android.util.Size;

public abstract class TFWebcam implements Modulable, Tickable, FinishCondition {

    //Declare model for object detection
    @SuppressLint("SdCardPath")
    protected String TFOD_MODEL = "MyModelStoredAsAsset.tflite";

    protected String[] LABELS = {"Red"};

    /**
     * The instance of the TensorFlow Object Detection engine.
     */
    protected TfodProcessor tfod;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    protected boolean isEnabled = false;

    public boolean detectionComplete;


    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    protected void initTfod(HardwareMap hardwareMap) {
        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()

//                 With the following lines commented out, the default TfodProcessor Builder
//                 will load the default model for the season. To define a custom model to load,
//                 choose one of the following:
//                   Use setModelAssetName() if the custom TF Model is built in as an asset (AS only).
//                   Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
                .setModelAssetName(TFOD_MODEL)
//                .setModelFileName(TFOD_MODEL)

//                 The following default settings are available to un-comment and edit as needed to
//                 set parameters for custom models.
                .setModelLabels(LABELS)
                .setIsModelTensorFlow2(true)
                .setIsModelQuantized(true)
                .setModelInputSize(300)
                .setModelAspectRatio(16.0 / 9.0)

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));

        // Choose a camera resolution. Not all cameras support all resolutions.
        builder.setCameraResolution(new Size(1920, 1080));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(tfod);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        tfod.setMinResultConfidence(0.55f);

        // Disable or re-enable the TFOD processor at any time.
        visionPortal.setProcessorEnabled(tfod, true);

    }

    public void toggle() {
        if (tfod != null) {
            isEnabled = !isEnabled;
            visionPortal.setProcessorEnabled(tfod, isEnabled);
        }
    }

    public void stop(){
        // Save more CPU resources when camera is no longer needed.
        visionPortal.close();
    }

    @Override
    public void tick() {
        detect();
    }

    public abstract void detect();

    @Override
    public boolean isFinished() {
        return detectionComplete;
    }
}
