package org.firstinspires.ftc.teamcode.config.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.Robot;

public class Far6Paths {

    private Follower f;

    public Pose startPose = new Pose(87.317, 9.3, Math.toRadians(0));
    Pose matchAngle = new Pose(129.382, 35.35, Math.toRadians(0));
    Pose intake = new Pose(135, 35.35, Math.toRadians(0));

    public Far6Paths(Robot r) {
        this.f = r.f;

        if (r.a.equals(Alliance.BLUE)) {
            startPose = startPose.mirror();
            matchAngle = matchAngle.mirror();
            intake = intake.mirror();
        }
    }

    public PathChain matchAngle() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                startPose,
                                matchAngle
                        )
                )
                .setLinearHeadingInterpolation(startPose.getHeading(), matchAngle.getHeading())
                .build();
    }

    public PathChain intake() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                matchAngle,
                                intake
                        )
                )
                .setTangentHeadingInterpolation()
                .build();
    }

    public PathChain shoot() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                intake,
                                startPose
                        )
                )
                .setLinearHeadingInterpolation(intake.getHeading(), startPose.getHeading())
                .build();
    }

}
