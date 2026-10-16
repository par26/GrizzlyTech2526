package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterServo;

@TeleOp(group="test")
public class TestManualSorter extends CommandOpMode {

    private GamepadEx m_driver;
    private SorterServo m_servo;

    @Override
    public void initialize() {
        m_driver = new GamepadEx(gamepad1);
        m_servo = new SorterServo(hardwareMap, telemetry);

        register(m_servo);

        m_driver.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new InstantCommand(() -> m_servo.setPosition(1)));

        m_driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(new InstantCommand(() -> m_servo.setPosition(0)));

        m_driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(new InstantCommand(() -> m_servo.setPosition(2)));

        m_driver.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(new InstantCommand(() -> m_servo.applyOffset()));

        m_driver.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(new InstantCommand(() -> m_servo.removeOffset()));


    }
}
