package org.firstinspires.ftc.teamcode.modules.Webcams;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.modules.Modulable;
import org.firstinspires.ftc.teamcode.modules.Tickable;
import org.firstinspires.ftc.teamcode.util.ChainableBooleanSupplier;

import java.util.function.BooleanSupplier;

public class Webcam implements Modulable, Tickable, ChainableBooleanSupplier {
    //Declare model for object detection
    @SuppressLint("SdCardPath")
    private String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/signal.tflite";

    private String[] LABELS = {};

    private static final String VUFORIA_KEY = "AQIK9eP/////AAABmSfuIJd+0UVOt6G7lBD1wM8kaTvNDhfhZpIcg4Pa/wr6OIq8nARnVDLguWK5ae82T2dSvfb8NkfNPauXPlSmvwsWWq7zq+BfO5BfhaOsn3SNZKpBGKm8i3KMBnp48rD6oz/nQ8FATjUNv7j0W/CdhgWbete4GpVS7FC0Cr+6/iJGF1mqCEsCgiWx02sLd5NFkYqp+uKh5uiEtA/CC3T86hR/khTaX3BsnnXG9hUGh0t+lwxzL9ontudjc1ldRIhylOGnPUB0v6ht4R/X9iprB9yc1Je0D0e/Ra8ysLGROAxf8SAbuotjU2J7qmam6en3b9X0A0FrVMX1zI7W2vAzmJgrEaXZ+NmjgvsKGjI16/Qe";

    /**
     * The instance of the Vuforia localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * The instance of the TensorFlow Object Detection engine.
     */
    public TFObjectDetector tfod;

    /**
     * Whether the Webcam is done detecting
     */
    public boolean detectionComplete;


    /**
     * Initialize the Vuforia localization engine.
     */
    public void initVuforia(HardwareMap map, String tfodModelFile, String[] labels) {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = map.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        LABELS = labels;
        TFOD_MODEL_FILE = tfodModelFile;
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    public void initTfod(HardwareMap map) {
        int tfodMonitorViewId = map.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", map.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        //tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }


    public void detect(){

    }

    @Override
    public void init(HardwareMap map){

    }
    @Override
    public void tick(){

    }
    @Override
    public boolean getAsBoolean(){
        return detectionComplete;
    }
}
