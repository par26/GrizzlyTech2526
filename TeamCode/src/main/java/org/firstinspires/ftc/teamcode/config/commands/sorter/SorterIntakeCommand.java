package org.firstinspires.ftc.teamcode.config.commands.sorter;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.commands.CommandBase;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterServo;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterSensor;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;
import org.firstinspires.ftc.teamcode.config.util.SorterNode;

public class SorterIntakeCommand extends CommandBase {
    private final SorterSensor m_sensor;
    private final SorterServo m_servo;
    private Telemetry telemetry;

    private int intakeCount;
    private MatchValues.RobotState nextState;

    SorterNode[] nodeArray;

    public SorterIntakeCommand(SorterSensor sensor, SorterServo servo, Telemetry telemetry) {
        this.m_sensor = sensor;
        this.m_servo = servo;
        this.telemetry = telemetry;
        addRequirements(sensor, servo);
    }

    @Override
    public void initialize() {
        intakeCount = 2;
        nodeArray = new SorterNode[] {m_sensor.sorterNode1, m_sensor.sorterNode2, m_sensor.sorterNode3};
        nextState = MatchValues.RobotState.INTAKE;
        MatchValues.robotState = MatchValues.RobotState.INTAKE;
    }

    @Override
    public void execute() {
        telemetry.addData("Count: ", intakeCount);
        SorterNode.NodeOption chosen =
                m_sensor.filtered1 != SorterNode.NodeOption.EMPTY ? m_sensor.filtered1 :
                m_sensor.filtered2 != SorterNode.NodeOption.EMPTY ? m_sensor.filtered2 :
                SorterNode.NodeOption.EMPTY;

        if (m_servo.isAtTarget() && chosen != SorterNode.NodeOption.EMPTY) {
            m_servo.rotateCC();
            nodeArray[intakeCount].setNode(chosen);
            intakeCount--;

            if (intakeCount < 0) {
                nextState = MatchValues.RobotState.SHOOT;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return nextState == MatchValues.RobotState.SHOOT;
    }

    @Override
    public void end(boolean interrupted) {
        MatchValues.robotState = MatchValues.RobotState.SHOOT;

        m_sensor.initIndexMap();
    }
}
