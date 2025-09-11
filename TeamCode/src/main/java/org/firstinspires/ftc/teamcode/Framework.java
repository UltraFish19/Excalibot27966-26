/*
* Purpose: Framework for Teleop and Autonomous
*
*
*/


package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles; //Import everything

public class Framework { // Main class for everything


    public DcMotor ArmMotor1;
    public DcMotor ArmMotor2;
    public CRServo Extender;
    public IMU IMUSensor;

    public Servo Intake;


    public DcMotor FrontLeftMotor;
    public DcMotor FrontRightMotor;
    public DcMotor BackLeftMotor;
    public DcMotor BackRightMotor;

    public HardwareMap Hardware;
    public double ClawPosition; // The position from 0 degrees
    public int ExtenderPosition;
    IMU.Parameters IMUParameters;
    Telemetry BotConsole;


    //Telemetry Data

    Telemetry.Item ExtenderPositionMessage;
    Telemetry.Item ClawPositionMessage; //The message appearing in the Driver hub console that states the Claw Position

    Telemetry.Item OrienYaw;
    Telemetry.Item OrienRoll;
    Telemetry.Item OrienPitch;

    Telemetry.Item EncoderValueMessage;


    final float WheelDiameter = 31.42f; // In cm


    final int TicksPerRotation = 1120;

    final double TicksPerCM = TicksPerRotation / WheelDiameter;


    private void ResetEncoder() {
        FrontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Make sure the encoder on the drivetrain is reset.
        FrontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BackLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BackRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        FrontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // Start the motor back up
        FrontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void Init(HardwareMap Hardware, Telemetry Telemetry) { // Will be used in Auto and TeleOp


        this.Hardware = Hardware;
        BotConsole = Telemetry;
        ClawPosition = 0.8;
        ExtenderPosition = 0;

        ArmMotor1 = Hardware.get(DcMotor.class, "ArmMotor1"); //Init Arm motors
        ArmMotor2 = Hardware.get(DcMotor.class, "ArmMotor2");

        Intake = Hardware.get(Servo.class, "intake"); //Claw
        Intake.setPosition(0.8);

        Extender = Hardware.get(CRServo.class, "Extender");

        FrontLeftMotor = Hardware.get(DcMotor.class, "FrontLeftMotor");
        FrontRightMotor = Hardware.get(DcMotor.class, "FrontRightMotor");
        BackLeftMotor = Hardware.get(DcMotor.class, "BackLeftMotor");
        BackRightMotor = Hardware.get(DcMotor.class, "BackRightMotor");

        IMUSensor = Hardware.get(IMU.class, "imu");

        FrontLeftMotor.setDirection(DcMotor.Direction.REVERSE); //Set Directions
        FrontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        BackLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        BackRightMotor.setDirection(DcMotor.Direction.FORWARD);

        ArmMotor1.setDirection(DcMotor.Direction.FORWARD);
        ArmMotor2.setDirection(DcMotor.Direction.REVERSE);


        FrontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //Set Zero Power Behaviour
        FrontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Intake.setDirection(Servo.Direction.REVERSE);

        ArmMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ArmMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ArmMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Run Encoders
        ArmMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        ClawPositionMessage = Telemetry.addData("Claw Position", ClawPosition);
        ExtenderPositionMessage = Telemetry.addData("Extender Position", ExtenderPosition);
        EncoderValueMessage = Telemetry.addData("Encoder Value", BackLeftMotor.getCurrentPosition());


        ResetEncoder();


        Log("Init successful!");

        SetupIMU(); // Setup Orientation Sensor


        OrienYaw = Telemetry.addData("Yaw", 0.0);
        OrienRoll = Telemetry.addData("Roll", 0.0);
        OrienPitch = Telemetry.addData("Pitch", 0.0);


    }


    public static class IMUAngle {
        double Yaw;
        double Roll;
        double Pitch;
    }


    private void SetupIMU() {


        IMUParameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )


        );

