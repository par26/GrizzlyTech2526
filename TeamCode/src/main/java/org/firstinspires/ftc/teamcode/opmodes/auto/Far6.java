package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.config.paths.Far6Paths;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.Robot;

public class Far6 extends CommandOpMode {

    private Robot r;
    final Alliance a;

    public Far6(Alliance alliance) {
        a = alliance;

    }

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, a, telemetry);
        r.register(this);
        Far6Paths p = new Far6Paths(r);
        r.f.setStartingPose(p.startPose);

        schedule(
                new SequentialCommandGroup(
                        new FollowPathCommand(r.f, p.matchAngle()),
                        new FollowPathCommand(r.f, p.intake()),
                        new FollowPathCommand(r.f, p.shoot())
                )
        );

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Alliance", "Blue");
        telemetry.addData("Start Location", "Far");
        telemetry.addData("Starting Pose", p.startPose);
        telemetry.update();
    }
}
