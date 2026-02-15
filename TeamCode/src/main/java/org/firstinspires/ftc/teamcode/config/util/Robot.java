package org.firstinspires.ftc.teamcode.config.util;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.config.subsystems.Drive;
import org.firstinspires.ftc.teamcode.config.subsystems.Localization;
import org.firstinspires.ftc.teamcode.config.subsystems.intake.Intake;
//import org.firstinspires.ftc.teamcode.config.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.Kicker;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterSensor;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterServo;
import org.firstinspires.ftc.teamcode.config.subsystems.turret.Turret;

import java.util.List;

//TODO: Restructure code
public class Robot {

    public final Intake intake;
    public final Turret turret;
//    public final Shooter shooter;
    public final SorterSensor sensor;
    public final SorterServo servo;
    public final Kicker kicker;
    public final Drive drive;
    public final Follower follower;
    public final Localization local;
    public Alliance a;

    private final List<LynxModule> hubs;

    public Robot(HardwareMap hwMap, Alliance a, Telemetry telemetry) {
        this.a = a;
        initAllianceSpecific();
        follower = Constants.createFollower(hwMap);
        intake = new Intake(hwMap);
        turret = new Turret(hwMap, follower, telemetry);
//        shooter = new Shooter(hwMap, follower);
        servo = new SorterServo(hwMap);
        sensor = new SorterSensor(hwMap);
        kicker = new Kicker(hwMap);
        drive = new Drive(hwMap, follower);
        local = new Localization(telemetry, follower);


        hubs = hwMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    private void initAllianceSpecific() {
        MatchValues.goalPose = a == Alliance.BLUE ? FieldConstants.blueGoalPose : FieldConstants.redGoalPose;
        MatchValues.alliance = a;
    }

    public void register(CommandOpMode opmode) {
        opmode.register(intake, turret, sensor, servo, drive, kicker, local);
    }

}
