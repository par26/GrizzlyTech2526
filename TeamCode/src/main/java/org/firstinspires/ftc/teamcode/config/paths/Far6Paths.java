package org.firstinspires.ftc.teamcode.config.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.Robot;

public class Far6Paths {

    private Follower f;

    public Pose startPose = new Pose(87.317, 9.3, Math.toRadians(0));
    Pose corner1 = new Pose(135, 9.3, 0);
    Pose shoot = new Pose(86.38, 14.23, 0);
    Pose corner2 = new Pose(132.79, 11.019); //tangential, no heading
    Pose corner2Control = new Pose(119.63, 5.698);


    public Far6Paths(Robot r) {
        this.f = r.f;

        if (r.a.equals(Alliance.BLUE)) {
            startPose = startPose.mirror();
            corner1 = corner1.mirror();
            shoot = shoot.mirror();
            corner2 = corner2.mirror();
            corner2Control = corner2Control.mirror();
        }
    }

    public PathChain corner1() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                startPose,
                                corner1
                        )
                )
                .setLinearHeadingInterpolation(startPose.getHeading(), corner1.getHeading())
                .build();
    }

    public PathChain shoot1() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                corner1,
                                shoot
                        )
                )
                .setLinearHeadingInterpolation(corner1.getHeading(), shoot.getHeading())
                .build();
    }

    public PathChain scoop() {
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shoot,
                                corner2,
                                corner2Control
                        )
                )
                .setTangentHeadingInterpolation()
                .build();
    }

    public PathChain shoot2() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                corner2,
                                shoot
                        )
                )
                .setConstantHeadingInterpolation(0)
                .build();
    }



}
