package org.firstinspires.ftc.teamcode.config.subsystems.old;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;

@Configurable
public class Outtake extends SubsystemBase {
    private Motor m_motor;
    public static double LAUNCH_POWER_CLOSE = 0.41;
    public static double LAUNCH_POWER_FAR = 0.50;

    //tune values in prod
    public static double p = 0.7;
    public static double i = 0.2;
    public static double d = 0.5;

    private double power;

    public Outtake(HardwareMap hwMap) {
        m_motor = new Motor(hwMap, "flyWheel",28, 6000);
        m_motor.setRunMode(Motor.RunMode.VelocityControl);
        m_motor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        m_motor.setVeloCoefficients(p, i, d);

        power = LAUNCH_POWER_CLOSE;
        m_motor.resetEncoder();
    }

    public void spin(boolean isThree) {
        m_motor.setRunMode(Motor.RunMode.VelocityControl);
        power = isThree ? LAUNCH_POWER_FAR : LAUNCH_POWER_CLOSE;
    }

    @Override
    public void periodic() {
        m_motor.set(power);
    }
}
