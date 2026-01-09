/*
* Purpose: Framework for Teleop and Autonomous
*
*
*/


package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import android.util.Size;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles; //Import everything
import org.firstinspires.ftc.teamcode.Utils.DriveTrainParams;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import java.util.ArrayList;

import java.util.List;

import org.firstinspires.ftc.teamcode.Utils.FixedSizeList;


public class Framework { // Main class for everything


   public IMU IMUSensor;



    public DcMotor FrontLeftMotor;
    public DcMotor FrontRightMotor;
    public DcMotor BackLeftMotor;
    public DcMotor BackRightMotor;
    public DcMotor Intake; // Intake to take balls in
    public DcMotorEx Shooter;  //To shoot the ball
    public HardwareMap Hardware;
    List<LynxModule> ConnectedHubs;

    IMU.Parameters IMUParameters;
    Telemetry BotConsole;


    //Telemetry Data


    Telemetry.Item OrienYaw;
    Telemetry.Item OrienRoll;
    Telemetry.Item OrienPitch;
    Telemetry.Item ShooterRPMMessage;
    Telemetry.Item BasketRange;

    FixedSizeList<Double> ShooterRPMVals = new FixedSizeList<>(5); // The number inputted is the amount of data that will be used to average the values

    Double Range;




    final float WheelDiameter = 31.42f; // In cm
    final float TicksPerRotation = 537.6f;
    final float TicksPerIntakeHalfRotation =  537.6f / 2;
    final float ShooterTicksPerRotation = 117;
    final double TicksPerCM = TicksPerRotation / WheelDiameter;

    final double SweetSpot = 132;
    final double SweetSpotTolerance = 4;

    final static float FlywheelRPM = 1200;



    public static class MotorParams{
        public static DriveTrainParams StraightParams = new DriveTrainParams(1,1,1,1);
        public static DriveTrainParams RotateParams = new DriveTrainParams(-1,1,-1,1);
        public static DriveTrainParams CrabwalkParams = new DriveTrainParams(-1,1,1,-1);
    }

    public static class MoveDirection{
        static byte STRAIGHT = 1;
        static byte ROTATE = 2;
        static byte CRABWALK = 3;
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
        Intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Shooter = Hardware.get(DcMotorEx.class,"Shooter");
        Shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        IMUSensor = Hardware.get(IMU.class, "imu");

        FrontLeftMotor.setDirection(DcMotor.Direction.FORWARD); //Set Directions
        FrontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        BackLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        BackRightMotor.setDirection(DcMotor.Direction.REVERSE);

        FrontLeftMotorOutputs = new ArrayList<>();
        FrontRightMotorOutputs = new ArrayList<>();
        BackLeftMotorOutputs = new ArrayList<>();
        BackRightMotorOutputs = new ArrayList<>();




        FrontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //Set Zero Power Behaviour
        FrontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        ResetEncoder();
        ConnectedHubs = Hardware.getAll(LynxModule.class);
        Log("Init successful!");

        SetupIMU(); // Setup Orientation Sensor


        OrienYaw = Telemetry.addData("Yaw", 0.0);
        OrienRoll = Telemetry.addData("Roll", 0.0);
        OrienPitch = Telemetry.addData("Pitch", 0.0);
        ShooterRPMMessage = Telemetry.addData("Shooter Speed","0.0");
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

    public boolean ValueInTolerance(double Target, double Actual, double Tolerance) {
        return Math.abs(Target - Actual) <= Tolerance;
    }

    public static class IMUAngle {
        double Yaw;
        double Roll;
        double Pitch;
    }

    public void SetIndicatorLight(int Color) {
        for (LynxModule Hub : ConnectedHubs) {
            Hub.setConstant(Color);
        }
    }




    public void AlignIntake(){ // This function will align the intake so it can be easier to shoot 2 balls
        int IntakePos = Intake.getCurrentPosition();

        float IntakeRotations = IntakePos / TicksPerIntakeHalfRotation;

        IntakeRotations = Math.round(IntakeRotations);


        Intake.setTargetPosition((int) (IntakeRotations * TicksPerIntakeHalfRotation));






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
        System.out.println(Message); // Print to log cat
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
        double ShooterRPM = (Shooter.getVelocity() / 117) * 60;
        ShooterRPMVals.add(ShooterRPM);
        double AverageRPM = JavaUtil.averageOfList(ShooterRPMVals);
        ShooterRPMMessage.setValue("Avg: " + AverageRPM + "Current: " + ShooterRPM);




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
            BasketRange.setValue(Range + "CM");

        }


