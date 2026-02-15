package org.firstinspires.ftc.teamcode.opmodes.test;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.config.subsystems.Localization;
import org.firstinspires.ftc.teamcode.config.subsystems.old.ManualTurretSubsystem;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.FieldConstants;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;

@TeleOp(group="test")
public class TestManualTurret extends CommandOpMode {

    private ManualTurretSubsystem m_turret;
    private GamepadEx m_driver1;
    private Follower m_follower;
    private Localization m_local;

    @Override
    public void initialize() {

        MatchValues.goalPose = FieldConstants.redGoalPose;
        MatchValues.autoStartPose = FieldConstants.redSpawnTest;
        MatchValues.alliance = Alliance.RED;

        m_follower = Constants.createFollower(hardwareMap);
        m_follower.setStartingPose(FieldConstants.redSpawnTest);

        m_driver1 = new GamepadEx(gamepad1);
        m_turret = new ManualTurretSubsystem(hardwareMap, telemetry, m_follower);
        m_local = new Localization(telemetry, m_follower);

        register(m_local, m_turret);

        m_driver1.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new InstantCommand(() -> m_turret.rotateTo180()));

        m_driver1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(new InstantCommand(() -> m_turret.rotateTo0()));

        m_driver1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new InstantCommand(() -> m_turret.increment30()));

        m_driver1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new InstantCommand(() -> m_turret.decrement30()));



    }
}
