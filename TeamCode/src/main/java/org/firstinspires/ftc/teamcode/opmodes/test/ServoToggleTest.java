package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoToggleTest", group = "Test")
public class ServoToggleTest extends LinearOpMode {

    private Servo testServo;

    // Define your two angles here
    private static final double POSITION_ONE = 0.2;
    private static final double POSITION_TWO = 0.8;

    private boolean currentState = false;
    private boolean previousRightBumper = false;

    @Override
    public void runOpMode() {

        testServo = hardwareMap.get(Servo.class, "testServo");

        // Set initial position
        testServo.setPosition(POSITION_ONE);

        waitForStart();

        while (opModeIsActive()) {

            boolean currentRightBumper = gamepad1.right_bumper;

            // Edge detection (button pressed THIS loop but not last loop)
            if (currentRightBumper && !previousRightBumper) {

                currentState = !currentState;

                if (currentState) {
                    testServo.setPosition(POSITION_TWO);
                } else {
                    testServo.setPosition(POSITION_ONE);
                }
            }

            previousRightBumper = currentRightBumper;

            telemetry.addData("Servo Position", testServo.getPosition());
            telemetry.addData("Toggle State", currentState);
            telemetry.update();
        }
    }
}
