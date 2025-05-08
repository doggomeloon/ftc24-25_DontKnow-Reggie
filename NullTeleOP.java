package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@TeleOp(name="Null")
public class NullTeleOP extends LinearOpMode {

    @Override
    public void runOpMode(){
        
        telemetry.addData("Press Start When Ready","");
        telemetry.update();
        
        waitForStart();
        
        telemetry.addData("hi robert","");
        telemetry.update();
    }
}
