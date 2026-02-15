package org.firstinspires.ftc.teamcode.opmodes.test;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.config.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.Kicker;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;

public class TestShooter extends CommandOpMode {

    private Follower m_follower;
    private Shooter m_shooter;
    private Kicker m_kicker;
    private GamepadEx gp;

    @Override
    public void initialize() {
        MatchValues.alliance = Alliance.RED;
        m_follower = Constants.createFollower(hardwareMap);

        m_shooter = new Shooter(hardwareMap, m_follower);
        m_kicker = new Kicker(hardwareMap);

        gp.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                        .whenPressed(new InstantCommand(() -> m_kicker.activate(), m_kicker));

        gp.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                        .whenPressed(new InstantCommand(() -> m_kicker.rest(), m_kicker));


        register(m_shooter);

    }
}
