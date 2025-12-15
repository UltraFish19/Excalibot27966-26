package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

@Autonomous(name = "ExcalibotAuto ⭐")
public class ExcalibotAuto extends LinearOpMode {


    Framework Bot = new Framework();

    List<String> AutoList = List.of(
         "Basic go Straight",
         "Score basket as BLUE",
         "Score basket as RED",
         "spin"
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






    }
}
