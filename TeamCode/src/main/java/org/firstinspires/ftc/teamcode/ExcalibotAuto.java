package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;

@Autonomous(name = "ExcalibotAuto  ⭐")
public class ExcalibotAuto extends LinearOpMode {


    Framework Bot = new Framework();

    List<String> AutoList = List.of(
         "Basic go Straight",
         "Score Basket"
    );

    int AutoSelected = 0;

    private void CycleAutos(){
        AutoSelected += 1;

        if (AutoSelected >= AutoList.size()){
            AutoSelected = 0;
        }


        telemetry.addLine("Selected Auto:");
        telemetry.addLine(AutoList.get(AutoSelected));
        telemetry.update();
    }


    @Override
    public void runOpMode() {




        boolean CycleButtonPressed = false;
        while (opModeInInit()){ // wait until the auto is initialized
            if (gamepad1.a) {

                if (!CycleButtonPressed) {
                    CycleAutos();
                }

                CycleButtonPressed = true;

            } else {
                CycleButtonPressed = false;
            }
        }


        Bot.Init(hardwareMap,telemetry);
        Bot.SetIndicatorLight(Color.RED);

        switch (AutoSelected){


            case (0):
                Bot.AutoFramework.Move(70,-0.3);
                break;

            case(1):

                Bot.AutoFramework.Move(117,-0.3);
                Bot.AutoFramework.Shoot();
                Bot.AutoFramework.Move(50,-0.5);
                break;

        }





    }
}
