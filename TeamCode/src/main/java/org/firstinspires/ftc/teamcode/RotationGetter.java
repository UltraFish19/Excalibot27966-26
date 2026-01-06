package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@TeleOp(name = "Rotation Calibrator", group = "Utils")
public class RotationGetter extends OpMode {

    DcMotor Motor;


    @Override
    public void init() {
        Motor = hardwareMap.get(DcMotor.class, "Shooter");
        Motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            telemetry.addLine(String.valueOf(Motor.getCurrentPosition()));
            telemetry.update();
        }
    }


}