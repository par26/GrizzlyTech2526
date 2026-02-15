package org.firstinspires.ftc.teamcode.opmodes.test;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.config.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.config.subsystems.Drive;
import org.firstinspires.ftc.teamcode.config.subsystems.Localization;

@TeleOp(group="test")
public class TestDriveTrain extends CommandOpMode {

    private GamepadEx m_driver;
    private Drive m_drive;
    private Localization m_local;
    private Follower m_follower;

    @Override
    public void initialize() {
        m_follower = Constants.createFollower(hardwareMap);

        m_driver = new GamepadEx(gamepad1);
        m_drive = new Drive(hardwareMap, m_follower);
        m_local = new Localization(telemetry, m_follower);

        register(m_drive, m_local);

        //goofy ahh supplier system
        m_drive.setDefaultCommand(
                new DriveCommand(
                        m_drive,
                        () -> m_driver.getLeftY(),
                        () -> -m_driver.getLeftX(),
                        () -> -m_driver.getRightX(),
                        () -> true,
                        () -> m_driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).get() ? 0.35 : 1.0
                )
        );

    }
}
