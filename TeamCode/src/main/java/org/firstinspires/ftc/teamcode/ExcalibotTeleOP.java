package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;


@TeleOp(name = "TeleOP ⭐ (1.83)")
public class ExcalibotTeleOP extends OpMode {

    final float SlowSpeed = 0.4f;
    Framework Bot = new Framework();
    ElapsedTime TelemetryTimer;





    @Override
    public void init() {

        Bot.Init(hardwareMap, telemetry); // The Framework needs to get sent the 2 params because it doesn't have any access to them by default
        Bot.SetupCamera();
        TelemetryTimer = new ElapsedTime();
        Bot.SetIndicatorLight(Color.DKGRAY);





    }














    private void AdditionalMotorLoop() {

        if (gamepad1.right_bumper) { // Suck balls in
            Bot.Intake.setPower(-1.0);
            Bot.Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            if (gamepad1.right_trigger == 0){
                Bot.Shooter.setPower(-0.4);
            }

        } else if (gamepad1.left_bumper) {
            Bot.Intake.setPower(1.0);
            Bot.Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else {

                Bot.AlignIntake();
                Bot.Intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                Bot.Intake.setPower(1);



        }

        if (gamepad1.right_trigger > 0){
         Bot.Shooter.setVelocity((Framework.FlywheelRPM / 60) * Bot.ShooterTicksPerRotation);
    }   else if (gamepad1.left_trigger > 0) {
        Bot.Shooter.setPower(-(gamepad1.left_trigger / 2.0));
        } else{
            Bot.Shooter.setPower(0);
        }


    }

    private void DriveTrainLoop() {




        Bot.ResetDriveTrainMotors();

        if (gamepad1.left_stick_y != 0) { // Forward
            float Power = (float) Math.copySign(
                    Math.pow(Math.abs(gamepad1.left_stick_y), 1.8),
                    gamepad1.left_stick_y
            );

            Bot.Move(Power,Framework.MoveDirection.STRAIGHT);

        }

        if (gamepad1.left_stick_x !=0) {
            Bot.Move(gamepad1.left_stick_x,Framework.MoveDirection.CRABWALK);

        }

        if (gamepad1.right_stick_x != 0) {
            if (gamepad1.right_stick_button) { // Crab walking
                // Action used to be here.
            } else { // Rotate
                float Power = (float) Math.copySign(
                        Math.pow(Math.abs(gamepad1.right_stick_x), 1.8),
                        gamepad1.right_stick_x // Uses the sign of the inverted input
                );

                Bot.Move(Power,Framework.MoveDirection.ROTATE);

            }
        }

        if (gamepad1.dpad_right) { // Slow Crab walking
            Bot.Move(SlowSpeed,Framework.MoveDirection.CRABWALK);

        } else if (gamepad1.dpad_left) {
            Bot.Move(-SlowSpeed,Framework.MoveDirection.CRABWALK);

        }

        if (gamepad1.dpad_up) {
            Bot.Move(-SlowSpeed,Framework.MoveDirection.STRAIGHT);

        } else if (gamepad1.dpad_down) {
            Bot.Move(SlowSpeed, Framework.MoveDirection.STRAIGHT);
        }

        System.out.println(gamepad1.x);

        if (gamepad1.x){

            Bot.Move(-SlowSpeed / 2 ,Framework.MoveDirection.ROTATE);
        }  else  if (gamepad1.b){
            Bot.Move(SlowSpeed / 2,Framework.MoveDirection.ROTATE);

        }






        if (gamepad1.y){

                if ( Bot.Range == null ) {
                    Bot.Move(SlowSpeed, Framework.MoveDirection.STRAIGHT); // Too far

                } else if (Bot.Range > Bot.SweetSpot + Bot.SweetSpotTolerance) {
                        Bot.Move(-SlowSpeed, Framework.MoveDirection.STRAIGHT); // Too far
                } else if (Bot.Range < Bot.SweetSpot - Bot.SweetSpotTolerance) {
                    Bot.Move(SlowSpeed, Framework.MoveDirection.STRAIGHT); // close
                }

        }


        Bot.UpdateDriveTrainMotors();

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
