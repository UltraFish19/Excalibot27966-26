/*
* Purpose: Framework for Teleop and Autonomous
*
*
*/


package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles; //Import everything
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;


public class Framework { // Main class for everything


   public IMU IMUSensor;



    public DcMotor FrontLeftMotor;
    public DcMotor FrontRightMotor;
    public DcMotor BackLeftMotor;
    public DcMotor BackRightMotor;
    public DcMotor Intake; // Intake to take balls in
    public DcMotorEx Shooter;  //To shoot the ball
    public HardwareMap Hardware;

    IMU.Parameters IMUParameters;
    Telemetry BotConsole;


    //Telemetry Data


    Telemetry.Item OrienYaw;
    Telemetry.Item OrienRoll;
    Telemetry.Item OrienPitch;
    Telemetry.Item EncoderValueMessage;
    Telemetry.Item BasketRange;

    Double Range;




    final float WheelDiameter = 31.42f; // In cm
    final float TicksPerRotation = 537.6f;
    final float ShooterTicksPerRotation = 117;
    final double TicksPerCM = TicksPerRotation / WheelDiameter;

    final double SweetSpot = 132;
    final double SweetSpotTolerance = 0.2;





    public static class MotorParams{
        public static DriveTrainParams  StraightParams = new DriveTrainParams(-1,-1,-1,-1);
        public static DriveTrainParams RotateParams = new DriveTrainParams(1,-1,1,-1);
        public static DriveTrainParams CrabwalkParams = new DriveTrainParams(1,-1,-1,1);
    }








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


        FrontLeftMotor = Hardware.get(DcMotor.class, "FrontLeftMotor");
        FrontRightMotor = Hardware.get(DcMotor.class, "FrontRightMotor");
        BackLeftMotor = Hardware.get(DcMotor.class, "BackLeftMotor");
        BackRightMotor = Hardware.get(DcMotor.class, "BackRightMotor");

        Intake = Hardware.get(DcMotor.class,"Intake");
        Intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // We do not need a motor for something that spins mindlessly

        Shooter = Hardware.get(DcMotorEx.class,"Shooter");
        Shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        IMUSensor = Hardware.get(IMU.class, "imu");

        FrontLeftMotor.setDirection(DcMotor.Direction.REVERSE); //Set Directions
        FrontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        BackLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        BackRightMotor.setDirection(DcMotor.Direction.FORWARD);




        FrontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //Set Zero Power Behaviour
        FrontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        ResetEncoder();


        Log("Init successful!");

        SetupIMU(); // Setup Orientation Sensor


        OrienYaw = Telemetry.addData("Yaw", 0.0);
        OrienRoll = Telemetry.addData("Roll", 0.0);
        OrienPitch = Telemetry.addData("Pitch", 0.0);
        EncoderValueMessage = Telemetry.addData("Shooter Speed",0.0);
        BasketRange = Telemetry.addData("Range: ", "Unknown, Tag not detected!");

    }


    VisionPortal Vision;
    AprilTagProcessor ATagProcessor; //April tag processer
    public void SetupCamera(){
        AprilTagProcessor.Builder ATagBuilder = new AprilTagProcessor.Builder();

        ATagBuilder.setDrawTagID(true);

        ATagProcessor = ATagBuilder.build();

        VisionPortal.Builder VisionBuilder = new VisionPortal.Builder();

        VisionBuilder.setCamera(Hardware.get(WebcamName.class,"FrontCam"));
        VisionBuilder.addProcessor(ATagProcessor);
        VisionBuilder.setCameraResolution(new Size(640,480));
        VisionBuilder.enableLiveView(true);
        VisionBuilder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);

        Vision = VisionBuilder.build();






    }


    public static class IMUAngle {
        double Yaw;
        double Roll;
        double Pitch;
    }


    private void SetupIMU() {


        IMUParameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                )


        );

        IMUSensor.initialize(IMUParameters);

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

    public double GetRange(){ // Returns -1 for no detection, -2 for multiple, -3 for error, -4 invalid ftc pose. // Returns range in cm

        try{

            ArrayList<AprilTagDetection> Detections = ATagProcessor.getDetections();
            AprilTagDetection FirstDetection;

            if (Detections.isEmpty()){
                return -1;
            } else if (Detections.size() > 1) {
                return -2;
            } else{
                FirstDetection = Detections.get(0);

                if( FirstDetection.ftcPose != null){
                   return (FirstDetection.ftcPose.range * 2.54) * 1.12;
                } else {
                    return  -4;
                }
            }


        } catch(Exception E){
            return -3;

        }




    }



    public void UpdateData() {



        IMUAngle CurrentAngle = GetIMUAngles();

        OrienYaw.setValue(CurrentAngle.Yaw);
        OrienRoll.setValue(CurrentAngle.Roll);
        OrienPitch.setValue(CurrentAngle.Pitch);
        EncoderValueMessage.setValue((Shooter.getVelocity() / 117) * 60);

        double TagRange = GetRange();

        if (TagRange == -1){
            BasketRange.setValue("No tag detected!");
            Range = null;
        } else if (TagRange == -2) {
            BasketRange.setValue("Multiple tags detected!");
            Range = null;
        } else if (TagRange == -4) {

            BasketRange.setValue("Can't fully decode tag, please align the camera more!");
            Range = null;
        } else {
            Range = TagRange;
            BasketRange.setValue(String.valueOf(Range) + "CM");

        }




    }


    public Auto AutoFramework = new Auto();

    public class Auto {


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


            FrontLeftMotor.setPower(0.25 * Direction);
            FrontRightMotor.setPower(0.25 * Direction);

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
                BackLeftMotor.setPower(-Speed * Direction);
                FrontRightMotor.setPower(Speed * Direction);
                BackRightMotor.setPower(Speed * Direction);
                Sleep(10);
            }

            Log("Turned " + Integer.toString(SetAngleTo) + " Degrees");
            FrontLeftMotor.setPower(0);
            BackLeftMotor.setPower(0);
            FrontRightMotor.setPower(0);
            BackRightMotor.setPower(0);


        }


        public double GetShooterMotorRPM(){

            double OriginalPos = Shooter.getCurrentPosition();

            Sleep(100);

            double Delta = OriginalPos - Shooter.getCurrentPosition();

            double RPS = (Delta * 10) / ShooterTicksPerRotation;

            return RPS * 60;
        }


        public void Move(float Distance,double Speed) {
        /*
        Set the robot's distance to a value (In cm)
         */

            ResetEncoder();

            double Ticks = Distance * TicksPerCM;

            while (Math.abs(BackLeftMotor.getCurrentPosition()) <= Ticks ) {
                FrontLeftMotor.setPower(Speed);
                FrontRightMotor.setPower(Speed);
                BackLeftMotor.setPower(Speed);
                BackRightMotor.setPower(Speed);

                Sleep(10);
            }

            FrontLeftMotor.setPower(0);
            FrontRightMotor.setPower(0);
            BackLeftMotor.setPower(0);
            BackRightMotor.setPower(0);

            Log("Moved " + Float.toString(Distance) + "cm");


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


        public void Shoot(){
            Shooter.setPower(1.0);

            Sleep(5000); // Wait 5 sec

            Intake.setPower(-1.0);

            Sleep(3000);

            Intake.setPower(0);
            Shooter.setPower(0);
        }

    }


}
 

    
    


    
