package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * This is an example minimal implementation of the mecanum drivetrain
 * for demonstration purposes.  Not tested and not guaranteed to be bug free.
 *
 * @author Brandon Gong
 */
@TeleOp(name="Mecanum Plus Servo Plus Lift 112322", group="Iterative Opmode")
public class MecanumPlusServoPlusLift_112322 extends LinearOpMode {

    /*
     * The mecanum drivetrain involves four separate motors that spin in
     * different directions and different speeds to produce the desired
     * movement at the desired speed.
     */

    // declare and initialize four DcMotors.
    private DcMotor LeftFront  = null;
    private DcMotor RightFront = null;
    private DcMotor LeftBack   = null;
    private DcMotor RightBack  = null;
    private DcMotor LiftMotor = null;
    private Servo RevServo;
    
    @Override
    public void runOpMode() {
        // Name strings must match up with the config on the Robot Controller
        // app.
        LeftFront   = hardwareMap.get(DcMotor.class, "LeftFront");
        RightFront  = hardwareMap.get(DcMotor.class, "RightFront");
        LeftBack    = hardwareMap.get(DcMotor.class, "LeftBack");
        RightBack   = hardwareMap.get(DcMotor.class, "RightBack");
        RevServo    = hardwareMap.get(Servo.class, "RevServo");
        LiftMotor = hardwareMap.get(DcMotor.class, "LiftMotor");
        LiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery


        telemetry.addData("Status", "initialized");
        telemetry.update();
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        LiftMotor.setTargetPosition(0);
        LiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

                    telemetry.addData("Servo Position", RevServo.getPosition());
                    telemetry.addData("Lift Position", LiftMotor.getCurrentPosition());
                    telemetry.addData("LF Motor Power", LeftFront.getPower());
                    telemetry.addData("RF Motor Power", RightFront.getPower());
                    telemetry.addData("LR Motor Power", LeftBack.getPower());
                    telemetry.addData("RR Motor Power", RightBack.getPower());
                    telemetry.update();
                    
        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        LeftFront.setDirection(DcMotor.Direction.FORWARD);
        RightFront.setDirection(DcMotor.Direction.FORWARD);
        LeftBack.setDirection(DcMotor.Direction.REVERSE);
        RightBack.setDirection(DcMotor.Direction.REVERSE);
        LiftMotor.setDirection(DcMotor.Direction.FORWARD);
        LiftMotor.setPower(1);

            if(gamepad2.left_bumper){
                RevServo.setPosition(0.35);
                //open claw position
           } else if(gamepad2.right_bumper){
                //close claw position
                RevServo.setPosition(1);}
            if(gamepad2.x){
                //move to low junction
                LiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                LiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                LiftMotor.setTargetPosition(-1300);
            } else if(gamepad2.y){
                //move to medium junction
                LiftMotor.setTargetPosition(-2175);
            } else if(gamepad2.b){
                //move to high junction
                LiftMotor.setTargetPosition(-3050);
            } else if(gamepad2.a){
                //move to ground
                LiftMotor.setTargetPosition(0);}
              else if(gamepad2.dpad_up){
                //move lift incrementally up
                LiftMotor.setTargetPosition(LiftMotor.getCurrentPosition()-50);}
              else if(gamepad2.dpad_down){
                //move lift incrementally down
                LiftMotor.setTargetPosition(LiftMotor.getCurrentPosition()+50);}
                
        //Scalar values introduced to make twist and drive easier to operate
        double drive  = gamepad1.left_stick_y*-.5;
        double strafe = gamepad1.left_stick_x;
        double twist  = gamepad1.right_stick_x*.75;

        double[] speeds = {
            ((drive + strafe + twist)*-1),
            ((drive - strafe - twist)),
            ((drive - strafe + twist)*-1),
            ((drive + strafe - twist))};

        // Loop through all values in the speeds[] array and find the greatest
        // *magnitude*.  Not the greatest velocity.
        double max = Math.abs(speeds[0]);
        for(int i = 0; i < speeds.length; i++) {
            if ( max < Math.abs(speeds[i]) ) max = Math.abs(speeds[i]);
        }
        // If and only if the maximum is outside of the range we want it to be,
        // normalize all the other speeds based on the given speed value.
        if (max > 1) {
            for (int i = 0; i < speeds.length; i++) speeds[i] /= max;
        }
        // apply the calculated values to the motors.
        LeftFront.setPower(speeds[0]);
        RightFront.setPower(speeds[1]);
        LeftBack.setPower(speeds[2]);
        RightBack.setPower(speeds[3]);
        }
}
}
