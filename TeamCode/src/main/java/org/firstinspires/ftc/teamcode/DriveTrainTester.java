package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "DriveTrainTester", group = "Utils")
public class DriveTrainTester extends OpMode {

    private DcMotor frontLeft, backLeft, frontRight, backRight;

    @Override
    public void init() {
        // Map motors to hardware names
        frontLeft = hardwareMap.get(DcMotor.class, "FrontLeftMotor");
        backLeft = hardwareMap.get(DcMotor.class, "BackLeftMotor");
        frontRight = hardwareMap.get(DcMotor.class, "FrontRightMotor");
        backRight = hardwareMap.get(DcMotor.class, "BackRightMotor");

        // Set directions
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        telemetry.addData("Status", "Ready to test. Use D-Pad.");
    }

    @Override
    public void loop() {
        // Testing Front Left - Dpad Up
        if (gamepad1.dpad_up) {
            frontLeft.setPower(0.5);
            telemetry.addData("Testing", "Front Left (UP)");
        } else {
            frontLeft.setPower(0);
        }

        // Testing Back Left - Dpad Down
        if (gamepad1.dpad_down) {
            backLeft.setPower(0.5);
            telemetry.addData("Testing", "Back Left (DOWN)");
        } else {
            backLeft.setPower(0);
        }

        // Testing Front Right - Dpad Right
        if (gamepad1.dpad_right) {
            frontRight.setPower(0.5);
            telemetry.addData("Testing", "Front Right (RIGHT)");
        } else {
            frontRight.setPower(0);
        }

        // Testing Back Right - Dpad Left
        if (gamepad1.dpad_left) {
            backRight.setPower(0.5);
            telemetry.addData("Testing", "Back Right (LEFT)");
        } else {
            backRight.setPower(0);
        }

        telemetry.update();
    }
}