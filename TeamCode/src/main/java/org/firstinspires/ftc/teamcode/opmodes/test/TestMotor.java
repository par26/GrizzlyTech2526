package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.config.subsystems.intake.Intake;

@TeleOp(group="test")
public class TestMotor extends CommandOpMode {

    private Intake intake;

    @Override
    public void initialize() {
        intake = new Intake(hardwareMap);
        register(intake);
    }
}
