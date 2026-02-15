package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.commands.IntakeStateCommand;
import org.firstinspires.ftc.teamcode.config.commands.ShootStateCommand;
import org.firstinspires.ftc.teamcode.config.commands.sorter.SorterIntakeCommand;
import org.firstinspires.ftc.teamcode.config.commands.sorter.SorterShootCommand;
import org.firstinspires.ftc.teamcode.config.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.Kicker;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterSensor;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterServo;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;
import org.firstinspires.ftc.teamcode.config.util.SorterNode;

@TeleOp(group = "test")
public class TestSorter extends CommandOpMode {

    private Intake m_intake;
    private SorterServo m_servo;
    private Kicker m_kicker;
    private SorterSensor m_sensor;

    private GamepadEx gp1;


    @Override
    public void initialize() {
        MatchValues.robotState = MatchValues.RobotState.INTAKE;
        MatchValues.matchMotif = new SorterNode.NodeOption[] {SorterNode.NodeOption.PURPLE, SorterNode.NodeOption.GREEN, SorterNode.NodeOption.PURPLE};

        m_intake = new Intake(hardwareMap);
        m_sensor = new SorterSensor(hardwareMap);
        m_kicker = new Kicker(hardwareMap);
        m_servo = new SorterServo(hardwareMap, telemetry);
        gp1 = new GamepadEx(gamepad1);

        register(m_intake, m_sensor, m_kicker, m_servo);

        new Trigger(() -> MatchValues.robotState == MatchValues.RobotState.SHOOT)
                .whenActive(
                        new ShootStateCommand(m_intake, m_servo)
                );

        new Trigger(() -> MatchValues.robotState == MatchValues.RobotState.SHOOT && gp1.isDown(GamepadKeys.Button.RIGHT_BUMPER))
                .whileActiveOnce(
                        new SorterShootCommand(m_sensor, m_servo, m_kicker)
                );

        new Trigger(() -> MatchValues.robotState == MatchValues.RobotState.INTAKE)
                .whenActive(new SequentialCommandGroup(
                        new IntakeStateCommand(m_intake, m_servo),
                        new SorterIntakeCommand(m_sensor, m_servo, telemetry)
                ));
    }
}
