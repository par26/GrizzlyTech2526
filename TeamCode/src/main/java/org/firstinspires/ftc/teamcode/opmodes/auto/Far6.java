package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.util.Alliance;

public class Far6 extends CommandOpMode {

    private PathChain path1;
    private PathChain path2;
    private PathChain path3;

    Follower follower;
    Telemetry telemetry;

    public Far6(Alliance alliance) {


    }

    @Override
    public void initialize() {
        // Set match constants
        //MatchConstants.isBlueAlliance = true;
        //MatchConstants.startLocation = MatchConstants.RobotStart.FAR;


        // Initialize follower with starting pose
        Pose startPose = new Pose(88.017, 8.086, Math.toRadians(90));

        follower.setStartingPose(startPose);

        // Build paths
        buildPaths();

        // Schedule the autonomous command sequence
        schedule(
                new SequentialCommandGroup(
                        new FollowPathCommand(follower, path1),
                        new FollowPathCommand(follower, path2),
                        new FollowPathCommand(follower, path3)
                )
        );

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Alliance", "Blue");
        telemetry.addData("Start Location", "Far");
        telemetry.addData("Starting Pose", startPose);
        telemetry.update();
    }


    /**
     * Build all paths for the autonomous routine
     */
    private void buildPaths() {
        // Path 1: Start to first waypoint
        path1 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(88.017, 8.086),
                        new Pose(102.013, 35.145)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(0))
                .build();

        // Path 2: First waypoint to second waypoint
        path2 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(102.013, 35.145),
                        new Pose(129.382, 35.456)
                ))
                .setTangentHeadingInterpolation()
                .build();

        // Path 3: Second waypoint back to start area
        path3 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(129.382, 35.456),
                        new Pose(87.896, 8.218)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(90))
                .build();
    }
}
