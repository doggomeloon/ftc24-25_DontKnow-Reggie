package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

@TeleOp(name="ReggieWriter")
public class ReggieWriter extends LinearOpMode {
    
    DcMotorEx m1, m2, m3, m4, highArm, yArm;
    Servo highClaw, lowClaw, xArm, clawRotator;
    
    BufferedWriter out = null;
    
    DateFormat dateFormat = new SimpleDateFormat("MM/dd 'at' HH:mm:ss");
    Date date = new Date();

    public void writeInstruction(String text){
        try {
            out.write(text);
            out.write("\n");
            out.flush();
        } catch (IOException r){
            r.printStackTrace();
        }
    }
    
    @Override
    public void runOpMode(){
        
        try {
            FileWriter fstream = new FileWriter("sdcard/FIRST/java/src/org/firstinspires/ftc/teamcode/Instructions.txt", true);
            out = new BufferedWriter(fstream);
            out.write("\n----------NEW INSTRUCTIONS " + dateFormat.format(date) + "----------\n\n\n");
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        
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
        
        boolean locked = false;
        boolean stealthMode = false;
        
        boolean isLogged = false;
        
        boolean isLoggedHighClaw = false;
        boolean isLoggedLowClaw = false;  
        boolean isLoggedHighArm = false;
      
        boolean isOpenHighClaw = false;
        boolean isOpenLowClaw = false;  
        int isOpenHighArm = 0;
        
        int controllerRotation = 0;
        
        boolean controllerLogged = false;
        
        //Starting values initialized
        int startm1 = 0; int startm2 = 0; int startm3 = 0; int startm4 = 0; 
        int startHighArm = 0; double startClawRotator = 0.0; double startHighClaw = 0.0;
        double startXArm = 0.0; int startYArm = 0; double startLowClaw = 0.0;
        
        int endm1 = 0; int endm2 = 0; int endm3 = 0; int endm4 = 0; 
        int endHighArm = 0; double endClawRotator = 0.0; double endHighClaw = 0.0;
        double endXArm = 0.0; int endYArm = 0; double endLowClaw = 0.0;
        
        double speed = 0.2;
        
        
        //Starting instruction value
        int currentInstruction = 1;
        
        
        waitForStart();
        

        while (opModeIsActive()) {

                // BODY MOVEMENT
            double px = -gamepad1.left_stick_x; //The power of the left stick on the x axis
                                                // Left to Right, -1.00 to 1.00
            double py = gamepad1.left_stick_y; //The power of the left stick on the y axis
                                                // Down to Up, -1.00 to 1.00
            double pa = gamepad1.left_trigger - gamepad1.right_trigger;
                                                //The power of the body rotation

            //math equation stuff...
            
            //p1 correlates to m1
            //p2 correlates to m2
            // etc.
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
            
            
            //Updated Locations
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
            
            if(gamepad1.guide && isLogged == false){
            //Starting values initialized
            // int startm1 = 0; int startm2 = 0; int startm3 = 0; int startm4 = 0; 
            // int startarm = 0; double startClawRotator = 0.0; double startHighClaw = 0.0;
            // double startXArm = 0.0; int startYArm = 0; double startLowClaw = 0.0;
                                
                writeInstruction(endm1 + " " + endm2 + " " + endm3 + " " + endm4
                                +" "+endHighArm
                                +" "+(int)(endClawRotator * 100) / 100.0
                                +" "+(int)(endHighClaw * 100) / 100.0
                                +" "+(int)(endXArm * 100) / 100.0 +" "+endYArm
                                +" "+(int)(endLowClaw * 100) / 100.0
                                );
                
                startm1 = endm1; startm2 = endm2; startm3 = endm3; startm4 = endm4; 
                startHighArm = endHighArm; startClawRotator = endClawRotator; startHighClaw = endHighClaw;
                startXArm = endXArm; startYArm = endYArm; startLowClaw = endLowClaw;
                
                currentInstruction++;
                isLogged = true;
                
            } else if (gamepad1.guide != true){ //prevents repetition
                isLogged = false;
            }
            
            
            stealthMode = false;
            
            if(stealthMode == true){
                m1.setPower(p1 * 0.1);
                m2.setPower(p2 * 0.1);
                m3.setPower(p3 * 0.1);
                m4.setPower(p4 * 0.1);
            }else {
                m1.setPower(p1 * 0.7);
                m2.setPower(p2 * 0.7);
                m3.setPower(p3 * 0.7);
                m4.setPower(p4 * 0.7);
            }
            
            //Display Positions of the motors
            telemetry.addData("Motor Encoders"," %d %d %d %d", m1.getCurrentPosition(), m2.getCurrentPosition(),
                    m3.getCurrentPosition(), m4.getCurrentPosition());
          
            telemetry.addData("High Claw: ", isOpenHighClaw);
            telemetry.addData("Low Claw: ", isOpenLowClaw);
            telemetry.addData("High Arm: ", isOpenHighArm);
          
          
            //High claw servo controls
            if(gamepad2.left_bumper && isLoggedHighClaw == false){ // High claw
                if(isOpenHighClaw ==  false){ //Openng
                  isOpenHighClaw = true;
                  highClaw.setPosition(0.6);
                } else if(isOpenHighClaw == true){//Closing
                  isOpenHighClaw = false;
                  highClaw.setPosition(1);
                }
                isLoggedHighClaw = true;
            } else if (gamepad2.left_bumper != true){//
                isLoggedHighClaw = false;
            }
            
            //Low claw servo controls
            if(gamepad2.right_bumper && isLoggedLowClaw == false){ // Low Claw
              if(isOpenLowClaw == false){ //Opening
                isOpenLowClaw = true;
                lowClaw.setPosition(0.1);
              } else if(isOpenLowClaw == true){ //Closing
                isOpenLowClaw = false;
                lowClaw.setPosition(0.25);
              }
              isLoggedLowClaw = true;
            } else if (gamepad2.right_bumper != true){ //
                  isLoggedLowClaw = false;
            }
            
            //lowX Controls
            if (gamepad2.dpad_right && !(xArm.getPosition() >= 0.37)){ //Max
                xArm.setPosition(xArm.getPosition() + 0.005);
            } else if (gamepad2.dpad_left && !(xArm.getPosition() <= 0.04)){ //Min
                xArm.setPosition(xArm.getPosition() - 0.005);
            } else {
                xArm.setPosition(xArm.getPosition());
            }
            
            
            //High Arm motor controls
            if(gamepad2.x && isLoggedHighArm == false){ // High Arm 
                if(isOpenHighArm == 0){
                    highArm.setTargetPosition(2250);
                    highArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    highArm.setPower(0.8);
                    isOpenHighArm = 1;
                    clawRotator.setPosition(0.73);
                } else if(isOpenHighArm == 1){
                    highArm.setTargetPosition(1600);
                    highArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    highArm.setPower(0.8);
                    isOpenHighArm = 2;
                    clawRotator.setPosition(0.0);
                } else if(isOpenHighArm == 2){
                    highArm.setTargetPosition(750);
                    highArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    highArm.setPower(0.8);
                    //clawRotator.setPosition(0.0);
                    isOpenHighArm = 0;
                }
                isLoggedHighArm = true;
            } else if (gamepad2.x != true){
                isLoggedHighArm = false;
            }

            telemetry.addData("Current Arm Stage: ", isOpenHighArm);
            

            
            // yArm Motor Controls
            // if (gamepad2.left_stick_y != 1289037){ //Max
            //   yArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            //     yArm.setPower(gamepad2.left_stick_y);
            //     controllerLogged = false;
            // } else if (gamepad2.left_stick_y == 0){
            //     if(controllerLogged == false){
            //         controllerRotation = yArm.getCurrentPosition();
            //         controllerLogged = true;
            //     }
                
            //     yArm.setTargetPosition(controllerRotation);
            //     yArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //     yArm.setPower(0.8);
            // }
            
            int rotatorPos = yArm.getCurrentPosition();
            telemetry.addData("rotatorPos: ", rotatorPos);
            
            if(gamepad2.dpad_down){
                yArm.setPower(-0.5);
            } else if (gamepad2.dpad_up){
                yArm.setPower(0.5);
            } else if(gamepad2.dpad_left != true && gamepad2.dpad_right != true){
                yArm.setPower(0);
            }
            
            
            
            telemetry.addData("xArm: ", xArm.getPosition());
            telemetry.addData("yArm: ", yArm.getCurrentPosition());
            telemetry.addData("yArm Target: ", yArm.getTargetPosition());
            telemetry.addData("armGoal: ", highArm.getTargetPosition());
            telemetry.addData("armLocation: ", highArm.getCurrentPosition());
            telemetry.addData("controller rotate: ", controllerRotation);
            
            telemetry.update();
        }
        
        try {
            out.flush();
            out.close();
        } catch (IOException u){
            u.printStackTrace();
        }
    }
}
