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



    ElapsedTime Time;


    @Override
    public void init() {

        Bot.Init(hardwareMap, telemetry);
        Time = new ElapsedTime();

    }

    private void DriveTrainLoop() {

        List<Float> FrontLeftMotorOutputs = new ArrayList<Float>();
        List<Float> FrontRightMotorOutputs = new ArrayList<Float>();
        List<Float> BackLeftMotorOutputs = new ArrayList<Float>();
        List<Float> BackRightMotorOutputs = new ArrayList<Float>();

        if (gamepad1.left_stick_y != 0) { // Forward
            FrontLeftMotorOutputs.add(gamepad1.left_stick_y);
            FrontRightMotorOutputs.add(gamepad1.left_stick_y);
            BackLeftMotorOutputs.add(gamepad1.left_stick_y);
            BackRightMotorOutputs.add(gamepad1.left_stick_y);
        }

        if (gamepad1.right_stick_x != 0) {
            if (gamepad1.right_stick_button) { // Crab walking

                FrontLeftMotorOutputs.add(-gamepad1.right_stick_x);
                FrontRightMotorOutputs.add(gamepad1.right_stick_x);
                BackLeftMotorOutputs.add(-gamepad1.right_stick_x);
                BackRightMotorOutputs.add(gamepad1.right_stick_x);
            } else { // Rotate
                FrontLeftMotorOutputs.add(-gamepad1.right_stick_x);
                FrontRightMotorOutputs.add(gamepad1.right_stick_x);
                BackLeftMotorOutputs.add(gamepad1.right_stick_x);
                BackRightMotorOutputs.add(-gamepad1.right_stick_x);
            }
        }

        if (gamepad1.dpad_right) { // Slow Crab walking
            FrontLeftMotorOutputs.add(-SlowSpeed);
            FrontRightMotorOutputs.add(SlowSpeed);
            BackLeftMotorOutputs.add(-SlowSpeed);
            BackRightMotorOutputs.add(SlowSpeed);

        } else if (gamepad1.dpad_left) {
            FrontLeftMotorOutputs.add(SlowSpeed);
            FrontRightMotorOutputs.add(-SlowSpeed);
            BackLeftMotorOutputs.add(SlowSpeed);
            BackRightMotorOutputs.add(-SlowSpeed);

        }

        if (gamepad1.dpad_up) {
            FrontLeftMotorOutputs.add(-SlowSpeed);
            FrontRightMotorOutputs.add(-SlowSpeed);
            BackLeftMotorOutputs.add(-SlowSpeed);
            BackRightMotorOutputs.add(-SlowSpeed);
        } else if (gamepad1.dpad_down){
            FrontLeftMotorOutputs.add(SlowSpeed);
            FrontRightMotorOutputs.add(SlowSpeed);
            BackLeftMotorOutputs.add(SlowSpeed);
            BackRightMotorOutputs.add(SlowSpeed);


        }



        Bot.FrontLeftMotor.setPower(JavaUtil.averageOfList(FrontLeftMotorOutputs));
        Bot.FrontRightMotor.setPower(JavaUtil.averageOfList(FrontRightMotorOutputs));
        Bot.BackLeftMotor.setPower(JavaUtil.averageOfList(BackLeftMotorOutputs));
        Bot.BackRightMotor.setPower(JavaUtil.averageOfList(BackRightMotorOutputs));

    }



    public void loop() {

        DriveTrainLoop();
       // ExtenderLoop();

        if (Time.milliseconds() >= 250.0) { // Telemetry log every 250 millisecond to not overflow
            Bot.UpdateData();
            Time.reset();
        }


    }


}
