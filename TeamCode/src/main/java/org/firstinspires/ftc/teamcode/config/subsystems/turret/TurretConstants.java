package org.firstinspires.ftc.teamcode.config.subsystems.turret;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class TurretConstants {

    private TurretConstants() {}

    public static Turret turret = new Turret();
    public static class Turret {
        public ROTATE rotate = new ROTATE();
        public VISION vision = new VISION();
    }

    public static class HW {
        public static final String LIMELIGHT = "limelight";
        public static final String MOTOR = "turretMotor";

        private HW() {};
    }

    public static class VISION {
        public static int POLL_RATE = 250;
    }

    //tuned :checkmark:
    public static class ROTATE {
        public static double POWER = 0.60;
        public static double kP = 0.01;
        public static double TOLERANCE = 10;

        public static double OFFSET_AMPLIFIER = 0.5;
        public static double OFFSET_INTERVAL = 0.5;
        public static double EXTERNAL_GEAR_RATIO = 4.37499955; // motor revs per turret rev
    }
}
