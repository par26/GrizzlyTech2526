package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.config.commands.IntakeStateCommand;
import org.firstinspires.ftc.teamcode.config.commands.ShootStateCommand;
import org.firstinspires.ftc.teamcode.config.commands.sorter.SorterIntakeCommand;
import org.firstinspires.ftc.teamcode.config.commands.sorter.SorterShootCommand;
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
                        new ShootStateCommand(r.intake, r.servo),
                        new SorterShootCommand(r.sensor, r.servo, r.kicker),

                        new ParallelCommandGroup(
                                new FollowPathCommand(r.f, p.corner1()),
                                new SequentialCommandGroup(
                                        new IntakeStateCommand(r.intake, r.servo),
                                        new SorterIntakeCommand(r.sensor, r.servo)
                                )
                        ),
                        new FollowPathCommand(r.f, p.shoot1()),
                        new ShootStateCommand(r.intake, r.servo),
                        new SorterShootCommand(r.sensor, r.servo, r.kicker),

                        new ParallelCommandGroup(
                                new FollowPathCommand(r.f, p.scoop()),
                                new SequentialCommandGroup(
                                        new IntakeStateCommand(r.intake, r.servo),
                                        new SorterIntakeCommand(r.sensor, r.servo)
                                )
                        ),
                        new FollowPathCommand(r.f, p.shoot2()),
                        new ShootStateCommand(r.intake, r.servo),
                        new SorterShootCommand(r.sensor, r.servo, r.kicker)

                        //repeat follow path command scoop & shoot2 as many times as possible
                )
        );

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Alliance", "Blue");
        telemetry.addData("Start Location", "Far");
        telemetry.addData("Starting Pose", p.startPose);
        telemetry.update();
    }
}
