package org.firstinspires.ftc.teamcode.config.subsystems.shooter;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.FieldConstants;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;

public class ManualShooter extends SubsystemBase {
    private Motor m_flywheelMotor;
    private Telemetry m_telemetry;

    private Pose goalPose;

    public static double targetVelocity = 10;

    public ManualShooter(HardwareMap hwMap, Telemetry telemetry) {
        this.m_telemetry = telemetry;
        m_flywheelMotor = new Motor(hwMap, ShooterConstants.HW.MOTOR,28, 6000);
        m_flywheelMotor.setRunMode(Motor.RunMode.VelocityControl);
        m_flywheelMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        m_flywheelMotor.setVeloCoefficients(ShooterConstants.kP, 0,0);
        m_flywheelMotor.setFeedforwardCoefficients(ShooterConstants.kS, ShooterConstants.kV);

        m_flywheelMotor.resetEncoder();
        goalPose = MatchValues.alliance == Alliance.BLUE ? FieldConstants.blueGoalPose : FieldConstants.redGoalPose;
    }

    private void log() {
        m_telemetry.addData("Target Velocity:", targetVelocity);
    }

    @Override
    public void periodic() {
        m_flywheelMotor.set(targetVelocity);
        log();
    }
}
