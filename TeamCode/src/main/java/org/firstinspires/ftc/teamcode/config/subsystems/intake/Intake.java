package org.firstinspires.ftc.teamcode.config.subsystems.intake;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;

@Configurable
public class Intake extends SubsystemBase {

    private Servo m_servo;
    private Motor m_motor;

    private double servoPosition = IntakeConstants.RAISE.RAISED_ANGLE;
    private double motorSpeed = IntakeConstants.SPIN.INTAKE_POWER;

    public Intake(HardwareMap hwMap) {
        m_servo = hwMap.get(Servo.class, IntakeConstants.HW.SERVO);
        m_servo.setDirection(IntakeConstants.HW.SERVO_DIRECTION);

        //TODO: may need to adjust cpr & rpm
        m_motor = new Motor(hwMap, IntakeConstants.HW.MOTOR,28, 6000);
        m_motor.setRunMode(Motor.RunMode.RawPower);

    }

    public void raiseIntake() {
        motorSpeed = 0;
        servoPosition = IntakeConstants.RAISE.RAISED_ANGLE;
    }

    public void lowerIntake() {
        servoPosition = IntakeConstants.RAISE.LOWERED_ANGLE;
        motorSpeed = IntakeConstants.SPIN.INTAKE_POWER;
        m_motor.setInverted(false);
    }

    public void intake() {
        motorSpeed = IntakeConstants.SPIN.INTAKE_POWER;
        m_motor.setInverted(false);
    }

    public void reverse() {
        motorSpeed = IntakeConstants.SPIN.REVERSE_POWER;
        m_motor.setInverted(true);
    }

    @Override
    public void periodic() {
        m_servo.setPosition(servoPosition);
        m_motor.set(motorSpeed);

    }
}
