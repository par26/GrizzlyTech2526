package org.firstinspires.ftc.teamcode.config.commands;

import org.firstinspires.ftc.teamcode.config.subsystems.Drive;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class DriveCommand extends CommandBase {

    private final Drive drive;
    private final DoubleSupplier forward, strafe, turn;
    private final BooleanSupplier robotCentric;
    private final DoubleSupplier multiplier;

    public DriveCommand(Drive drive, DoubleSupplier forward,
                        DoubleSupplier strafe, DoubleSupplier turn,
                        BooleanSupplier robotCentric, DoubleSupplier multiplier) {
        this.drive = drive;
        this.forward = forward;
        this.strafe = strafe;
        this.turn = turn;
        this.robotCentric = robotCentric;
        this.multiplier = multiplier;

        addRequirements(drive);
    }

    @Override
    public void initialize() {
        drive.startTeleOp();
    }

    @Override
    public void execute() {
        drive.drive(
                forward.getAsDouble() * multiplier.getAsDouble(),
                strafe.getAsDouble() * multiplier.getAsDouble(),
                turn.getAsDouble() * multiplier.getAsDouble(),
                robotCentric.getAsBoolean()
        );
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
