package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
//import org.firstinspires.ftc.robotcore.util.ElapsedTime;

@Autonomous

public class DontKnowAuto extends LinearOpMode {
    
    DcMotorEx m1, m2, m3, m4;

    private void drive(double py, double px, double pa) {
                
        //py is the power of the left stick on the y axis (vertical movement)
        // Down to Up, -1.00 to 1.00
        
        //px is the power of the left stick on the x axis (horizontal movement)
        // Left to Right, -1.00 to 1.00
        
        //pa is the power of the body rotation
        
        double p1 = -px + py - pa; //bl
        double p2 = px + py + pa; //br
        double p3 = -px + py + pa; //fr
        double p4 = px + py - pa; //fl
        double max = Math.max(1.0, Math.abs(p1));
        max = Math.max(max, Math.abs(p2));
        max = Math.max(max, Math.abs(p3));
        max = Math.max(max, Math.abs(p4));
        p1 /= max;
        p2 /= max;
        p3 /= max;
        p4 /= max;
        m1.setPower(p1);
        m2.setPower(p2);
        m3.setPower(p3);
        m4.setPower(p4);
    }
    

    @Override
    public void runOpMode() throws InterruptedException {
        
        
        // Wheels, ordered accordingly:
        // Top Left, Back Left, Front Right, Back Right
        m1 = hardwareMap.get(DcMotorEx.class, "bl"); //back left
        m2 = hardwareMap.get(DcMotorEx.class, "br"); //back right
        m3 = hardwareMap.get(DcMotorEx.class, "fr"); //front right
        m4 = hardwareMap.get(DcMotorEx.class, "fl"); //front left
    
        //Motor direction, flip to reverse direction of robot (may need to reorder motors)
        //m1.setDirection(DcMotor.Direction.REVERSE);
        m2.setDirection(DcMotor.Direction.REVERSE);
        m3.setDirection(DcMotor.Direction.REVERSE);
        m4.setDirection(DcMotor.Direction.REVERSE);
        
        //not going to lie i have no idea what this stuff is 
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        m1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m4.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        telemetry.addData("Press Start When Ready","");
        telemetry.update();

        // HERE STARTS AUTO
        
        // Position of the arm when it's lifted
      int armUpPosition = 1000;

      // Position of the arm when it's down
      int armDownPosition = 0;

      // Find a motor in the hardware map named "Arm Motor"
      DcMotor armMotor = hardwareMap.dcMotor.get("Arm Motor");

      // Reset the motor encoder so that it reads zero ticks
      armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

      // Sets the starting position of the arm to the down position
      armMotor.setTargetPosition(armDownPosition);
      armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

      waitForStart();



    }
}
