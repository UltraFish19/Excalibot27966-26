package org.firstinspires.ftc.teamcode;


import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


@TeleOp
public class AprilTagTestV2 extends OpMode {


    VisionPortal Vision;
    AprilTagProcessor ATagProcessor; //April tag processer
    ElapsedTime VisionTimer; // Prevents overflowing CPU usage.

    @Override
    public void init(){
        AprilTagProcessor.Builder ATagBuilder = new AprilTagProcessor.Builder();

        ATagBuilder.setDrawTagID(true);

        ATagProcessor = ATagBuilder.build();

        VisionPortal.Builder VisionBuilder = new VisionPortal.Builder();

        VisionBuilder.setCamera(hardwareMap.get(WebcamName.class,"FrontCam"));
        VisionBuilder.addProcessor(ATagProcessor);
        VisionBuilder.setCameraResolution(new Size(620,480));
        VisionBuilder.enableLiveView(true);

        Vision = VisionBuilder.build();

        VisionTimer = new ElapsedTime();




    }


    @Override
    public void loop (){

        if (VisionTimer.seconds() >= 1) {

            List<AprilTagDetection> ATagDetections = ATagProcessor.getDetections();


            for (AprilTagDetection ATagDetection : ATagDetections) {
                if (ATagDetection.ftcPose != null) {
                    telemetry.addLine("Detected FTC Tag: " + ATagDetection.id);
                    telemetry.addLine("Distance: " + ATagDetection.ftcPose.range);
                }

            }



            VisionTimer.reset();
        }


    }



}
