package org.firstinspires.ftc.teamcode.config.subsystems.old;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorGroup;

import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;

@Configurable
public class Lift extends SubsystemBase {

    private MotorGroup m_lift;
    private Motor m_left;
    private Motor m_right;

    public static int ARISE_TICK_COUNT = 1200;
    public static int RESET_TICK_COUNT = 0;
    public static double kP = 0.5;
    private int targetTickCount;


    public static int TOLERANCE = 15;

    public Lift(HardwareMap hwMap) {
        m_left = new Motor(hwMap, "leftLiftMotor");
        m_right = new Motor(hwMap, "rightLiftMotor");

        m_lift = new MotorGroup(m_left, m_right);
        m_lift.setRunMode(Motor.RunMode.PositionControl);
        m_lift.setPositionTolerance(TOLERANCE);
        m_lift.setPositionCoefficient(kP);
    }

    public void arise() {
        m_lift.setTargetPosition(ARISE_TICK_COUNT);
    }

    public void reset() {
        m_lift.setTargetPosition(RESET_TICK_COUNT);
    }

    @Override
    public void periodic() {
        m_lift.set(1);
    }
}
