package org.firstinspires.ftc.teamcode.config.subsystems.shooter;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

@Configurable
public class ShooterConstants {

    private ShooterConstants() {}

    public static double kP = 0.0;
    public static double kS = 0.0003;
    public static double kV = 0.005;

    public static Shooter shooter = new Shooter();
    public static class Shooter {

    }

    public static class HW {
        public static final String MOTOR = "shooterMotor";


        private HW() {};
    }


}
