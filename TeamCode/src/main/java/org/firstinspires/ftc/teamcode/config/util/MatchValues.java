package org.firstinspires.ftc.teamcode.config.util;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

public class MatchValues {
    public enum RobotStart {
        FAR,
        CLOSE,
        TEST
    }

    public enum RobotState {
        INTAKE,
        SHOOT
    }

    public static void switchRobotState() {
        robotState = robotState == RobotState.INTAKE ? RobotState.SHOOT : RobotState.INTAKE;
    }

    public static MatchValues.RobotStart startLocation;
    public static MatchValues.RobotState robotState;
    public static Alliance alliance;
    public static Follower matchFollower;
    public static Pose goalPose;
    public static Pose autoStartPose;
    public static Pose teleStartPose;

    public static SorterNode.NodeOption[] matchMotif;
}
