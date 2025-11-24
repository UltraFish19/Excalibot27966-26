package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

import java.util.ArrayList;
import java.util.List;


@TeleOp(name = "Excalibot TeleOP")
public class ExcalibotTeleOP extends OpMode {

    Framework Bot = new Framework();


    final float SlowSpeed = 0.35f;



    ElapsedTime TelemetryTimer;


    boolean IntakeValueLastIteration = false;



    @Override
    public void init() {

        Bot.Init(hardwareMap, telemetry); // The Framework needs to get sent the 2 params because it doesn't have any access to them by default
        TelemetryTimer = new ElapsedTime();


    }



    List<Float> FrontLeftMotorOutputs;
    List<Float> FrontRightMotorOutputs;
    List<Float> BackLeftMotorOutputs;
    List<Float> BackRightMotorOutputs;

    private void StraightMove(float Speed){
        FrontLeftMotorOutputs.add(Speed);
        FrontRightMotorOutputs.add(Speed);
        BackLeftMotorOutputs.add(Speed);
        BackRightMotorOutputs.add(Speed);
    }

    private void Crabwalk(float Speed){
        FrontLeftMotorOutputs.add(-Speed);
        FrontRightMotorOutputs.add(Speed);
        BackLeftMotorOutputs.add(Speed);
        BackRightMotorOutputs.add(-Speed);
    }

    private void Rotate(float Speed){
        FrontLeftMotorOutputs.add(Speed);
        FrontRightMotorOutputs.add(-Speed);
        BackLeftMotorOutputs.add(Speed);
        BackRightMotorOutputs.add(-Speed);
    }


    private void IntakeLoop(){
        if (gamepad1.right_bumper) {
            Bot.Intake.setPower(-1.0);
        } else if (gamepad1.left_bumper) {
            Bot.Intake.setPower(1.0);
        } else {
            Bot.Intake.setPower(0);
        }
    }

    private void DriveTrainLoop() {

        FrontLeftMotorOutputs = new ArrayList<Float>();
        FrontRightMotorOutputs = new ArrayList<Float>();
        BackLeftMotorOutputs = new ArrayList<Float>();
        BackRightMotorOutputs = new ArrayList<Float>();

        if (gamepad1.left_stick_y != 0) { // Forward
            StraightMove(-gamepad1.left_stick_y);
        }

        if (gamepad1.right_stick_x != 0) {
            if (gamepad1.right_stick_button) { // Crab walking
                Crabwalk(gamepad1.right_stick_x);
            } else { // Rotate
                Rotate(gamepad1.right_stick_x);
            }
        }

        if (gamepad1.dpad_right) { // Slow Crab walking
            Crabwalk(SlowSpeed);

        } else if (gamepad1.dpad_left) {
            Crabwalk(-SlowSpeed);
        }

        if (gamepad1.dpad_up) {
            StraightMove(SlowSpeed);
        } else if (gamepad1.dpad_down){
            StraightMove(-SlowSpeed);


        }






        Bot.FrontLeftMotor.setPower(JavaUtil.averageOfList(FrontLeftMotorOutputs));
        Bot.FrontRightMotor.setPower(JavaUtil.averageOfList(FrontRightMotorOutputs));
        Bot.BackLeftMotor.setPower(JavaUtil.averageOfList(BackLeftMotorOutputs));
        Bot.BackRightMotor.setPower(JavaUtil.averageOfList(BackRightMotorOutputs));

    }



    public void loop() {

        DriveTrainLoop();
        IntakeLoop();

        if (TelemetryTimer.milliseconds() >= 250.0) { // Telemetry log every 250 millisecond to not overflow
            Bot.UpdateData();
            TelemetryTimer.reset();
        }




    }


}
