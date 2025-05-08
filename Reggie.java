package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Reggie")
public class Reggie extends LinearOpMode {
    
    DcMotorEx m1, m2, m3, m4, highArm, yArm;
    Servo highClaw, lowClaw, xArm, clawRotator;
    
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
        //yArm = hardwareMap.get(DcMotorEx.class, "yArm"); // The motor controlling low arm on y Axis
    
        //Motor direction, flip to reverse direction of robot (may need to reorder motors)
        //m1.setDirection(DcMotor.Direction.REVERSE); //bl
        m2.setDirection(DcMotor.Direction.REVERSE); //br
        m3.setDirection(DcMotor.Direction.REVERSE); //fr
        m4.setDirection(DcMotor.Direction.REVERSE); //fl
        
        highArm.setDirection(DcMotor.Direction.REVERSE);
        //yArm.setDirection(DcMotor.Direction.REVERSE);
        
        
        
        m1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m4.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        highArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //yArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        
        //Reset the encoders on Init, and set them to the correct mode 
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        highArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //yArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        highArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //yArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        
        
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
        boolean isLogged2 = false;
        
        boolean isLoggedHighClaw = false;
        boolean isLoggedLowClaw = false;  
        boolean isLoggedHighArm = false;
      
        boolean isOpenHighClaw = false;
        boolean isOpenLowClaw = false;  
        int isOpenHighArm = 0;
        
        int controllerRotation = 0;
        
        boolean controllerLogged = false;
        
        // boolean manualMode = false;
      
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
            
            
            //Stealth Mode
            if(gamepad1.guide && isLogged == false){
                if(stealthMode == false) stealthMode = true;
                else if(stealthMode == true) stealthMode = false;
                isLogged = true;
                
            }else if (gamepad1.guide != true){
                isLogged = false;
            }
            
            //Arm Manual Mode
            // if(gamepad2.guide && isLogged2 == false){
            //     if(manualMode == false) manualMode = true;
            //     else if(manualMode == true) manualMode = false;
            //     isLogged = true;
                
            // } else if (gamepad2.guide != true){
            //     isLogged = false;
            // }
            
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
            if (gamepad2.dpad_right && !(xArm.getPosition() >= 1)){ //Max
                xArm.setPosition(xArm.getPosition() + 0.005);
            } else if (gamepad2.dpad_left && !(xArm.getPosition() <= 0.04)){ //Min
                xArm.setPosition(xArm.getPosition() - 0.005);
            } else {
                xArm.setPosition(xArm.getPosition());
            }
            
            
            //High Arm motor controls
            // if(manualMode == false){
                // highArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
            // } else {
            //     highArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            //     int rotatorPos = yArm.getCurrentPosition();
                
            //     if(gamepad2.left_stick_y == 0){
            //         if (!locked) {
            //             yArm.setTargetPosition(rotatorPos);
            //             yArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //             yArm.setPower(0.7);
            //             locked = true;
            //         }
            //     } else {
            //         yArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            //         yArm.setPower(gamepad2.left_stick_y*0.2);
            //         locked = false;
            //     }
            // }
            

            telemetry.addData("Current Arm Stage: ", isOpenHighArm-1);
            
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
            
            //int rotatorPos = yArm.getCurrentPosition();
            //telemetry.addData("rotatorPos: ", rotatorPos);
            
            //if(gamepad2.dpad_down){
            //    yArm.setPower(-1);
            //} else if (gamepad2.dpad_up){
            //    yArm.setPower(1);
            //} else if(gamepad2.dpad_left != true && gamepad2.dpad_right != true){
            //    yArm.setPower(0);
            //}
            
            
            
            telemetry.addData("xArm: ", xArm.getPosition());
            //telemetry.addData("yArm: ", yArm.getCurrentPosition());
            //telemetry.addData("yArm Target: ", yArm.getTargetPosition());
            telemetry.addData("armGoal: ", highArm.getTargetPosition());
            telemetry.addData("armLocation: ", highArm.getCurrentPosition());
            telemetry.addData("controller rotate: ", controllerRotation);
            
            telemetry.update();
        }
        //Stops all motors
        m1.setPower(0);
        m2.setPower(0);
        m3.setPower(0);
        m4.setPower(0);
    }
}
