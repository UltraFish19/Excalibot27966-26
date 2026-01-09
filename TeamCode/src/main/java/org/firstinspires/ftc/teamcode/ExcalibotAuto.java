package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

@Autonomous(name = "ExcalibotAuto (1.2)  ⭐")
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
                Bot.Auto.MoveStraight(70f,0.3f);
                break;

            case(1):

                Bot.Auto.MoveStraight(117f,-0.3f);
                Bot.Auto.Shoot();
                Bot.Auto.MoveStraight(70f,-0.5f);
                break;

        }





    }
}


