package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Disabled
@Autonomous(name = "AutoTest")
public class AutoTest extends LinearOpMode {

    Framework Bot = new Framework();

    @Override
    public void runOpMode() throws InterruptedException { // Runs the stuff

        Bot.Init(hardwareMap,telemetry);

        waitForStart();

        Bot.AutoFramework.MoveSideways(100,true);



    }
}
