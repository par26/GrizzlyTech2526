package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.config.subsystems.shooter.ManualShooter;

@TeleOp(group="test")
public class TestManualShooter extends CommandOpMode {

    private GamepadEx m_driver;
    private ManualShooter m_shooter;

    @Override
    public void initialize() {
        m_driver = new GamepadEx(gamepad1);
        m_shooter = new ManualShooter(hardwareMap, telemetry);

        register(m_shooter);
    }
}
