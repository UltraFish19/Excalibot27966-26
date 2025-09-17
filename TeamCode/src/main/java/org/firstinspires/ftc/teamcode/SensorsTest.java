package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.robotcore.external.Telemetry;


@TeleOp(name = "Color Sensor Test")
public class SensorsTest extends OpMode {


    RevColorSensorV3 ColourSensor;
    Telemetry.Item ColourValue;


    int RedVal;
    int GreenVal;
    int BlueVal;

    Telemetry.Item RedValue;
    Telemetry.Item GreenValue;
    Telemetry.Item BlueValue;



    boolean IsPurple = false;

    int ARGBVal; // Colour sensor value as an INT





    @Override
    public void init() {
        ColourSensor = hardwareMap.get(RevColorSensorV3.class,"ColorSensor");
        ColourValue = telemetry.addData("Is Purple:",false);
        RedValue = telemetry.addData("Red Value:",0);
        GreenValue = telemetry.addData("Green Value:",0);
        BlueValue = telemetry.addData("Blue Value:",0);





    }

    @Override
    public void loop() {



        ColourSensor.enableLed(false);

        ARGBVal = ColourSensor.argb();


        RedVal = Color.red(ARGBVal);
        GreenVal = Color.green(ARGBVal);
        BlueVal = Color.blue(ARGBVal);



        IsPurple = (RedVal > 5); // Detect if its closer to purple or green.
        ColourValue.setValue(IsPurple);

        RedValue.setValue(RedVal);
        GreenValue.setValue(GreenVal);
        BlueValue.setValue(BlueVal);

        telemetry.update();
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
