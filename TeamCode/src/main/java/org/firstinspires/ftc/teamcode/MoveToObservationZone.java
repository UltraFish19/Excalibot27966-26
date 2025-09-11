package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "MoveToObservationZone (MUST BE CLOSEST TO ZONE)")
public class MoveToObservationZone extends LinearOpMode {

    Framework Bot = new Framework();

    @Override
    public void runOpMode() throws InterruptedException { // Runs the stuff

        Bot.Init(hardwareMap,telemetry);

        waitForStart();

        Bot.AutoFramework.Move(10);
        Bot.AutoFramework.SetAngle(-90,false,0.3);
        Bot.AutoFramework.Move(100);



    }
}
