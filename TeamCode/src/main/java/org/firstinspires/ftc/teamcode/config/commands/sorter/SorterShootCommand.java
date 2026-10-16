package org.firstinspires.ftc.teamcode.config.commands.sorter;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.commands.CommandBase;
import org.firstinspires.ftc.teamcode.config.subsystems.Solvers;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.Kicker;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterConstants;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterSensor;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterServo;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;
import org.firstinspires.ftc.teamcode.config.util.SorterNode;

public class SorterShootCommand extends CommandBase {

    private final SorterSensor m_sensor;
    private final SorterServo  m_servo;
    private final Kicker      m_kicker;
    private final Solvers m_solvers;

    private enum State { BUILD, MOVE, FIRE, RESET, DONE }
    private State state;

    private final ElapsedTime timer = new ElapsedTime();

    // Up to 3 shots
    private final int[] shootIndexOrder = new int[3]; // servo indices (0/1/2) for SHOOT positions
    private final int[] pocketOrder     = new int[3]; // which pocket (0/1/2) we are firing that step
    private int orderLen  = 0;
    private int orderStep = 0;

    /**
     * Mapping derived from your diagram (SHOOT/FIRE positions):
     * shoot Ind 0 -> pocket 1 at FIRE  -> pocket index 0
     * shoot Ind 1 -> pocket 3 at FIRE  -> pocket index 2
     * shoot Ind 2 -> pocket 2 at FIRE  -> pocket index 1
     *
     * We want pocketIndex -> shootIndex:
     * pocket 0 -> 0
     * pocket 1 -> 2
     * pocket 2 -> 1
     */
    private static final int[] POCKET_TO_SHOOT_INDEX = { 0, 2, 1 };

    public SorterShootCommand(SorterSensor sensor, SorterServo servo, Kicker kicker, Solvers solvers) {
        m_sensor = sensor;
        m_servo  = servo;
        m_kicker = kicker;
        m_solvers = solvers;
        addRequirements(sensor, servo, kicker);
    }

    @Override
    public void initialize() {
        MatchValues.robotState = MatchValues.RobotState.SHOOT;

        m_kicker.rest();
        m_solvers.activateTrigger4();


        // Ensure servo is using shootPositions[] and give time to apply offset
        m_servo.applyOffset();

        // Reset plan
        orderLen = 0;
        orderStep = 0;

        state = State.BUILD;
    }

    @Override
    public void execute() {
        // Wait until offset swap has had time to settle before doing any node timing logic
        if (!m_servo.isFinishedRotating()) return;

        switch (state) {
            case BUILD: {
                if (!m_sensor.anyBallsLeft()) {
                    state = State.DONE;
                    break;
                }

                buildShootOrder();

                if (orderLen == 0) {
                    state = State.DONE;
                    break;
                }

                state = State.MOVE;
                break;
            }

            case MOVE: {
                if (timer.milliseconds() < SorterConstants.Kicker.HOLD_TIME_MS + 300) break;
                if (orderStep >= orderLen) {
                    state = State.DONE;
                    break;
                }

                int shootIndex = shootIndexOrder[orderStep];
                m_servo.setPosition(shootIndex);

                timer.reset();
                state = State.FIRE;
                break;
            }

            case FIRE: {
                if (!m_servo.isFinishedRotating()) break;

                m_kicker.activate();
                timer.reset();

                state = State.RESET;
                break;
            }

            case RESET: {
                if (timer.milliseconds() < SorterConstants.Kicker.HOLD_TIME_MS) break;

                // Clear the REAL pocket we just shot (NOT a constant shoot index)
                int pocketShot = pocketOrder[orderStep];
                m_sensor.getNode(pocketShot).setNode(SorterNode.NodeOption.EMPTY);

                m_kicker.rest();
                timer.reset();
                orderStep++;

                state = (orderStep < orderLen) ? State.MOVE : State.DONE;
                break;
            }

            case DONE:
                break;
        }
    }

    @Override
    public boolean isFinished() {
        if (timer.milliseconds() < SorterConstants.Kicker.HOLD_TIME_MS) return false;
        return state == State.DONE;
    }

    @Override
    public void end(boolean interrupted) {
        m_kicker.rest();
        if (!interrupted) {
            MatchValues.robotState = MatchValues.RobotState.INTAKE;
        }
    }

    /**
     * Because pockets are fixed in order {node1,node2,node3}, the only
     * thing that changes is which pocket is currently aligned to FIRE,
     * which is handled by POCKET_TO_SHOOT_INDEX mapping.
     *
     * We simulate removals only by setting pockets to EMPTY in the local snapshot.
     */
    private void buildShootOrder() {
        orderLen = 0;
        orderStep = 0;

        // Snapshot pocket colors
        SorterNode.NodeOption[] sim = new SorterNode.NodeOption[] {
                m_sensor.getNode(0).getStoredNode(),
                m_sensor.getNode(1).getStoredNode(),
                m_sensor.getNode(2).getStoredNode()
        };

        for (int shot = 0; shot < 3; shot++) {
            if (!anyBalls(sim)) break;

            SorterNode.NodeOption desired = getMotifTarget(shot);

            int pocketIdx = -1;

            // 1) Prefer motif color if it exists
            if (desired != SorterNode.NodeOption.EMPTY) {
                pocketIdx = firstIndexOf(sim, desired);
            }

            // 2) Otherwise fallback to any ball (deterministic: lowest index)
            if (pocketIdx == -1) {
                pocketIdx = firstNonEmptyIndex(sim);
            }

            if (pocketIdx == -1) break;

            pocketOrder[orderLen]     = pocketIdx;
            shootIndexOrder[orderLen] = POCKET_TO_SHOOT_INDEX[pocketIdx];
            orderLen++;

            // simulate this pocket being fired
            sim[pocketIdx] = SorterNode.NodeOption.EMPTY;
        }
    }

    private boolean anyBalls(SorterNode.NodeOption[] sim) {
        return sim[0] != SorterNode.NodeOption.EMPTY
                || sim[1] != SorterNode.NodeOption.EMPTY
                || sim[2] != SorterNode.NodeOption.EMPTY;
    }

    private int firstIndexOf(SorterNode.NodeOption[] sim, SorterNode.NodeOption c) {
        for (int i = 0; i < sim.length; i++) {
            if (sim[i] == c) return i;
        }
        return -1;
    }

    private int firstNonEmptyIndex(SorterNode.NodeOption[] sim) {
        for (int i = 0; i < sim.length; i++) {
            if (sim[i] != SorterNode.NodeOption.EMPTY) return i;
        }
        return -1;
    }

    private SorterNode.NodeOption getMotifTarget(int shotIndex) {
        if (MatchValues.matchMotif == null) return SorterNode.NodeOption.EMPTY;
        if (shotIndex < 0 || shotIndex >= MatchValues.matchMotif.length) return SorterNode.NodeOption.EMPTY;
        return MatchValues.matchMotif[shotIndex];
    }
}