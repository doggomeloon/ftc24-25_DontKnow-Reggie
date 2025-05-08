package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;

// static final double ARM_MAX = 1000;
// static final double ROTATE_MIN = 120;
// static final double ROTATE_MAX = 1050;
// static final double ARM_SPEED =: 0.5;
// static final double ROTATE_SPEED = 0.5;

@TeleOp(name="DontKnowOrigin")
public class DontKnowOrigin extends LinearOpMode {
    
    DcMotorEx m1, m2, m3, m4, arm, armRotator;
    Servo claw;
    

    //Enums and Classes for State machine
    enum State {
        DR, DE, UR, UE, BUSY
    }
    
    State state;
    State targetState;

    @Override
    public void runOpMode(){
        
        
        // Wheels, ordered accordingly:
        // Top Left, Back Left, Front Right, Back Right
        m1 = hardwareMap.get(DcMotorEx.class, "bl"); //back left
        m2 = hardwareMap.get(DcMotorEx.class, "br"); //back right
        m3 = hardwareMap.get(DcMotorEx.class, "fr"); //front right
        m4 = hardwareMap.get(DcMotorEx.class, "fl"); //front left
    
        claw = hardwareMap.get(Servo.class, "claw"); //claw servo
        
        //Declare Arm
        arm = hardwareMap.get(DcMotorEx.class, "arm"); //arm
        armRotator = hardwareMap.get(DcMotorEx.class, "armRotator"); //arm
    
        //Motor direction, flip to reverse direction of robot (may need to reorder motors)
        //m1.setDirection(DcMotor.Direction.REVERSE);
        m2.setDirection(DcMotor.Direction.REVERSE);
        m3.setDirection(DcMotor.Direction.REVERSE);
        m4.setDirection(DcMotor.Direction.REVERSE);
        
        arm.setDirection(DcMotor.Direction.REVERSE);
        //armRotator.setDirection(DcMotor.Direction.REVERSE
        
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
        armRotator.setTargetPosition(0);
        arm.setTargetPosition(0);
        state = State.DR;
        targetState = State.DR;
        
        claw.setPosition(0.1);
        telemetry.addData("Press Start When Ready","");
        telemetry.update();
        
        //Max/Min Arm Positions
        int maxArmPos = 6450;
        int minArmPos = 150;
        int maxRotatorPos = 850;
        int minRotatorPos = 20;
        
        int controllerRotation = 0;
        
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
            m1.setPower(p1 * 0.7);
            m2.setPower(p2 * 0.7);
            m3.setPower(p3 * 0.7);
            m4.setPower(p4 * 0.7);
           
           //State Machine
            if (gamepad2.a) {
                if (state == State.UR) {
                    targetState = State.DR;
                    armRotator.setTargetPosition(120);
                    armRotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    armRotator.setPower(0.5);
                    state = State.BUSY;
                } else if (state == State.DE) {
                    // targetState = State.DR;
                    // arm.setTargetPosition(0);
                    // arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    // arm.setPower(0.5);
                    // state = State.BUSY;  
                }
            } else if (gamepad2.b) {
                if (state == State.DR) {
                    // targetState = State.DE;
                    // arm.setTargetPosition(100);
                    // armRotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    // armRotator.setPower(0.5);
                    // state = State.BUSY;   
                }
                //targetState = DE; // down, extendend
            } else if (gamepad2.x) {// up, retracted
                if (state == State.DR) {
                    targetState = State.UR;
                    armRotator.setTargetPosition(1050);
                    armRotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    armRotator.setPower(0.5);
                    state = State.BUSY;
                    // rotate down
                } 
                else if(state == State.UE){
                    // targetState = State.UR;
                    // arm.setTargetPosition(0);
                    // arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    // arm.setPower(0.5);
                    // state = State.BUSY;
                }
            } else if (gamepad2.y) {
                if (state == State.UR) {
                    // targetState = State.UE;
                    // arm.setTargetPosition(100);
                    // arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    // arm.setPower(0.5);
                    // state = State.BUSY;
                }
                //targetState = 3; // up, extended
                //if(state == State.DR){
                    
                //}
                
            }
            
            if (state == State.BUSY) {
                if (!(armRotator.isBusy() || arm.isBusy())) {
                    state = targetState;
                }
            }
            

            //Display Positions of the motors
            telemetry.addData("Motor Encoders"," %d %d %d %d", m1.getCurrentPosition(), m2.getCurrentPosition(),
                    m3.getCurrentPosition(), m4.getCurrentPosition());
            telemetry.addData("Motor Target","%d %d %d %d", m1.getTargetPosition(), m2.getTargetPosition(), 
                    m3.getTargetPosition(), m4.getTargetPosition());
                    
            telemetry.addData("Arm Encoders","%d %d", arm.getCurrentPosition(), armRotator.getCurrentPosition()); 
            telemetry.addData("Arm Target","%d %d", arm.getTargetPosition(), armRotator.getTargetPosition());
            telemetry.addData("Controller Rotation", controllerRotation);
            
            
            telemetry.addData("State", "%s", state);
            telemetry.addData("TargetState", targetState);
            
            
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
            
            
            
            if(gamepad2.left_stick_y != 0){
                controllerRotation += -gamepad2.left_stick_y*12;
            } /*else if(gamepad2.left_stick_y < 0){
                armRotator.setPower(0);
            }*/
            
            
            //Manual Rotation
            double rotatorPower = -gamepad2.left_stick_y;
            int rotatorPos = armRotator.getCurrentPosition();
            telemetry.addData("rotatorPos: ", rotatorPos);
            telemetry.addData("rotatorPower: ", rotatorPower);
            // if ((rotatorPos > minRotatorPos || rotatorPower > 0)
            //     && (rotatorPos < maxRotatorPos || rotatorPower < 0)
            //     && (armPos < 600 || rotatorPower < 0)) {
            //     armRotator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //     armRotator.setPower(rotatorPower);
            // } else {
                //sets the motor back to correct mode stay in position
                if (rotatorPos < minRotatorPos ) rotatorPos = minRotatorPos;
                if (rotatorPos > maxRotatorPos) rotatorPos = maxRotatorPos;
                armRotator.setTargetPosition(controllerRotation);
                armRotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armRotator.setPower(0.5);
            // }
            
            
    
            //telemetry.addData("rotate power: ", rotatorPower);
            //armRotator.setPower(rotatorPower);
            
            //Claw Controls
            if (gamepad2.left_bumper){
               claw.setPosition(0.2);
            } else if (gamepad2.right_bumper) {
                claw.setPosition(0.33);
            }
            
            
            telemetry.update();
        }
        //Stops all motors
        m1.setPower(0);
        m2.setPower(0);
        m3.setPower(0);
        m4.setPower(0);
        
        arm.setPower(0);
        armRotator.setPower(0);
    }
}