        if (ValueInTolerance(AverageRPM, FlywheelRPM, 75)){
                SetIndicatorLight(Color.MAGENTA);
        } else if (Range != null) {

            if (ValueInTolerance(Range, SweetSpot, SweetSpotTolerance))
                SetIndicatorLight(Color.RED);
            else {
                SetIndicatorLight(Color.CYAN);
            }

        }

        else {
            SetIndicatorLight(0x90EE90);
        }





    }


    List<Float> FrontLeftMotorOutputs;
    List<Float> FrontRightMotorOutputs;
    List<Float> BackLeftMotorOutputs;
    List<Float> BackRightMotorOutputs;

    public void Move(float Speed, byte Action) {
        DriveTrainParams MotorParameter; // The correct directions of the motors

        switch (Action) {
            case 1:
                MotorParameter = MotorParams.StraightParams;
                break;

            case 2:
                MotorParameter = MotorParams.RotateParams;
                break;

            case 3:
                MotorParameter = MotorParams.CrabwalkParams;
                break;

            default:
                throw new RuntimeException("Invalid Movement Action for drive train.");
        }
        FrontLeftMotorOutputs.add(Speed * MotorParameter.FrontLeft);
        FrontRightMotorOutputs.add(Speed * MotorParameter.FrontRight);
        BackLeftMotorOutputs.add(Speed * MotorParameter.BackLeft);
        BackRightMotorOutputs.add(Speed * MotorParameter.BackRight);

    }



    public void ResetDriveTrainMotors(){
        FrontLeftMotorOutputs = new ArrayList<>();
        FrontRightMotorOutputs = new ArrayList<>();
        BackLeftMotorOutputs = new ArrayList<>();
        BackRightMotorOutputs = new ArrayList<>();
    }



    public void UpdateDriveTrainMotors(){
        FrontLeftMotor.setPower(JavaUtil.averageOfList(FrontLeftMotorOutputs));
        FrontRightMotor.setPower(JavaUtil.averageOfList(FrontRightMotorOutputs));
        BackLeftMotor.setPower(JavaUtil.averageOfList(BackLeftMotorOutputs));
        BackRightMotor.setPower(JavaUtil.averageOfList(BackRightMotorOutputs));
    }

    public AutoFramework Auto = new AutoFramework();

    public class AutoFramework {


        private AutoFramework() {} // Prevent it from appearing outside of this.


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

            Log("Turned " + SetAngleTo + " Degrees");
            FrontLeftMotor.setPower(0);
            BackLeftMotor.setPower(0);
            FrontRightMotor.setPower(0);
            BackRightMotor.setPower(0);


        }


        public void MoveStraight(float Distance, float Speed) {
        /*
        Set the robot's distance to a value (In cm)
         */

            ResetEncoder();

            Speed = Speed * -1; // For whatever fucking java reason, the motor spins in the opposite direction when using auto.

            double Ticks = Distance * TicksPerCM;

            ResetDriveTrainMotors();

            while (Math.abs(BackLeftMotor.getCurrentPosition()) <= Ticks ) {
                Move(Speed,MoveDirection.STRAIGHT);

                UpdateDriveTrainMotors();

                Sleep(10);
            }


            ResetDriveTrainMotors();


            Move(0,MoveDirection.STRAIGHT);

            UpdateDriveTrainMotors();

            Log("Moved " + Distance + "cm");


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

            Log("Moved " + Distance + "cm");


        }


        public void Shoot(){ // This will shoot two balls
            Shooter.setVelocity((Framework.FlywheelRPM / 60) * ShooterTicksPerRotation);

            Sleep(5000); // Wait 5 sec

            Intake.setPower(-1.0);

            Sleep(50);

            Intake.setPower(0);

            Sleep(400);

            Intake.setPower(-1.0);





            Sleep(2000);

            Intake.setPower(0);
            Shooter.setVelocity(0);
        }

    }
}
