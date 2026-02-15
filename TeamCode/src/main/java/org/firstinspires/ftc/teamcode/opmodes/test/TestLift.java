package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.subsystems.old.Lift;

@TeleOp(group="test")
public class TestLift extends CommandOpMode {

    private Lift m_lift;
    private GamepadEx m_driver;

    @Override
    public void initialize() {
        m_lift = new Lift(hardwareMap);
        m_driver = new GamepadEx(gamepad1);

        register(m_lift);

        m_driver.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new InstantCommand(() -> m_lift.arise(), m_lift));

        m_driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
            .whenPressed(new InstantCommand(() -> m_lift.reset(), m_lift));
    }
}
