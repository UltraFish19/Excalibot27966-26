package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

import java.util.ArrayList;
import java.util.List;


@TeleOp(name = "TeleOP (1.5)")
public class ExcalibotTeleOP extends OpMode {

    final float SlowSpeed = 0.25f;
    Framework Bot = new Framework();
    ElapsedTime TelemetryTimer;
    List<Float> FrontLeftMotorOutputs;
    List<Float> FrontRightMotorOutputs;
    List<Float> BackLeftMotorOutputs;
    List<Float> BackRightMotorOutputs;

    @Override
    public void init() {

        Bot.Init(hardwareMap, telemetry); // The Framework needs to get sent the 2 params because it doesn't have any access to them by default
        Bot.SetupCamera();
        TelemetryTimer = new ElapsedTime();


    }

    private void Move(float Speed, String Action) {
        DriveTrainParams MotorParameter; // The correct directions of the motors

        switch (Action) {
            case "Straight":
                MotorParameter = Framework.MotorParams.StraightParams;
                break;

            case "Rotate":
                MotorParameter = Framework.MotorParams.RotateParams;
                break;

            case "Crabwalk":
                MotorParameter = Framework.MotorParams.CrabwalkParams;
                break;

            default:
                throw new RuntimeException("Invalid Movement Action for drive train.");

        }

        FrontLeftMotorOutputs.add(Speed * MotorParameter.FrontLeft);
        FrontRightMotorOutputs.add(Speed * MotorParameter.FrontRight);
        BackLeftMotorOutputs.add(Speed * MotorParameter.BackLeft);
        BackRightMotorOutputs.add(Speed * MotorParameter.BackRight);



    }




    private void AdditionalMotorLoop() {
        if (gamepad1.right_bumper) {
            Bot.Intake.setPower(-1.0);
        } else if (gamepad1.left_bumper) {
            Bot.Intake.setPower(1.0);
        } else {
            Bot.Intake.setPower(0);
        }

        if (gamepad1.right_trigger > 0){
         Bot.Shooter.setVelocity((1200 / 60) * 117);
    }   else if (gamepad1.left_trigger > 0) {
        Bot.Shooter.setPower(-(gamepad1.left_trigger / 2.0));
        } else{
            Bot.Shooter.setPower(0);
        }
    }

    private void DriveTrainLoop() {

        FrontLeftMotorOutputs = new ArrayList<>();
        FrontRightMotorOutputs = new ArrayList<>();
        BackLeftMotorOutputs = new ArrayList<>();
        BackRightMotorOutputs = new ArrayList<>();

        if (gamepad1.left_stick_y != 0) { // Forward
            float Power = (float) Math.copySign(
                    Math.pow(Math.abs(gamepad1.left_stick_y), 1.8),
                    gamepad1.left_stick_y
            );

            Move(Power,"Straight");
        }

        if (gamepad1.right_stick_x != 0) {
            if (gamepad1.right_stick_button) { // Crab walking
                Move(gamepad1.right_stick_x,"Crabwalk");
            } else { // Rotate
                float Power = (float) Math.copySign(
                        Math.pow(Math.abs(gamepad1.right_stick_x), 1.8),
                        gamepad1.right_stick_x // Uses the sign of the inverted input
                );

                Move(Power,"Rotate");
            }
        }

        if (gamepad1.dpad_right) { // Slow Crab walking
            Move(SlowSpeed,"Crabwalk");

        } else if (gamepad1.dpad_left) {
            Move(-SlowSpeed,"Crabwalk");
        }

        if (gamepad1.dpad_up) {
            Move(SlowSpeed,"Straight");
        } else if (gamepad1.dpad_down) {
            Move(-SlowSpeed,"Straight");


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
