package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@TeleOp(name = "April Tag Test")
public class AprilTagTest extends OpMode {

    AprilTagProcessor AprilTags;
    VisionPortal Vision;

    HardwareMap Hardware = hardwareMap;

    @Override
    public void init() {
        AprilTags = AprilTagProcessor.easyCreateWithDefaults();
        Vision = VisionPortal.easyCreateWithDefaults(Hardware.get(WebcamName.class,"MainCamera"),AprilTags);



    }

    @Override
    public void loop() {

    }




}