        IMUSensor.resetYaw(); // Reset back to 0


    }


    public IMUAngle GetIMUAngles() {


        YawPitchRollAngles Orientation;
        Orientation = IMUSensor.getRobotYawPitchRollAngles();


        IMUAngle Angle = new IMUAngle();

        Angle.Yaw = Orientation.getYaw(AngleUnit.DEGREES);

        Angle.Pitch = Orientation.getPitch(AngleUnit.DEGREES);

        Angle.Roll = Orientation.getRoll(AngleUnit.DEGREES);

        return Angle;


    }


    public void Log(String Message) {

        BotConsole.addLine(Message);
        BotConsole.update(); // Update so it actually appears

    }


    public void Sleep(int Millis) {
        try {
            Thread.sleep(Millis);
        } catch (Exception ignored) {
        }
    }


    // Auto functions


    public void UpdateData() {

        ClawPositionMessage.setValue(ClawPosition);
        ExtenderPositionMessage.setValue(ExtenderPosition);

        IMUAngle CurrentAngle = GetIMUAngles();

        OrienYaw.setValue(CurrentAngle.Yaw);
        OrienRoll.setValue(CurrentAngle.Roll);
        OrienPitch.setValue(CurrentAngle.Pitch);
        EncoderValueMessage.setValue(BackLeftMotor.getCurrentPosition());

    }


    public Auto AutoFramework = new Auto();

    public class Auto {

        @Deprecated
        public void MoveArm(int Time, boolean Up) {
            byte ArmDirection;

            if (Up) {
                ArmDirection = -1;
            } else {
                ArmDirection = 1;
            }


            ArmMotor1.setPower(0.1 * ArmDirection);
            ArmMotor1.setPower(0.1 * ArmDirection);

            Sleep(Time);

            ArmMotor1.setPower(0);
            ArmMotor1.setPower(0);

        }

        @Deprecated
        public void Turn(int Time, boolean Right) { // (2 Secs = 90 Degrees) = WRONG

            byte RightDirection;
            byte LeftDirection;


            if (Right) {
                RightDirection = -1;
                LeftDirection = 1;
            } else {
                RightDirection = 1;
                LeftDirection = -1;
            }


            FrontLeftMotor.setPower(0.5 * LeftDirection);
            FrontRightMotor.setPower(0.5 * RightDirection);

            Sleep(Time);

            FrontLeftMotor.setPower(0);
            FrontRightMotor.setPower(0);


        }

        @Deprecated
        public void MoveStraight(int Time, boolean Forward) {

            byte Direction; // Number between -255 and 255


            if (Forward) {
                Direction = 1;
            } else {
                Direction = -1;
            }


            FrontLeftMotor.setPower(0.5 * Direction);
            FrontRightMotor.setPower(0.5 * Direction);

            Sleep(Time);

            FrontLeftMotor.setPower(0);
            FrontRightMotor.setPower(0);

        }

        private int NormalizeAngle(int Angle) {
            Angle = Angle % 360;


            if (Angle > 180) {
                Angle -= 360;
            } else if (Angle < -180) {
                Angle += 360;
            }

            return Angle;
        }


        public void SetAngle(int SetAngleTo, Boolean Left,Double Speed) {
            ElapsedTime UpdateDataTimer = new ElapsedTime();

            if (Left == null) {
                Left = true;
            }
            if (Speed == null) {
                Speed = 0.25;
            }

            float Direction = 1;

            if (Left) {
                Direction = -1;
            }


            SetAngleTo = Math.max(Math.min(SetAngleTo, 180), -180);

            while (Math.round(GetIMUAngles().Yaw) != SetAngleTo) {


                FrontLeftMotor.setPower(-Speed * Direction);
                BackLeftMotor.setPower(Speed * Direction);
                FrontRightMotor.setPower(Speed * Direction);
                BackRightMotor.setPower(-Speed * Direction);
                Sleep(10);
            }

            Log("Turned " + Integer.toString(SetAngleTo) + " Degrees");
            FrontLeftMotor.setPower(0);
            BackLeftMotor.setPower(0);
            FrontRightMotor.setPower(0);
            BackRightMotor.setPower(0);


        }


        public void Move(float Distance) {
        /*
        Set the robot's distance to a value (In cm)
         */

            ResetEncoder();

            double Ticks = Distance * TicksPerCM;

            while (Math.abs(BackLeftMotor.getCurrentPosition()) <= Ticks ) {
                FrontLeftMotor.setPower(-0.5);
                FrontRightMotor.setPower(-0.5);
                BackLeftMotor.setPower(-0.5);
                BackRightMotor.setPower(-0.5);

                Sleep(10);
            }

            FrontLeftMotor.setPower(0);
            FrontRightMotor.setPower(0);
            BackLeftMotor.setPower(0);
            BackRightMotor.setPower(0);

            Log("Moved " + Float.toString(Distance) + "cm");


        }


        public void SetArm(double TimedFor) {



            ArmMotor1.setPower(-0.1);
            ArmMotor2.setPower(-0.1);

            Sleep((int) Math.round(TimedFor * 1000));

            ArmMotor1.setPower(0);
            ArmMotor2.setPower(0);

            Log("Arm in motion for " + Double.toString(TimedFor) + " secs");

        }

        public void SetClaw(double SetTo) {
            Intake.setPosition(SetTo);
            ClawPosition = SetTo;
            Log("Set Claw position to " + Double.toString(SetTo));

        }


        public void MoveSideways(float Distance, Boolean Left) { // WARNING: Avoid using for long distanced without adjusting angle
        /*
        Set the robot's distance sideways to a value (In cm)
         */
            if (Left == null) {
                Left = true;
            }

            float Direction = 1;

            if (Left) {
                Direction = -1;
            }

            ResetEncoder();

            double Ticks = Distance * TicksPerCM;

            while (Math.abs(BackLeftMotor.getCurrentPosition()) <= Ticks ) {
                FrontLeftMotor.setPower(-0.5 * Direction);
                FrontRightMotor.setPower(0.5 * Direction);
                BackLeftMotor.setPower(-0.5 * Direction);
                BackRightMotor.setPower(0.5 * Direction);

                Sleep(10);
            }

            FrontLeftMotor.setPower(0);
            FrontRightMotor.setPower(0);
            BackLeftMotor.setPower(0);
            BackRightMotor.setPower(0);

            Log("Moved " + Float.toString(Distance) + "cm");


        }

    }


}
 

    
    


    
