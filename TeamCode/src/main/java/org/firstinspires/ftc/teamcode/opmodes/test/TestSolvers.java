package org.firstinspires.ftc.teamcode.opmodes.test;

import com.bylazar.gamepad.Gamepad;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.commands.TestSolversCommand;
import org.firstinspires.ftc.teamcode.config.subsystems.Solvers;


@TeleOp(group="test")
public class TestSolvers extends CommandOpMode {

    private Solvers solvers;
    private GamepadEx gp1;

    //Note: if a command was activate inside of a sequential command group, it can't get canceled by itself, gotta cancel the group

    @Override
    public void initialize() {
        gp1 = new GamepadEx(gamepad1);
        solvers = new Solvers(telemetry);

        TestSolversCommand solversCom = new TestSolversCommand(solvers);
        SequentialCommandGroup group = new SequentialCommandGroup(
                new InstantCommand(solvers::activateTrigger4, solvers),
                solversCom
        );

        register(solvers);

        new Trigger(() -> gp1.isDown(GamepadKeys.Button.A))
                .whenActive(
                        group
                );

        new Trigger(() -> gp1.isDown(GamepadKeys.Button.B))
                .cancelWhenActive(group);
    }
}
