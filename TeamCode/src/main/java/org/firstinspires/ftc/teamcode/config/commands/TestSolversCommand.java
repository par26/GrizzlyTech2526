package org.firstinspires.ftc.teamcode.config.commands;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.config.subsystems.Solvers;

public class TestSolversCommand extends CommandBase{

    private Solvers solvers;
    private ElapsedTime timer;

    public TestSolversCommand(Solvers solvers) {
        this.solvers = solvers;
        timer = new ElapsedTime();
        addRequirements(solvers);
    }

    @Override
    public void initialize() {


        solvers.activateTrigger1();
        timer.reset();
    }

    @Override
    public void execute() {
        if (timer.seconds() >= 2) {
            timer.reset();
            if (solvers.trigger2) {
                solvers.deactiveTrigger2();
            } else {
                solvers.activateTrigger2();
            }
        }
    }

    @Override
    public boolean isFinished() {
        return solvers.trigger3;
    }

    @Override
    public void end(boolean interrupted) {

    }
}
