package org.firstinspires.ftc.teamcode.config.commands.sorter;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.config.commands.CommandBase;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.Kicker;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterConstants;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterSensor;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterServo;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;
import org.firstinspires.ftc.teamcode.config.util.SorterNode;

public class SorterShootCommand extends CommandBase {

    private final SorterSensor m_sensor;
    private final SorterServo m_servo;
    private final Kicker m_kicker;

    private enum State { PICK, ROTATE, FIRE, RESET, DONE }
    private State state;

    private SorterNode.NodeOption targetToShoot = SorterNode.NodeOption.EMPTY;

    private final ElapsedTime timer = new ElapsedTime();
    private int counter = 0;

    public SorterShootCommand(SorterSensor sensor, SorterServo servo, Kicker kicker) {
        m_sensor = sensor;
        m_servo = servo;
        m_kicker = kicker;
        addRequirements(sensor, servo, kicker);
    }

    @Override
    public void initialize() {
        MatchValues.robotState = MatchValues.RobotState.SHOOT;
        state = State.PICK;
        m_kicker.rest();
    }

    @Override
    public void execute() {
        switch (state) {
            case PICK: {
                if (!m_sensor.anyBallsLeft()) {
                    state = State.DONE;
                    break;
                }
                if (getMotifTarget() == SorterNode.NodeOption.EMPTY) break;

                targetToShoot = pickNextToShootMaxPoints();

                state = State.ROTATE;
                break;
            }

            case ROTATE: {
                if (!m_servo.isAtTarget()) break;

                int index = m_sensor.indexOfFirst(targetToShoot);

                if (index == SorterSensor.SHOOT_INDEX) {
                    m_kicker.activate();
                    timer.reset();
                    state = State.FIRE;
                    break;
                }

                if (index == 0) {
                    m_servo.rotateCC();
                    m_sensor.rotateModelCC();
                } else if (index == 2) {
                    m_servo.rotateC();
                    m_sensor.rotateModelC();
                }

                break;
            }

            case FIRE: {
                if (timer.seconds() < SorterConstants.Kicker.HOLD_TIME_MS) break;

                m_sensor.getDequeNode(SorterSensor.SHOOT_INDEX).setNode(SorterNode.NodeOption.EMPTY);

                m_kicker.rest();
                timer.reset();
                state = State.RESET;
                counter++;
                break;
            }

            case RESET: {
                if (timer.seconds() < SorterConstants.Kicker.HOLD_TIME_MS) break;
                state = State.PICK;
                break;
            }

            case DONE:
                break;
        }
    }

    @Override
    public boolean isFinished() {
        return state == State.DONE;
    }

    @Override
    public void end(boolean interrupted) {
        m_kicker.rest();
        if (!interrupted) {
            MatchValues.robotState = MatchValues.RobotState.INTAKE;
        }
    }

    private SorterNode.NodeOption pickNextToShootMaxPoints() {
        SorterNode.NodeOption motifTarget = getMotifTarget();

        if (motifTarget != SorterNode.NodeOption.EMPTY && m_sensor.containsColor(motifTarget)) {
            return motifTarget;
        }

        return fallbackAnyColor();
    }

    private SorterNode.NodeOption fallbackAnyColor() {
        if (m_sensor.containsColor(SorterNode.NodeOption.PURPLE)) return SorterNode.NodeOption.PURPLE;
        if (m_sensor.containsColor(SorterNode.NodeOption.GREEN))  return SorterNode.NodeOption.GREEN;
        return SorterNode.NodeOption.EMPTY;
    }

    private SorterNode.NodeOption getMotifTarget() {
        if (MatchValues.matchMotif == null) return SorterNode.NodeOption.EMPTY;


        return MatchValues.matchMotif[counter];
    }
}
