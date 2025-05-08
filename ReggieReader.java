//imports and packages ;) -ethan
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ReggieReader")
public class ReggieReader extends LinearOpMode {
    
    DcMotorEx m1, m2, m3, m4, highArm, yArm;
    Servo highClaw, lowClaw, xArm, clawRotator;

    private List<String> instructions = new ArrayList<>(); // To store all instructions

    public boolean withinRange(int real, int goal) {
        return Math.abs(real-goal) < 8;
    }

    @Override
    public void runOpMode(){
        
        // Wheels, ordered accordingly:
        // Top Left, Back Left, Front Right, Back Right
        m1 = hardwareMap.get(DcMotorEx.class, "bl"); //back left
        m2 = hardwareMap.get(DcMotorEx.class, "br"); //back right
        m3 = hardwareMap.get(DcMotorEx.class, "fr"); //front right
        m4 = hardwareMap.get(DcMotorEx.class, "fl"); //front left
    
        lowClaw = hardwareMap.get(Servo.class, "lowClaw"); //The servo for the low claw
        highClaw = hardwareMap.get(Servo.class, "highClaw"); //The servo for the high claw
        xArm = hardwareMap.get(Servo.class, "xArm"); // The servo controlling low arm on x Axis
        
        
        clawRotator = hardwareMap.get(Servo.class, "clawRotator"); //The servo for the claw rotator
        
        //Declare Arms
        highArm = hardwareMap.get(DcMotorEx.class, "highArm"); //arm
        yArm = hardwareMap.get(DcMotorEx.class, "yArm"); // The motor controlling low arm on y Axis
    
        //Motor direction, flip to reverse direction of robot (may need to reorder motors)
        //m1.setDirection(DcMotor.Direction.REVERSE);
        m2.setDirection(DcMotor.Direction.REVERSE);
        m3.setDirection(DcMotor.Direction.REVERSE);
        m4.setDirection(DcMotor.Direction.REVERSE);
        
        highArm.setDirection(DcMotor.Direction.REVERSE);
        yArm.setDirection(DcMotor.Direction.REVERSE);
        
        
        
        m1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m4.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        highArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        yArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        
        //Reset the encoders on Init, and set them to the correct mode 
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        highArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        yArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        highArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        yArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        
        
        highClaw.setPosition(1);
        lowClaw.setPosition(0.1);
        clawRotator.setPosition(0);
        
        
        highArm.setTargetPosition(500);
        highArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        highArm.setPower(0.5);
        
        xArm.setPosition(0.37);
        
        
        telemetry.addData("Press Start When Ready","");
        telemetry.update();
        
        // Read the file during initialization
        try (BufferedReader br = new BufferedReader(new FileReader("sdcard/FIRST/java/src/org/firstinspires/ftc/teamcode/Instructions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                instructions.add(line);
            }
            telemetry.addData("Status", "Instructions loaded: " + instructions.size());
            telemetry.update();
        } catch (IOException e) {
            telemetry.addData("Error", "Failed to load instructions: " + e.getMessage());
            telemetry.update();
        }
        
        
        int endm1 = 0; int endm2 = 0; int endm3 = 0; int endm4 = 0; 
        int endHighArm = 0; double endClawRotator = 0.0; double endHighClaw = 0.0;
        double endXArm = 0.0; int endYArm = 0; double endLowClaw = 0.0;
        
        
        
        //Wait for the Play Button to be pressed
        waitForStart();
        
        //Main movement logic
        int instructionIndex = 0;
        
        while (opModeIsActive() && instructionIndex < instructions.size()) {
            
            endm1 = m1.getCurrentPosition(); //Front Left
            endm2 = m2.getCurrentPosition(); //Back Left
            endm3 = m3.getCurrentPosition(); //Front Right
            endm4 = m4.getCurrentPosition(); //Back Right
            endHighArm = highArm.getCurrentPosition(); // Arm
            endClawRotator = clawRotator.getPosition();
            endHighClaw = highClaw.getPosition();
            endXArm = xArm.getPosition();
            endYArm = yArm.getCurrentPosition();
            endLowClaw = lowClaw.getPosition();
            
            String[] values = instructions.get(instructionIndex).split(" ");
            
            // Parse the current instruction
            int m1P = Integer.parseInt(values[0]);
            int m2P = Integer.parseInt(values[1]);
            int m3P = Integer.parseInt(values[2]);
            int m4P = Integer.parseInt(values[3]);
            int highArmP = Integer.parseInt(values[4]);
            double clawRotatorP = Double.parseDouble(values[5]);
            double highClawP = Double.parseDouble(values[6]);
            double xArmP = Double.parseDouble(values[7]);
            int yArmP = Integer.parseInt(values[8]);
            double lowClawP = Double.parseDouble(values[9]);
            
            m1.setTargetPosition(m1P);
            m2.setTargetPosition(m2P);
            m3.setTargetPosition(m3P);
            m4.setTargetPosition(m4P);
            highArm.setTargetPosition(highArmP);
            yArm.setTargetPosition(yArmP);
            
            m1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m4.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            highArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            yArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            double driveSpeed = 0.2;
            
            m1.setPower(driveSpeed);
            m2.setPower(driveSpeed);
            m3.setPower(driveSpeed);
            m4.setPower(driveSpeed);
            highArm.setPower(0.8);
            yArm.setPower(1);
            clawRotator.setPosition(clawRotatorP);
            highClaw.setPosition(highClawP);
            xArm.setPosition(xArmP);
            lowClaw.setPosition(lowClawP);
            
            
            
            if(withinRange(endm1, m1P) &&
              withinRange(endm2, m2P) && 
              withinRange(endm3, m3P) &&
              withinRange(endm4, m4P) &&
              withinRange(endHighArm, highArmP) &&
              withinRange(endYArm, yArmP)){
                
                m1.setPower(0);
                m2.setPower(0);
                m3.setPower(0);
                m4.setPower(0);
                highArm.setPower(0);
                yArm.setPower(0);
                
                // if(gamepad2.left_stick_y == 0){
                //     if (!locked) {
                //         armRotator.setTargetPosition(rotatorPos);
                //         armRotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                //         armRotator.setPower(0.5);
                //         locked = true;
                //     }
                // } else {
                //     //controllerRotation = rotatorPos;
                //     armRotator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                //     armRotator.setPower(-gamepad2.left_stick_y*0.2);
                //     locked = false;
                // }
                
                instructionIndex++;
                sleep(50);
            }
            
            
            // Log the instruction
            // telemetry.addData("Instruction", 
            //     "m1P: %d, m2P: %d, m3P: %d, m4P: %d, armP: %d, armrotateP: %d, clawP: %.2f", //, clawrotateP: %.2f
            //     m1P, m2P, m3P, m4P, armP, armrotateP, clawP/*, clawrotateP*/);
            // telemetry.addData("Current Positions"," %d %d %d %d", 
            //   m1.getCurrentPosition(), m2.getCurrentPosition(), 
            //   m3.getCurrentPosition(), m4.getCurrentPosition(), 
            //   arm.getCurrentPosition(), armRotator.getCurrentPosition(),
            //   claw.getPosition()/*, clawRotator.getPosition()*/);
            telemetry.update();
            
        }
    }
}
