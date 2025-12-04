package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

import java.util.ArrayList;
import java.util.List;


@TeleOp(name = "TeleOP (1.2)")
public class ExcalibotTeleOP extends OpMode {

    Framework Bot = new Framework();


    final float SlowSpeed = 0.15f;



    ElapsedTime TelemetryTimer;






    @Override
    public void init() {

        Bot.Init(hardwareMap, telemetry); // The Framework needs to get sent the 2 params because it doesn't have any access to them by default
       // Bot.SetupCamera();
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
        FrontLeftMotorOutputs.add(Speed);
        FrontRightMotorOutputs.add(-Speed);
        BackLeftMotorOutputs.add(-Speed);
        BackRightMotorOutputs.add(Speed);
    }

    private void Rotate(float Speed){
        FrontLeftMotorOutputs.add(Speed);
        FrontRightMotorOutputs.add(-Speed);
        BackLeftMotorOutputs.add(Speed);
        BackRightMotorOutputs.add(-Speed);
    }


    private void AdditionalMotorLoop(){
        if (gamepad1.right_bumper) {
            Bot.Intake.setPower(-1.0);
        } else if (gamepad1.left_bumper) {
            Bot.Intake.setPower(1.0);
        } else {
            Bot.Intake.setPower(0);
        }

        Bot.Shooter.setPower(gamepad1.right_trigger);
    }

    private void DriveTrainLoop() {

        FrontLeftMotorOutputs = new ArrayList<Float>();
        FrontRightMotorOutputs = new ArrayList<Float>();
        BackLeftMotorOutputs = new ArrayList<Float>();
        BackRightMotorOutputs = new ArrayList<Float>();

        if (gamepad1.left_stick_y != 0) { // Forward
            float Power = (float) Math.copySign(
                    Math.pow(Math.abs(gamepad1.left_stick_y), 1.8),
                    -gamepad1.left_stick_y
            );

            StraightMove(Power);
        }

        if (gamepad1.right_stick_x != 0) {
            if (gamepad1.right_stick_button) { // Crab walking
                Crabwalk(gamepad1.right_stick_x);
            } else { // Rotate
                float Power = (float) Math.copySign(
                        Math.pow(Math.abs(gamepad1.right_stick_x), 1.8),
                        gamepad1.right_stick_x // Uses the sign of the inverted input
                );

                Rotate(Power);
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
        AdditionalMotorLoop();

        if (TelemetryTimer.milliseconds() >= 250.0) { // Telemetry log every 250 millisecond to not overflow
            Bot.UpdateData();
            TelemetryTimer.reset();
        }






    }


}
