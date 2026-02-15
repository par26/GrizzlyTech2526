package org.firstinspires.ftc.teamcode.config.commands;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.config.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterServo;

//raise intake & apply offset from shooting
public class ShootStateCommand extends SequentialCommandGroup {

    private final Intake m_intake;
    private final SorterServo m_servo;

    public ShootStateCommand(Intake intake, SorterServo servo) {
        this.m_intake = intake;
        this.m_servo = servo;

        addCommands(
                new InstantCommand(m_servo::applyOffset, m_servo),
                new InstantCommand(m_intake::reverse, m_intake),
                new WaitCommand(300),
                new InstantCommand(m_intake::raiseIntake, m_intake)
        );
    }

}
