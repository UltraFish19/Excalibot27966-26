package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name = "AutoTest1", group = "Utils")
public class AutoTest1 extends LinearOpMode {


    public void runOpMode()  {
        Framework Bot = new Framework();
        Bot.Init(hardwareMap,telemetry);

        waitForStart();

        Bot.AutoFramework.Move(117,0.3);
        Bot.AutoFramework.SetAngle(180,true,0.15);
        Bot.AutoFramework.Shoot();
        Bot.AutoFramework.Move(50,-0.5);




    }
}
