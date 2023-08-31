package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "RightAutoParkOnly", group = "opmode")
public class RightAutoParkOnly extends EncoderAutoParkOnly {
    public RightAutoParkOnly() {
        super(EncoderAutoParkOnly.RIGHT);
    }
}

