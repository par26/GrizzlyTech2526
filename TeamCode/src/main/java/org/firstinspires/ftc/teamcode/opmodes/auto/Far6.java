package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.config.commands.IntakeStateCommand;
import org.firstinspires.ftc.teamcode.config.commands.ShootStateCommand;
import org.firstinspires.ftc.teamcode.config.commands.sorter.SorterIntakeCommand;
import org.firstinspires.ftc.teamcode.config.commands.sorter.SorterShootCommand;
import org.firstinspires.ftc.teamcode.config.paths.Far6Paths;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;
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

        double targetAngle = MatchValues.alliance == Alliance.RED ? 70 : 110;

        schedule(
                new SequentialCommandGroup(
                        new InstantCommand(r.turret::enableManualTarget, r.turret),
                        new InstantCommand(() -> r.turret.setTargetAngle(targetAngle)),

                        new ShootStateCommand(r.intake, r.servo, r.s),
                        new SorterShootCommand(r.sensor, r.servo, r.kicker, r.s),

                        new FollowPathCommand(r.f, p.startToShoot()),

                        new ParallelCommandGroup(
                                new FollowPathCommand(r.f, p.corner1()),
                                new SequentialCommandGroup(
                                        new IntakeStateCommand(r.intake, r.servo),
                                        new SorterIntakeCommand(r.sensor, r.servo, telemetry)
                                )
                        ),
                        new FollowPathCommand(r.f, p.shoot1()),
                        new ShootStateCommand(r.intake, r.servo, r.s),
                        new SorterShootCommand(r.sensor, r.servo, r.kicker, r.s),

                        new ParallelCommandGroup(
                                new FollowPathCommand(r.f, p.scoop()),
                                new SequentialCommandGroup(
                                        new IntakeStateCommand(r.intake, r.servo),
                                        new SorterIntakeCommand(r.sensor, r.servo, telemetry)
                                )
                        ),
                        new FollowPathCommand(r.f, p.shoot2()),
                        new ShootStateCommand(r.intake, r.servo, r.s),
                        new SorterShootCommand(r.sensor, r.servo, r.kicker, r.s),


                        new InstantCommand(() -> r.turret.setTargetAngle(0))


                )
        );

        MatchValues.teleStartPose = p.shoot;

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Alliance", "Blue");
        telemetry.addData("Start Location", "Far");
        telemetry.addData("Starting Pose", p.startPose);
        telemetry.addData("Ending Pose", p.shoot);
        telemetry.update();
    }
}
