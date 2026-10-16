package org.firstinspires.ftc.teamcode.opmodes.test;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.config.subsystems.TelemetrySubsystem;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;

@Autonomous(name = "TelemetrySubsys Test - Auto", group = "test", preselectTeleOp = "TelemetryTestTele")
public class TelemetryTestAuto extends CommandOpMode {

    private TelemetrySubsystem telemSubsys;

    @Override
    public void initialize() {
        MatchValues.teleStartPose = new Pose(87.317, 9.3, Math.toRadians(0));


    }
}
