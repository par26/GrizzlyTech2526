package org.firstinspires.ftc.teamcode.config.subsystems.shooter;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.FieldConstants;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;

import smile.interpolation.BilinearInterpolation;
import smile.interpolation.Interpolation2D;

@Configurable
public class Shooter extends SubsystemBase {
    private Motor m_flywheelMotor;
    private Follower m_follower;

    private final double farThreshold = 120;
    private static double farVelocity = 1550;

    private static final double[] xClose = {44, 72, 110};
    private static final double[] yClose = {15, 35, 70};
    private static final double[][] closeVelocities = {
            {1200, 1200, 1275},
            {1275, 1275, 1350},
            {1325, 1360, 1400}
    };

//    private static final double[] xFar = {};
//    private static final double[] yFar = {};
//    private static final double[][] farVelocities = {
//            {},
//            {}
//    };

    private static final Interpolation2D closeInterpolation = new BilinearInterpolation(xClose, yClose, closeVelocities);
//    private static final Interpolation2D farInterpolation = new BilinearInterpolation(xFar, yFar, farVelocities);

    private double targetVelocityPerTick;
    private Pose goalPose;

    public Shooter(HardwareMap hwMap, Follower follower) {
        this.m_follower = follower;
        m_flywheelMotor = new Motor(hwMap, ShooterConstants.HW.MOTOR,28, 6000);
        m_flywheelMotor.setRunMode(Motor.RunMode.VelocityControl);
        m_flywheelMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        m_flywheelMotor.setVeloCoefficients(ShooterConstants.kP, 0,0);
        m_flywheelMotor.setFeedforwardCoefficients(ShooterConstants.kS, ShooterConstants.kV);

        m_flywheelMotor.resetEncoder();
        goalPose = MatchValues.alliance == Alliance.BLUE ? FieldConstants.blueGoalPose : FieldConstants.redGoalPose;
    }

    private void calculateTargetVelocity() {
        Pose turretPose = getAdjustedPose(m_follower.getPose(), 12);
        double dx = Math.abs(goalPose.getX() - turretPose.getX());
        double dy = Math.abs(goalPose.getY() - turretPose.getY());
        double distance = Math.hypot(dx, dy);

        if (distance < farThreshold) {
            targetVelocityPerTick = closeInterpolation.interpolate(dx, dy);
        } else {
            targetVelocityPerTick = farVelocity;
        }
    }

    public static Pose getAdjustedPose(Pose currentPose, double offsetDistanceCm) {
        double headingRad = currentPose.getHeading();
        offsetDistanceCm /= 2.54;

        double offsetX = -offsetDistanceCm * Math.cos(headingRad);
        double offsetY = -offsetDistanceCm * Math.sin(headingRad);

        return new Pose(currentPose.getX() + offsetX, currentPose.getY() + offsetY, currentPose.getHeading()
        );
    }

    @Override
    public void periodic() {
        calculateTargetVelocity();

        m_flywheelMotor.set(targetVelocityPerTick);
    }
}
