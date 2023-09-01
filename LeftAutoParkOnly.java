package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "LeftAutoParkOnly", group = "opmode")
public class LeftAutoParkOnly extends EncoderAutoParkOnly {
    public LeftAutoParkOnly() {
        super(EncoderAutoParkOnly.LEFT);
    }
}

