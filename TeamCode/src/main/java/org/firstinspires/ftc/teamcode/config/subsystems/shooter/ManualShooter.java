package org.firstinspires.ftc.teamcode.config.subsystems.shooter;

import static org.firstinspires.ftc.teamcode.config.subsystems.shooter.ShooterConstants.kP;
import static org.firstinspires.ftc.teamcode.config.subsystems.shooter.ShooterConstants.kS;
import static org.firstinspires.ftc.teamcode.config.subsystems.shooter.ShooterConstants.kV;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.FieldConstants;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;

public class ManualShooter extends SubsystemBase {
    private DcMotorEx m_flywheelMotor;
    private Telemetry m_telemetry;

    private Pose goalPose;

    public static double targetVelocity = 1360;

    public ManualShooter(HardwareMap hwMap, Telemetry telemetry) {
        this.m_telemetry = telemetry;
        m_flywheelMotor = hwMap.get(DcMotorEx.class, ShooterConstants.HW.MOTOR);

        goalPose = MatchValues.alliance == Alliance.BLUE ? FieldConstants.blueGoalPose : FieldConstants.redGoalPose;
    }

    public void setTargetVelocity(double velocity) {
        targetVelocity = velocity;
    }

    private void setPower(double power) {
        m_flywheelMotor.setPower(power);
    }

    private double getVelocity() {
        return m_flywheelMotor.getVelocity();
    }

    private void log() {
        m_telemetry.addData("Target Velocity:", targetVelocity);
        m_telemetry.addData("Corrected Velocity:", m_flywheelMotor.getVelocity());
        m_telemetry.update();
    }

    @Override
    public void periodic() {
        setPower((kV * targetVelocity) + (kP * (targetVelocity - getVelocity())) + kS);

        log();
    }
}
