package org.firstinspires.ftc.teamcode.config.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.FieldConstants;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;

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

    public void relocalize() {
        if (MatchValues.alliance == Alliance.BLUE) {
            m_follower.setPose(FieldConstants.blueAllianceRelocalizePose);
        } else {
            m_follower.setPose(FieldConstants.redAllianceRelocalizePose);
        }
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
