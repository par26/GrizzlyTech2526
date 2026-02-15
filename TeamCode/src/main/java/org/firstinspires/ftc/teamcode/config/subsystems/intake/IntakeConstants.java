package org.firstinspires.ftc.teamcode.config.subsystems.intake;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

@Configurable
public class IntakeConstants {

    private IntakeConstants() {}

    public static Intake intake = new Intake();
    public static class Intake {
        public RAISE raise = new RAISE();
        public SPIN spin = new SPIN();
    }

    public static class HW {
        public static final String SERVO = "intakeServo";
        public static final String MOTOR = "intakeMotor";

        public static Servo.Direction SERVO_DIRECTION = Servo.Direction.FORWARD;


        private HW() {};
    }

    public static class RAISE {
        public static double RAISED_ANGLE = 0;
        public static double LOWERED_ANGLE = 0.5;
    }

    public static class SPIN {
        public static double INTAKE_POWER = 0.7;
        public static double REVERSE_POWER = 0.8;
    }
}
