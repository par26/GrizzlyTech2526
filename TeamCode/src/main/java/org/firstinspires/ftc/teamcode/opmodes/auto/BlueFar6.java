package org.firstinspires.ftc.teamcode.opmodes.auto;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
//import org.firstinspires.ftc.teamcode.util.MatchConstants;

/**
 * Blue Alliance Far Starting Position - 6 Specimen Auto
 * SolversLib-compliant Pedro Pathing Autonomous
 */
@Autonomous(name = "BlueFar6", preselectTeleOp = "TeleOp")
public class BlueFar6 extends Far6 {

    public BlueFar6() {
        super(Alliance.BLUE);
    }

}