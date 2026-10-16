package org.firstinspires.ftc.teamcode.config.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.util.FieldConstants;

public class Localization extends SubsystemBase{

    private Follower m_follower;
    private Telemetry telemetry;

    public Localization(Telemetry telemetry, Follower follower) {
        this.m_follower = follower;
        this.telemetry = telemetry;
    }

    public Pose getPose() {
        return m_follower.getPose();
    }

    public void relocalizeRed() {
        m_follower.setPose(FieldConstants.redGateRelocalizePose);
    }

    public void relocalizeBlue() {
        m_follower.setPose(FieldConstants.blueGateRelocalizePose);
    }

    private void log() {
        Pose pose = m_follower.getPose();
        telemetry.addData("Heading", Math.toDegrees(pose.getHeading()));
        telemetry.addData("X", pose.getX());
        telemetry.addData("Y", pose.getY());

        telemetry.update();
    }

    @Override
    public void periodic() {
        m_follower.update();

    }
}
