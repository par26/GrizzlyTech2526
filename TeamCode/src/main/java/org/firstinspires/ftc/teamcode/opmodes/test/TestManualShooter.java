package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.subsystems.shooter.ManualShooter;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.Kicker;

@TeleOp(group="test")
public class TestManualShooter extends CommandOpMode {

    private GamepadEx gp;
    private ManualShooter m_shooter;
    private Kicker m_kicker;

    @Override
    public void initialize() {
        gp = new GamepadEx(gamepad1);
        m_shooter = new ManualShooter(hardwareMap, telemetry);

        gp.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new InstantCommand(() -> m_kicker.activate(), m_kicker));

        gp.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(new InstantCommand(() -> m_kicker.rest(), m_kicker));

        register(m_shooter);
    }
}
