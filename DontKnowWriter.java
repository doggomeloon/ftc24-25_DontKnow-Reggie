//imports and packages ;) -ethan
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="DontKnowWriter")
public class DontKnowWriter extends LinearOpMode {
    
    DcMotorEx m1, m2, m3, m4, arm, armRotator;
    Servo claw/*, clawRotator*/;
    
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
            telemetry.addData("failed", true);
        }
        
        
        
        // Wheels, ordered accordingly:
        // Top Left, Back Left, Front Right, Back Right
        m1 = hardwareMap.get(DcMotorEx.class, "bl"); //back left
        m2 = hardwareMap.get(DcMotorEx.class, "br"); //back right
        m3 = hardwareMap.get(DcMotorEx.class, "fr"); //front right
        m4 = hardwareMap.get(DcMotorEx.class, "fl"); //front left
    
        claw = hardwareMap.get(Servo.class, "claw"); //claw servo
        //clawRotator = hardwareMap.get(Servo.class, "clawRotator"); //claw rotator servo
        
        //Declare Arm
        arm = hardwareMap.get(DcMotorEx.class, "arm"); //arm
        armRotator = hardwareMap.get(DcMotorEx.class, "armRotator"); //arm
    
        //Motor direction, flip to reverse direction of robot (may need to reorder motors)
        m1.setDirection(DcMotor.Direction.REVERSE);
        //m2.setDirection(DcMotor.Direction.REVERSE);
        //m3.setDirection(DcMotor.Direction.REVERSE);
        //m4.setDirection(DcMotor.Direction.REVERSE);
        
        arm.setDirection(DcMotor.Direction.REVERSE);
        armRotator.setDirection(DcMotor.Direction.REVERSE);
        
        m1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m4.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armRotator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        
        //Reset the encoders on Init, and set them to the correct mode 
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); 
        
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRotator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //arm.setTargetPosition(0);
        //armRotator.setPosition()
        //arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //armRotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        claw.setPosition(0.45);
        //clawRotator.setPosition(0.5);
        
        //clawRotator.scaleRange(0.0, 0.75);
        
        
        telemetry.addData("Press Start When Ready","");
        telemetry.update();
        
        //Max/Min Arm Positions
        int maxArmPos = 6450;
        int minArmPos = 150;
        int maxRotatorPos = 1450;
        int minRotatorPos = 20;
        
        int controllerRotation = 0;
        
        boolean locked = false;
        
        //double clawRotation = clawRotator.getPosition();
        
        
        //Starting values initialized
        int startm1 = 0; int startm2 = 0; int startm3 = 0; int startm4 = 0; 
        int startarm = 0; int startarmrotate = 0;
        double startclaw = 0; //double startclawrotate = 0;
        
        int endm1 = 0; int endm2 = 0; int endm3 = 0; int endm4 = 0; 
        int endarm = 0; int endarmrotate = 0;
        double endclaw = 0; //double endclawrotate = 0;
        
        double speed = 0.2;
        
        
        //Starting instruction value
        int currentInstruction = 1;
        
        //Check to see if it's logged
        boolean isLogged = false;
        
        waitForStart();

        while (opModeIsActive()) {

                // BODY MOVEMENT
            double px = gamepad1.left_stick_x; //The power of the left stick on the x axis
                                                // Left to Right, -1.00 to 1.00
            double py = -gamepad1.left_stick_y; //The power of the left stick on the y axis
                                                // Down to Up, -1.00 to 1.00
            double pa = gamepad1.right_trigger - gamepad1.left_trigger;
                                                //The power of the body rotation

            //math equation stuff
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
            m1.setPower(p1 * 0.1);
            m2.setPower(p2 * 0.1);
            m3.setPower(p3 * 0.1);
            m4.setPower(p4 * 0.1);
            
            
            //Updated Locations
            endm1 = m1.getCurrentPosition(); //Front Left
            endm2 = m2.getCurrentPosition(); //Back Left
            endm3 = m3.getCurrentPosition(); //Front Right
            endm4 = m4.getCurrentPosition(); //Back Right
            endarm = arm.getCurrentPosition(); // Arm
            endarmrotate = armRotator.getCurrentPosition(); //Arm Rotator
            endclaw = claw.getPosition();
            //endclawrotate = clawRotator.getPosition();
            
            if(gamepad1.guide && isLogged == false){
                //writeInstruction("\n" + currentInstruction + ":\n");
                //writeInstruction("Start: " + startm1 + " " + startm2 + " " + startm3 + " " + startm4
                //                + " " + startarm + " " + startarmrotate
                //                + " " + startclaw);
                                
                writeInstruction(/*"End: " + */endm1 + " " + endm2 + " " + endm3 + " " + endm4
                                + " " + endarm + " " + endarmrotate
                                + " " + (int)(endclaw * 100) / 100.0/* 
                                + " " + (int)(endclawrotate * 100) / 100.0*/ 
                                + " " + speed);
                
                startm1 = endm1; startm2 = endm2; startm3 = endm3; startm4 = endm4; 
                startarm = endarm; startarmrotate = endarmrotate;
                startclaw = endclaw; //startclawrotate = endclawrotate;
                
                currentInstruction++;
                isLogged = true;
                
            } else if (gamepad1.guide != true){ //prevents repetition
                isLogged = false;
            }
            
            telemetry.addData("Current Iteration: ", currentInstruction);
            telemetry.addData("Current Positions"," %d %d %d %d", m1.getCurrentPosition(), m2.getCurrentPosition(), 
                            m3.getCurrentPosition(), m4.getCurrentPosition(), arm.getCurrentPosition(), armRotator.getCurrentPosition());
            telemetry.addData("Last Logged"," %d %d %d %d", startm1, startm2, startm3, startm4, endarm, endarmrotate);
            
            //Display Positions of the motors
            telemetry.addData("Motor Target","%d %d %d %d", m1.getTargetPosition(), m2.getTargetPosition(), 
                    m3.getTargetPosition(), m4.getTargetPosition());
                    
            telemetry.addData("Arm Encoders","%d %d", arm.getCurrentPosition(), armRotator.getCurrentPosition()); 
            telemetry.addData("Arm Target","%d %d", arm.getTargetPosition(), armRotator.getTargetPosition());
            telemetry.addData("Controller Rotation", controllerRotation);
            
            
        

            //Manual Controls
            
            //Manual Extention 
            double armPower = -gamepad2.right_stick_y;
            double armPos = arm.getCurrentPosition();
            telemetry.addData("armPos: ", armPos);
            telemetry.addData("armPower: ", armPower);
            if ((armPos > minArmPos || armPower > 0) && (armPos < maxArmPos || armPower < 0)) {
                arm.setPower(armPower);
            } else {
                arm.setPower(0);
            }
            
            //Manual Rotation
            //double rotatorPower = -gamepad2.left_stick_y;
            int rotatorPos = armRotator.getCurrentPosition();
            telemetry.addData("rotatorPos: ", rotatorPos);
            //telemetry.addData("rotatorPower: ", rotatorPower);
            //sets the motor back to correct mode stay in position
            if (rotatorPos < minRotatorPos ) rotatorPos = minRotatorPos;
            if (rotatorPos > maxRotatorPos) rotatorPos = maxRotatorPos;
            
            // if(gamepad2.left_stick_y != 0){
            //     controllerRotation += gamepad2.left_stick_y*10;
            // }
            
            
            if(gamepad2.left_stick_y == 0){
                if (!locked) {
                  armRotator.setTargetPosition(rotatorPos);
                  armRotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                  armRotator.setPower(0.5);
                  locked = true;
                }
            } else {
                //controllerRotation = rotatorPos;
                armRotator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                armRotator.setPower(-gamepad2.left_stick_y*0.2);
                locked = false;
            }
            
            //armRotator.setPower(-gamepad2.left_stick_y);
            
            
    
            //telemetry.addData("rotate power: ", rotatorPower);
            //armRotator.setPower(rotatorPower);
            
            //Claw Controls
            if (gamepad2.left_bumper){
               claw.setPosition(0.1);
            } else if (gamepad2.right_bumper) {
                claw.setPosition(0.45);
            }
            
            // //Claw Rotator
            // if(gamepad2.right_trigger != 0){
            //     //clawRotation += + 0.05;
            //     clawRotator.setPosition(clawRotator.getPosition() + 0.05);
            // } else if (gamepad2.left_trigger != 0){
            //     //clawRotation -= + 0.05;
            //     clawRotator.setPosition(clawRotator.getPosition() - 0.05);
            // }
            
            //clawRotator.setPosition(clawRotation);
            
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
