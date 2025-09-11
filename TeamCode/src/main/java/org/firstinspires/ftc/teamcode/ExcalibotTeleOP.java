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

    float Y;
    float X;
    float LeftPower;
    float RightPower;
    double Max;

    final float SlowSpeed = 0.35f;

    final double ServoMin = 0.8;
    final double ServoMax = 1;


    float ArmSpeed = 0.1f;
    double ClawSpeed = 0.0025;
    double ExtenderSpeed = 0.5; // Unused
    boolean ArmHoldMode = false; // Unused


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






/*
    private void OLDDriveTrainLoop(){ //Drive train
 
     Y = -gamepad1.left_stick_y;  //Drive train 
    X = gamepad1.right_stick_x;
    LeftPower = Y + X;
    RightPower = Y - X;
    Max = JavaUtil.maxOfList(JavaUtil.createListWith(Math.abs(LeftPower), Math.abs(RightPower)));
    if (Max >= 1) {
      LeftPower = (float) (LeftPower / Max);
      RightPower = (float) (RightPower / Max);
    }
    Bot.LeftMotor.setPower(LeftPower);
    Bot.RightMotor.setPower(RightPower);
  
}

*/

    private void ArmControlLoop() {


        if (gamepad1.left_trigger > 0.1f) {
            Bot.ArmMotor1.setPower(ArmSpeed * gamepad1.left_trigger);
            Bot.ArmMotor2.setPower(ArmSpeed * gamepad1.left_trigger);
        } else if (gamepad1.right_trigger > 0.1f) { // Go Up



            Bot.ArmMotor1.setPower(-(ArmSpeed * gamepad1.right_trigger));
            Bot.ArmMotor2.setPower(-(ArmSpeed * gamepad1.right_trigger));
        } else {
            Bot.ArmMotor1.setPower(0);
            Bot.ArmMotor2.setPower(0);

        }

        if (gamepad1.start) { // Increase Power
            ArmSpeed = 0.35f;
        } else {
            ArmSpeed = 0.1f;
        }


    }



    /* public void encoder(){
     *    armTarget = armPos;
     *    armMotor1.setTargetPosition((int)armTarget);
     *    armMotor2.setTargetPosition((int)armTarget);
     *    armMotor1.setPower(0.5);
     *    armMotor2.setPower(0.5)
     *    armMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
     *    armMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
     *}
     *
     */


//    private void ExtenderLoop() {
//
//        if (gamepad1.dpad_up) {
//            Bot.ExtenderPosition += 1;
//            Bot.Extender.setPower(ExtenderSpeed);
//        } else if (gamepad1.dpad_down) {
//            Bot.ExtenderPosition -= 1;
//            Bot.Extender.setPower(-ExtenderSpeed);
//        } else {
//
//            Bot.Extender.setPower(0);
//        }
//
//    }


    private void ClawControl() {


        if (gamepad1.left_bumper) { // Open

            Bot.Intake.setPosition(Bot.ClawPosition);
            Bot.ClawPosition = Math.max(Bot.ClawPosition += -ClawSpeed, ServoMin);


        } else if (gamepad1.right_bumper) { //Close

            Bot.Intake.setPosition(Bot.ClawPosition);
            Bot.ClawPosition = Math.min(Bot.ClawPosition += ClawSpeed, ServoMax);

        } else { // Neutral (Hopefully)

            Bot.Intake.setPosition(Bot.ClawPosition);


        }
    }


    public void loop() {

        DriveTrainLoop();
        ArmControlLoop();
        ClawControl();
       // ExtenderLoop();

        if (Time.milliseconds() >= 250.0) {
            Bot.UpdateData();
            Time.reset();
        }


    }


}
