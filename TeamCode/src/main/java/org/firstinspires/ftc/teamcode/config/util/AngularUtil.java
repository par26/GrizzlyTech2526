package org.firstinspires.ftc.teamcode.config.util;

public final class AngularUtil {
    private AngularUtil() {}

    public static double wrap360(double angleDeg) {
        double a = angleDeg % 360.0;
        if (a < 0) a += 360.0;
        return a;
    }
    /**
     * employed to prevent >360 movement
     * @param current
     * @param maxAngle
     * @return Degrees
     */
    public static double limitedTurretDelta(double current, int maxAngle) {
        double wrapped = wrap360(current);
        if (wrapped > maxAngle) {
            double deadzoneMid = (360.0 + maxAngle) / 2.0;

            if (wrapped < deadzoneMid) {
                wrapped = maxAngle;
            } else {
                wrapped = 0.0;
            }
        }

        return wrapped;
    }
    /**
     * Overloading using default absoluteMax
     * @param current
     * @return Degrees
     */
    public static double turretDelta(double current) {
        return limitedTurretDelta(current, 330);
    }
}

