package org.firstinspires.ftc.teamcode.opmodes.test;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.config.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.config.subsystems.Drive;
import org.firstinspires.ftc.teamcode.config.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.Kicker;
import org.firstinspires.ftc.teamcode.config.subsystems.turret.Turret;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.FieldConstants;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;

@TeleOp(group = "test")
public class TestShooter extends CommandOpMode {

    private Follower m_follower;
    private Shooter m_shooter;
    private Kicker m_kicker;
    private Turret m_turret;
    private Drive m_drive;
    private GamepadEx gp;

    @Override
    public void initialize() {
        MatchValues.alliance = Alliance.RED;
        m_follower = Constants.createFollower(hardwareMap);
        m_follower.setStartingPose( new Pose(87.317, 9.3, Math.toRadians(90)));

        m_turret = new Turret(hardwareMap, m_follower, telemetry);
        m_shooter = new Shooter(hardwareMap, m_follower, telemetry);

        m_kicker = new Kicker(hardwareMap);
        m_drive = new Drive(hardwareMap, m_follower);

        gp.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                        .whenPressed(new InstantCommand(() -> m_kicker.activate(), m_kicker));

        gp.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                        .whenPressed(new InstantCommand(() -> m_kicker.rest(), m_kicker));


        register(m_shooter, m_drive, m_turret);

        m_drive.setDefaultCommand(
                new DriveCommand(
                        m_drive,
                        () -> gp.getLeftY(),
                        () -> -gp.getLeftX(),
                        () -> -gp.getRightX(),
                        () -> true,
                        () -> gp.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).get() ? 0.35 : 1.0
                )
        );

    }
}
