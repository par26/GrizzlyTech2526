package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.config.subsystems.TelemetrySubsystem;

@TeleOp
public class TelemetryTestTele extends CommandOpMode {
    private TelemetrySubsystem telemSubsys;

    @Override
    public void initialize() {
        telemSubsys = new TelemetrySubsystem(telemetry);

        register(telemSubsys);
    }
}
