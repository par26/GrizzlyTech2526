package org.firstinspires.ftc.teamcode.config.util;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;


public class Auto {

    private Follower m_follower;

    //Poses
    public Pose startPose, preloadPose;

    //Paths/Pathchains

    public Auto() {

    }

    private void createPoses() {
        switch (MatchValues.startLocation) {
            case FAR:

                break;
            case CLOSE:


                break;
            case TEST:


                break;
        }
    }
}
