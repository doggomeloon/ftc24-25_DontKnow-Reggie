package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name="ServoTest")
public class ServoTest extends LinearOpMode {
    
    Servo lowX, lowY;
    
    @Override
    public void runOpMode(){
        
        
        lowX = hardwareMap.get(Servo.class, "lowX"); 
        lowY = hardwareMap.get(Servo.class, "lowY"); 
        
        telemetry.addData("Press Start When Ready","");
        telemetry.update();
        waitForStart();
        
        double lowYPos = 0.0;
        double lowXPos = 0.0;
        
        double speed = 0.001;

        while (opModeIsActive()) {

            
            // //lowY Controls
            // if (gamepad1.right_stick_y > 0 && lowYPos != 1){
            //     lowYPos += speed;
            //     lowY.setPosition(lowY.getPosition() + speed);
            //     telemetry.addData("Y+",true);
            // } else if (gamepad1.right_stick_y < 0 && lowYPos != 0) {
            //     lowYPos -= speed;
            //     lowY.setPosition(lowY.getPosition() - speed);
            //     telemetry.addData("Y-",true);
            // }
            
            // //lowX Controls
            // if (gamepad1.right_stick_x > 0 && lowXPos != 1){
            //     lowXPos += speed;
            //     lowX.setPosition(lowX.getPosition() + speed);
            //     telemetry.addData("X+",true);
            // } else if (gamepad1.right_stick_x < 0 && lowXPos != 0) {
            //     lowXPos -= speed;
            //     lowX.setPosition(lowX.getPosition() - speed);
            //     telemetry.addData("X-",true);
            // }
            
            
            if(gamepad1.a){
                lowY.setPosition(0.0);
            }
            if(gamepad1.y){
                lowY.setPosition(1.0);
            }
            
            if(gamepad1.x){
                lowX.setPosition(0.0);
            }
            if(gamepad1.b){
                lowX.setPosition(1.0);
            }
            
            
            
            
            telemetry.addData("XPos", lowX.getPosition());
            telemetry.addData("YPos", lowY.getPosition());
            
            
            telemetry.update();
            
            // lowX.setPosition(lowXPos);
            // lowY.setPosition(lowYPos);
            
        }
    }
}
