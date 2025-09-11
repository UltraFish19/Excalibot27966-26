package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name = "CollectBlocks (MUST BE CLOSEST TO ZONE)")
public class CollectBlocksAuto extends LinearOpMode {

    Framework Bot = new Framework();

    @Override
    public void runOpMode() throws InterruptedException { // Runs the stuff

        Bot.Init(hardwareMap,telemetry);

        waitForStart();

        Bot.AutoFramework.Move(63.5f);
        Bot.AutoFramework.MoveSideways(40f,false);
        Bot.AutoFramework.Move(70f);
        Bot.AutoFramework.SetAngle(-90,false,0.3);




    }
}
