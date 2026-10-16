package org.firstinspires.ftc.teamcode.config.subsystems;

import org.firstinspires.ftc.teamcode.config.util.MatchValues;

public class TelemetrySubsystem extends SubsystemBase {

    private final org.firstinspires.ftc.robotcore.external.Telemetry telemetry;

    public TelemetrySubsystem(org.firstinspires.ftc.robotcore.external.Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public void periodic() {
        telemetry.addData("RobotState", MatchValues.robotState);
//        telemetry.addData("AutoStage", MatchValues.autoStage); // if you add this
        telemetry.update();
    }
}