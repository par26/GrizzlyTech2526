package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name="Encoder Test (Simple)", group="Test")
public class TestEncoder extends OpMode {

    private DcMotorEx motor;

    private int startTicks = 0;
    private int lastTicks = 0;

    @Override
    public void init() {
        motor = hardwareMap.get(DcMotorEx.class, "shooterMotor"); // <-- change name in RC config

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Reset encoder once at init
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        startTicks = motor.getCurrentPosition();
        lastTicks  = startTicks;

        telemetry.addLine("Ready. Hold RIGHT_BUMPER to run motor. Press A to re-zero.");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Hold right bumper to run
        boolean run = gamepad1.right_bumper;
        motor.setPower(run ? 0.30 : 0.0);

        // Press A to re-zero encoder (simple edge detect)
        if (gamepad1.a && !aWasDown) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            startTicks = motor.getCurrentPosition();
            lastTicks  = startTicks;
        }
        aWasDown = gamepad1.a;

        int ticks = motor.getCurrentPosition();
        int deltaFromStart = ticks - startTicks;
        int deltaSinceLast = ticks - lastTicks;
        lastTicks = ticks;

        double velTicksPerSec = motor.getVelocity(); // ticks/sec

        telemetry.addData("Motor", "testMotor");
        telemetry.addData("Ticks", ticks);
        telemetry.addData("Δ from start", deltaFromStart);
        telemetry.addData("Δ per loop", deltaSinceLast);
        telemetry.addData("Velocity (ticks/s)", velTicksPerSec);
        telemetry.addData("Running", run);
        telemetry.addLine("Hold RIGHT_BUMPER to spin. Tap A to reset encoder.");
        telemetry.update();
    }

    private boolean aWasDown = false;

    @Override
    public void stop() {
        motor.setPower(0);
    }
}