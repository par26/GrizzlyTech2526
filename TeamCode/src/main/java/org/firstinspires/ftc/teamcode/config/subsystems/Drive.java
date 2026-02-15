package org.firstinspires.ftc.teamcode.config.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drive extends SubsystemBase {

    private final Follower follower;

    public Drive(HardwareMap hwMap, Follower follower) {
        this.follower = follower;
    }

    public void startTeleOp() {
        follower.startTeleopDrive();
    }

    public void drive(double forward, double strafe, double turn, boolean robotCentric) {
        double denom = Math.max(Math.abs(forward) + Math.abs(strafe) + Math.abs(turn), 1.0);
        forward /= denom;
        strafe /= denom;
        turn /= denom;

        follower.setTeleOpDrive(forward, strafe, turn, robotCentric);
    }

    public void stop() {
        follower.setTeleOpDrive(0, 0, 0, true);
    }

    @Override
    public void periodic() {
        follower.update();
    }

}
