package org.firstinspires.ftc.teamcode.config.subsystems.shooter;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

@Configurable
public class ShooterConstants {

    private ShooterConstants() {}

//    public static double kP = 0.000165;
//    public static double kS = 0.038;
//    public static double kV = 0.00038;
    public static double kP = 0.007;
    public static double kS = 0.06515;
//    public static double kV = 0.00035;
//    public static double kV = 0.0004629;
    public static double kV = 0.000384;
    public static Shooter shooter = new Shooter();
    public static class Shooter {

    }

    public static class HW {
        public static final String MOTOR = "shooterMotor";


        private HW() {};
    }


}
