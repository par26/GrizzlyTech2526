package org.firstinspires.ftc.teamcode.opmodes;

import com.bylazar.gamepad.Gamepad;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.commands.DriveCommand;
//import org.firstinspires.ftc.teamcode.subsystems.sorter.Sorter;
import org.firstinspires.ftc.teamcode.config.commands.IntakeStateCommand;
import org.firstinspires.ftc.teamcode.config.commands.ShootStateCommand;
import org.firstinspires.ftc.teamcode.config.commands.sorter.SorterIntakeCommand;
import org.firstinspires.ftc.teamcode.config.commands.sorter.SorterShootCommand;
import org.firstinspires.ftc.teamcode.config.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.config.subsystems.Localization;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;
import org.firstinspires.ftc.teamcode.config.util.Robot;

public class Tele extends CommandOpMode {

    //Subsystems
    private GamepadEx gp1;
    private GamepadEx gp2;

    private Robot r;
    private Localization m_localization;

    private Alliance alliance;

    public Tele(Alliance alliance) {
        this.alliance = alliance;
    }

    @Override
    public void initialize() {
        MatchValues.teleStartPose = new Pose(87.317, 9.3, Math.toRadians(0));

        gp1 = new GamepadEx(gamepad1);
        gp2 = new GamepadEx(gamepad2);

        r = new Robot(hardwareMap, alliance, telemetry);
        r.register(this);
        m_localization = new Localization(telemetry, r.f);
        r.f.setPose(MatchValues.teleStartPose);


        register(m_localization);

        schedule(
                new InstantCommand(r.turret::disableManualTarget, r.turret),
                new SequentialCommandGroup(
                new IntakeStateCommand(r.intake, r.servo),
                new SorterIntakeCommand(r.sensor, r.servo, telemetry)
        )

        );

        r.drive.setDefaultCommand(
                new DriveCommand(
                        r.drive,
                        () -> gp1.getLeftY(),
                        () -> -gp1.getLeftX(),
                        () -> -gp1.getRightX(),
                        () -> true,
                        () -> gp1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).get() ? 0.35 : 1.0
                )
        );

        gp1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(new InstantCommand(m_localization::relocalizeBlue, m_localization));

        gp1.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(new InstantCommand(m_localization::relocalizeRed, m_localization));

        gp1.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(new InstantCommand(() -> MatchValues.switchRobotState()));

        gp1.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new InstantCommand(r.intake::reverse, r.intake));

        gp1.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenReleased(new InstantCommand(r.intake::intake, r.intake));

        new Trigger(() -> MatchValues.robotState == MatchValues.RobotState.SHOOT)
                .whenActive(
                        new ShootStateCommand(r.intake, r.servo, r.s)
                );

        new Trigger(() -> MatchValues.robotState == MatchValues.RobotState.SHOOT && gp1.isDown(GamepadKeys.Button.RIGHT_BUMPER))
                .whenActive(
                        new SorterShootCommand(r.sensor, r.servo, r.kicker, r.s)
                );

        new Trigger(() -> MatchValues.robotState == MatchValues.RobotState.INTAKE)
                .whenActive(new SequentialCommandGroup(
                        new IntakeStateCommand(r.intake, r.servo),
                        new SorterIntakeCommand(r.sensor, r.servo, telemetry)
                ));


        MatchValues.robotState = MatchValues.RobotState.INTAKE;
    }
}
