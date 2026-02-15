package org.firstinspires.ftc.teamcode.config.subsystems.old;

import static org.firstinspires.ftc.teamcode.config.util.AngularUtil.wrap360;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.IgnoreConfigurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;
import org.firstinspires.ftc.teamcode.config.subsystems.turret.TurretConstants;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.AngularUtil;
import org.firstinspires.ftc.teamcode.config.util.FieldConstants;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;
import org.firstinspires.ftc.teamcode.config.util.SorterNode;

import java.util.List;
import java.util.Map;

@Configurable
public class ManualTurretSubsystem extends SubsystemBase {

    private final Limelight3A m_limelight;
    private final Motor m_motor;
    private final Telemetry m_telemetry;
    private Follower m_follower;

    private final int BLUE_TAG = 20;
    private final int RED_TAG = 24;

    private static final Map<Integer, SorterNode.NodeOption[]> MOTIF_MAP =
            Map.of(
                    21, new SorterNode.NodeOption[]{
                            SorterNode.NodeOption.GREEN,
                            SorterNode.NodeOption.PURPLE,
                            SorterNode.NodeOption.PURPLE
                    },
                    22, new SorterNode.NodeOption[]{
                            SorterNode.NodeOption.PURPLE,
                            SorterNode.NodeOption.GREEN,
                            SorterNode.NodeOption.PURPLE
                    },
                    23, new SorterNode.NodeOption[]{
                            SorterNode.NodeOption.PURPLE,
                            SorterNode.NodeOption.PURPLE,
                            SorterNode.NodeOption.GREEN
                    }
            );

    private enum TurretState {
        SEARCHING,
        LOCKED
    }
    private TurretState state;
    private Pose targetPose;

    private LLResult results;
    private boolean foundMotifTag = false;
    private boolean foundGoalTag = false;

    private List<LLResultTypes.FiducialResult> foundTags;
    @IgnoreConfigurable

    private double targetAngle;
    private double currentAngle;
    private int targetTicks;

    private double initialAngle;
    //TODO: boolean value for alliance side

    public ManualTurretSubsystem(HardwareMap hwMap, Telemetry telemetry, Follower follower) {
        m_limelight = hwMap.get(Limelight3A.class, TurretConstants.HW.LIMELIGHT);
        m_motor = new Motor(hwMap, TurretConstants.HW.MOTOR,28, 435);
        m_telemetry = telemetry;
        m_follower = follower;

        m_motor.setRunMode(Motor.RunMode.PositionControl);
        m_motor.setInverted(true);
        m_motor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        m_motor.setPositionCoefficient(TurretConstants.ROTATE.kP);
        m_motor.setPositionTolerance(TurretConstants.ROTATE.TOLERANCE);
        m_motor.resetEncoder();

        m_limelight.setPollRateHz(TurretConstants.VISION.POLL_RATE);
        m_limelight.pipelineSwitch(8);
        m_limelight.start();

        state = TurretState.SEARCHING;
        targetPose = FieldConstants.obeliskPose;
    }

    //        /**
//         * Set desired turret angle [0-360] (avoids repeated spinning)
//         * @param angle degrees [0-360]
//         */
//        public void setClosestAngle(double angle) {
//            double currentAngle = ticksToDegrees(m_motor.getCurrentPosition());
//            double wrappedCurrent = AngularUtil.wrap360(currentAngle);
//        }
//
//        /**
//         * Increment turret angle [0-360]
//         * @param angle degrees [0-360]
//         */
//        public void incrementAngle(double angle) {
//
//        }
//        private void search() {
//            int increment = 20;
//            if (m_motor.atTargetPosition()) {
//                targetAngle += isLastRight ? increment : -increment;
//            }
//        }
//        private void lock() {
//            goal_tx = (int) goalTag.getTargetXDegrees();
//            isLastRight = goal_tx > 0;
//            m_telemetry.addData("TargetX Degrees", goal_tx);
//
//            if (m_motor.atTargetPosition()) {
//                targetAngle += goal_tx;
//            }
//
//        }
    //checking if found game motif & if goal wanted found
    private void scanTags() {
        foundGoalTag = false;
        for (LLResultTypes.FiducialResult curTag : foundTags) {
            int curID = curTag.getFiducialId();

            //motif mapping
            if (MOTIF_MAP.containsKey(curID) && !foundMotifTag) {
                MatchValues.matchMotif = MOTIF_MAP.get(curID);
                foundMotifTag = true;
                continue;
            }

            if (curID == BLUE_TAG && MatchValues.alliance == Alliance.BLUE) {
                foundGoalTag = true;
                break;
            }

            if (curID == RED_TAG && MatchValues.alliance == Alliance.RED) {
                foundGoalTag = true;
            }

            if (foundGoalTag && foundMotifTag) break;
        }
    }

