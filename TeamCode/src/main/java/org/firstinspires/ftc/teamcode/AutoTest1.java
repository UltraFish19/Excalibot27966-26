package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;



@Autonomous(name = "AutoTest1")
public class AutoTest1 extends LinearOpMode {


    public void runOpMode()  {
        Framework Bot = new Framework();
        Bot.Init(hardwareMap,telemetry);

        waitForStart();

        Bot.AutoFramework.Move(117);
        Bot.AutoFramework.SetAngle(180.0,0.3);
        Bot.AutoFramework.Shoot();




    }
}
