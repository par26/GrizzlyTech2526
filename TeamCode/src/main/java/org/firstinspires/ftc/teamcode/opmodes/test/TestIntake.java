package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.subsystems.intake.Intake;

@TeleOp(group = "test")
public class TestIntake extends CommandOpMode {

    private Intake m_intake;
    private GamepadEx gp1;

    @Override
    public void initialize() {
        m_intake = new Intake(hardwareMap);
        gp1 = new GamepadEx(gamepad1);

        // default safe state

        // Tap controls (edge-triggered)
        gp1.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(new InstantCommand(m_intake::intake));

        gp1.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(new InstantCommand(m_intake::reverse));

        gp1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(new InstantCommand(m_intake::lowerIntake));

        gp1.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(new InstantCommand(m_intake::raiseIntake));
    }

}

