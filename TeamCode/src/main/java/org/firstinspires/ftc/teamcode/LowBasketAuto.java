package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "LowBasketAuto (MUST BE CLOSEST TO BASKET)")
public class LowBasketAuto extends LinearOpMode {

    Framework Bot = new Framework();

    @Override
    public void runOpMode() throws InterruptedException { // Runs the stuff

        Bot.Init(hardwareMap,telemetry);

        waitForStart();

        Bot.AutoFramework.SetClaw(0.8);

        Bot.AutoFramework.Move(20);
        Bot.AutoFramework.SetAngle(90,true,0.3);
        Bot.AutoFramework.Move(80);
        Bot.AutoFramework.SetAngle(135,true,0.3);
        Bot.AutoFramework.SetArm(5);

        Bot.AutoFramework.SetClaw(1);

        Bot.Sleep(1000); // So it fully closes



    }
}
