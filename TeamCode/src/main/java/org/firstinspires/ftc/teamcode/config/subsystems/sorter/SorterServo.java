package org.firstinspires.ftc.teamcode.config.subsystems.sorter;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.seattlesolvers.solverslib.hardware.AbsoluteAnalogEncoder;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;

@Configurable
public class SorterServo extends SubsystemBase {

    private CRServoEx m_servo;
    private AbsoluteAnalogEncoder m_encoder;

    private double error;
    private double targetPos;

    private Telemetry telemetry;
    public static double kP = 0.4;
    public static double kI = 0.0;
    public static double kD = 0.0;

    public SorterServo(HardwareMap hwMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        m_encoder = new AbsoluteAnalogEncoder(hwMap, SorterConstants.HW.ENCODER);
        m_servo = new CRServoEx(hwMap, SorterConstants.HW.SERVO, m_encoder, CRServoEx.RunMode.OptimizedPositionalControl);

        m_servo.setPIDF(new PIDFCoefficients(kP, kI, kD, 0));
        m_servo.set(Math.toRadians(0));
    }

    private void changeAngle(double angle) {
        targetPos += angle;
    }

    public void rotateC() {
        changeAngle(SorterConstants.Index.NODE_ANGLE_DEG);
    }
    public void rotateCC() {
        changeAngle(-SorterConstants.Index.NODE_ANGLE_DEG);
    }
    public void applyOffset() {
        changeAngle(SorterConstants.Index.OFFSET_ANGLE_DEG);
    }
    public void removeOffset() {
        changeAngle(-SorterConstants.Index.OFFSET_ANGLE_DEG);
    }

    public void setAngle(double angle) {
        targetPos = angle;

    }

    public boolean isAtTarget() {
        return m_servo.atTargetPosition();
    }

    public double getCurrentAngle() {
        return (m_encoder.getVoltage() / 3.3) *  360;
    }

    public double getError() {
        return targetPos - getCurrentAngle();
    }

    public void log() {
        telemetry.addData("Error", getError());
        telemetry.addData("Current Angle", getCurrentAngle());
        telemetry.addData("Target Angle", targetPos);
        telemetry.update();
    }

    @Override
    public void periodic() {
        m_servo.set(targetPos);
        log();
    }
}
