package org.firstinspires.ftc.teamcode.opmodes.old;

import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.config.subsystems.old.MoveSubsystem;

//temporary autonomous to move forward & gain points
public class Auto extends CommandOpMode {

    private MoveSubsystem m_move;

    @Override
    public void initialize() {
        m_move = new MoveSubsystem(hardwareMap);

        register(m_move);
        SequentialCommandGroup command = new SequentialCommandGroup(
                new InstantCommand(() -> m_move.changePower(true), m_move),
                new WaitCommand(800),
                new InstantCommand(() -> m_move.changePower(false), m_move)
        );

        schedule(command);
    }


}
