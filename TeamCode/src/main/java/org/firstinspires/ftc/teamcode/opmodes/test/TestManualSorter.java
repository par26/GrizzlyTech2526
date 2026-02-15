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


        m_driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new InstantCommand(() -> m_servo.rotateC(), m_servo));

        m_driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new InstantCommand(() -> m_servo.rotateCC(), m_servo));

        m_driver.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(new InstantCommand(() -> m_servo.setAngle(0), m_servo));


    }
}