    /* angle updates */
    static double MOTOR_TICKS_PER_REV = 384.5;
    public static double EXTERNAL_GEAR_RATIO = 4.37599955; // motor revs per turret rev

    static double TICKS_PER_TURRET_REV = MOTOR_TICKS_PER_REV * EXTERNAL_GEAR_RATIO;

    public void rotateTo180() {
        targetTicks += degreesToTicks(180*20);
    }

    public void rotateTo0() {
        targetTicks -= degreesToTicks(180*20);
    }

    public void increment30() {
        targetTicks += degreesToTicks(90);
    }

    public void decrement30() {
        targetTicks -= degreesToTicks(90);
    }


    public static double ticksToDegrees(double ticks) {
        return ticks * (360.0 / TICKS_PER_TURRET_REV);
    }

    public static int degreesToTicks(double degrees) {
        return (int) Math.round(degrees * (TICKS_PER_TURRET_REV / 360.0));
    }

    public double calculateTurretAngle(Pose currentPose, Pose goalPose) {
        Pose turretPoseRelativeToRobot = getAdjustedPose(currentPose, 12);

        m_telemetry.addData("turretPoseX", turretPoseRelativeToRobot.getX());
        m_telemetry.addData("turretPoseY", turretPoseRelativeToRobot.getY());
        double deltaX = goalPose.getX() - turretPoseRelativeToRobot.getX();
        double deltaY = goalPose.getY() - turretPoseRelativeToRobot.getY();

        //translational angle adjustment
        double absoluteAngleToTarget = Math.toDegrees(Math.atan2(deltaY, deltaX));
        m_telemetry.addData("Translational Angle To Target", absoluteAngleToTarget);
        m_telemetry.addData("Dx", deltaX);
        m_telemetry.addData("Dy", deltaY);
        double robotHeading = wrap360(Math.toDegrees(currentPose.getHeading()));

        // turret angle = (target angle) - (robot heading - 90°)
        m_telemetry.addData("Robot Heading", robotHeading);
        double turretAngleToTarget = absoluteAngleToTarget - (robotHeading - 90.0);

        turretAngleToTarget = wrap360(turretAngleToTarget);
        m_telemetry.addData("Relative Angle to Target", turretAngleToTarget);

        return turretAngleToTarget;
    }

    public static Pose getAdjustedPose(Pose currentPose, double offsetDistanceCm) {
        double headingRad = currentPose.getHeading();
        offsetDistanceCm /= 2.54;

        double offsetX = -offsetDistanceCm * Math.cos(headingRad);
        double offsetY = -offsetDistanceCm * Math.sin(headingRad);

        return new Pose(currentPose.getX() + offsetX, currentPose.getY() + offsetY, currentPose.getHeading()
        );
    }

    public void updateTargetTicks() {
        targetAngle = calculateTurretAngle(m_follower.getPose(), targetPose);
        currentAngle = ticksToDegrees(m_motor.getCurrentPosition());
        targetTicks = degreesToTicks(AngularUtil.turretDelta(targetAngle));
    }

    @Override
    public void periodic() {
//        m_follower.update();
        results = m_limelight.getLatestResult();
        foundTags = results.getFiducialResults();
        scanTags();

        state = (foundMotifTag) ? TurretState.LOCKED : TurretState.SEARCHING;

        switch (state) {
            case SEARCHING:
                targetPose = FieldConstants.obeliskPose;
                m_telemetry.addLine("Turret: Searching");

                break;
            case LOCKED:
                targetPose = MatchValues.alliance == Alliance.BLUE ? FieldConstants.blueGoalPose : FieldConstants.redGoalPose;
                m_telemetry.addLine("Turret: Locked");
                break;
        }

        m_motor.setTargetPosition(targetTicks);
        m_motor.set(TurretConstants.ROTATE.POWER);
        log();
    }

    private void log() {

//            m_telemetry.addData("Done Searching", foundMotifTag ? "✔️" : "✖️");
//            m_telemetry.addData("Target Angle:", targetAngle);
        m_telemetry.addData("Motor Angle:", ticksToDegrees(m_motor.getCurrentPosition()));
        m_telemetry.addData("Motor Angle 360ed:", wrap360(ticksToDegrees(m_motor.getCurrentPosition())));
        m_telemetry.addData("Motor Ticks:", m_motor.getCurrentPosition());
        m_telemetry.addData("Target Angle:", wrap360(ticksToDegrees(targetTicks)));
        m_telemetry.addData("X:", m_follower.getPose().getX());
        m_telemetry.addData("Y:", m_follower.getPose().getY());
        m_telemetry.addData("H:", wrap360(Math.toDegrees(m_follower.getPose().getHeading())));

        m_telemetry.update();
    }
}
