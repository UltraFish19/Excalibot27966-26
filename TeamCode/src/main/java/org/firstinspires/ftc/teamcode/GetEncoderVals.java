package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "EncoderFinder", group = "Utils")
public class GetEncoderVals extends OpMode {

    // Define the four motors
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;

    @Override
    public void init() {
        // Initialize motors from the hardware map
        frontLeft = hardwareMap.get(DcMotor.class, "FrontLeftMotor");
        backLeft = hardwareMap.get(DcMotor.class, "BackLeftMotor");
        frontRight = hardwareMap.get(DcMotor.class, "FrontRightMotor");
        backRight = hardwareMap.get(DcMotor.class, "BackRightMotor");

        // Set directions as requested
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        // Tell the drivers the robot is ready
        telemetry.addData("Status", "Initialized. Encoders ready.");
    }

    @Override
    public void loop() {
        // Retrieve and display encoder values
        telemetry.addData("Front Left Encoder", frontLeft.getCurrentPosition());
        telemetry.addData("Back Left Encoder", backLeft.getCurrentPosition());
        telemetry.addData("Front Right Encoder", frontRight.getCurrentPosition());
        telemetry.addData("Back Right Encoder", backRight.getCurrentPosition());

        // Update the telemetry screen
        telemetry.update();
    }
}