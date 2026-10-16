package org.firstinspires.ftc.teamcode.config.subsystems.shooter;

import static org.firstinspires.ftc.teamcode.config.subsystems.shooter.ShooterConstants.kP;
import static org.firstinspires.ftc.teamcode.config.subsystems.shooter.ShooterConstants.kS;
import static org.firstinspires.ftc.teamcode.config.subsystems.shooter.ShooterConstants.kV;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.FieldConstants;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;

import smile.interpolation.BilinearInterpolation;
import smile.interpolation.Interpolation2D;

@Configurable
public class Shooter extends SubsystemBase {
    private DcMotorEx m_flywheelMotor;
    private Follower m_follower;
    private Telemetry m_telemetry;

    private final double farThreshold = 120;
    private static double farVelocity = 1400;

    private static final double[] xClose = {22, 46, 70, 94}; //col
    private static final double[] yClose = {6, 30, 54, 66}; //row
//    private static final double[][] closeVelocities = {
//            {0, 0, 0, 0},
//            {0, 1150, 1350, 0},
//            {1200, 1230, 1200, 1160},
//            {1250, 1325, 1333, 1300}
//    };

    private static final double[][] closeVelocities = {
            {1200, 1200, 1200, 1250},
            {1230, 1270, 1290, 1325},
            {1250, 1280, 1310, 1330},
            {1265, 1285, 1310, 1300}
    };


    private static final Interpolation2D closeInterpolation = new BilinearInterpolation(xClose, yClose, closeVelocities);
//    private static final Interpolation2D farInterpolation = new BilinearInterpolation(xFar, yFar, farVelocities);

    private double targetVelocityPerTick;
    private Pose goalPose;

    public Shooter(HardwareMap hwMap, Follower follower, Telemetry telemetry) {
        m_flywheelMotor = hwMap.get(DcMotorEx.class, ShooterConstants.HW.MOTOR);
        this.m_telemetry = telemetry;
        this.m_follower = follower;

        goalPose = MatchValues.alliance == Alliance.BLUE ? FieldConstants.blueGoalPose : FieldConstants.redGoalPose;
    }



    private void calculateTargetVelocity() {
        Pose turretPose = getAdjustedPose(m_follower.getPose(), 5);
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

    private void setPower(double power) {
        m_flywheelMotor.setPower(power);
    }

    private double getVelocity() {
        return m_flywheelMotor.getVelocity();
    }


    @Override
    public void periodic() {
        m_telemetry.addData("Target Velo", targetVelocityPerTick);
        m_telemetry.update();

        calculateTargetVelocity();
        setPower((kV * targetVelocityPerTick) + (kP * (targetVelocityPerTick - getVelocity())) + kS);

    }
}
