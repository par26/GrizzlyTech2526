package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.lynx.LynxModule;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.config.commands.IntakeStateCommand;
import org.firstinspires.ftc.teamcode.config.commands.ShootStateCommand;
import org.firstinspires.ftc.teamcode.config.commands.sorter.SorterIntakeCommand;
import org.firstinspires.ftc.teamcode.config.commands.sorter.SorterShootCommand;
//import org.firstinspires.ftc.teamcode.subsystems.sorter.Sorter;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;
import org.firstinspires.ftc.teamcode.config.util.Robot;

public class TeleOp extends CommandOpMode {

    //Subsystems
    private GamepadEx gp1;
    private GamepadEx gp2;
    private Robot r;

    private Alliance alliance;

    public TeleOp(Alliance alliance) {
        this.alliance = alliance;
    }

    @Override
    public void initialize() {
        gp1 = new GamepadEx(gamepad1);
        r = new Robot(hardwareMap, alliance, telemetry);

        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        r.register(this);

        r.drive.setDefaultCommand(
                new DriveCommand(
                        r.drive,
                        () -> gp1.getLeftY(),
                        () -> -gp1.getLeftX(),
                        () -> -gp1.getRightX(),
                        () -> true,
                        () -> gp1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).get() ? 0.35 : 1.0
                )
        );

        gp1.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(new InstantCommand(() -> r.local.relocalize(), r.local));

        new Trigger(() -> MatchValues.robotState == MatchValues.RobotState.SHOOT)
                .whenActive(
                        new ShootStateCommand(r.intake, r.servo)
                );

        new Trigger(() -> MatchValues.robotState == MatchValues.RobotState.SHOOT && gp2.isDown(GamepadKeys.Button.RIGHT_BUMPER))
                .whileActiveOnce(
                        new SorterShootCommand(r.sensor, r.servo, r.kicker)
                );

        new Trigger(() -> MatchValues.robotState == MatchValues.RobotState.INTAKE)
                .whenActive(new SequentialCommandGroup(
                        new IntakeStateCommand(r.intake, r.servo),
                        new SorterIntakeCommand(r.sensor, r.servo)
                ));




    }
}
