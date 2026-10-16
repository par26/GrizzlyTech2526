package org.firstinspires.ftc.teamcode.config.subsystems.sorter;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class SorterConstants {

    private SorterConstants() {};

    public static Sorter sorter = new Sorter();
    public static class Sorter {
        public Detect detect = new Detect();
        public Index index = new Index();
        public Kicker kicker = new Kicker();
    }

    /** Hardware Names */
    public static final class HW {
        public static final String ENCODER = "sorterEncoder";
        public static final String SERVO = "sorterServo";
        public static final String KICKER  = "kicker";
        public static final String SENSOR1 = "sensor1";
        public static final String SENSOR2 = "sensor2";

        public static final String NODE2   = "node2";
        public static final String NODE3   = "node3";

        private HW() {}
    }

    /** Node detection thresholds / filtering */
    public static final class Detect {
        public static double GREEN_HUE_MIN  = 150;
        public static double GREEN_HUE_MAX  = 170;

        public static double PURPLE_HUE_MIN = 230;
        public static double PURPLE_HUE_MAX = 245;
    }

    /** Sorter rotation / indexing behavior */
    public static final class Index {
        public static double[] intakePositions = {0.07, 0.43, 0.80};
        public static double[] shootPositions = {0.25, 0.62, 0.97};
        public static double nodeRotationSeconds = 0.5;
        public static double offsetRotationSeconds = 0.3;
    }

    /** Kicker servo positions + timing */
    public static final class Kicker {
        public static double ACTIVATE_POS = 0.59;
        public static double RESET_POS    = 0.00;

        public static long HOLD_TIME_MS  = 600;
    }
}
