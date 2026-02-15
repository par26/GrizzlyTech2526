package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.subsystems.sorter.Kicker;

@TeleOp(group = "test")
public class TestKicker extends CommandOpMode {

    private Kicker m_kicker;
    private GamepadEx gp1;

    @Override
    public void initialize() {
        m_kicker = new Kicker(hardwareMap);
        gp1 = new GamepadEx(gamepad1);

        register(m_kicker);

        gp1.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new InstantCommand(() -> m_kicker.activate(),m_kicker));
        gp1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(new InstantCommand(() -> m_kicker.rest(),m_kicker));
    }
}
