package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="67")
public class JavasuxpythonONTOP extends OpMode {
    DcMotor MotorName;
    DcMotor Flyweel;
    DcMotor Sonic;
    public void init(){
        MotorName = hardwareMap.get(DcMotor.class, "Intake");
        Flyweel = hardwareMap.get(DcMotor.class, "Shooter");
        Sonic = hardwareMap.get(DcMotor.class, "FrontLeftMotor");
    }
    public void loop() {
        if(gamepad1.a){
            MotorName.setPower(1);
        } else {
            MotorName.setPower(0);

        }

        if(gamepad1.y){
            Flyweel.setPower(0.1);
        } else {
            Flyweel.setPower(0);
        }
    }
}
