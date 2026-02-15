package org.firstinspires.ftc.teamcode.config.subsystems.sorter;

import android.graphics.Color;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;
import org.firstinspires.ftc.teamcode.config.util.SorterNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class SorterSensor extends SubsystemBase {

//    private final RTP m_rtp;
    private final RevColorSensorV3 sensor1;
    private final RevColorSensorV3 sensor2;

    public SorterNode sorterNode1;
    public SorterNode sorterNode2;
    public SorterNode sorterNode3;

    private SorterNode.NodeOption raw1;
    private SorterNode.NodeOption raw2;
    public SorterNode.NodeOption filtered1;
    public SorterNode.NodeOption filtered2;

    public Deque<SorterNode> nodeDeque;
    public static final int SHOOT_INDEX = 1;

    public SorterSensor(HardwareMap hwMap) {
        sensor1 = hwMap.get(RevColorSensorV3.class, SorterConstants.HW.SENSOR1);
        sensor2 = hwMap.get(RevColorSensorV3.class, SorterConstants.HW.SENSOR2);

        sensor1.enableLed(true);
        sensor2.enableLed(true);

        //depending on preload
        sorterNode1 = new SorterNode(SorterNode.NodeOption.PURPLE);
        sorterNode2 = new SorterNode(SorterNode.NodeOption.PURPLE);
        sorterNode3 = new SorterNode(SorterNode.NodeOption.PURPLE);

    }

    //where index 1 = shooting index, and when switched to shooting mode, we first rotate offset clockwise
    public void initIndexMap() {
        nodeDeque = new ArrayDeque<>(List.of(sorterNode1, sorterNode3, sorterNode2));
    }

    // Rotate deque Counterclockwise: [1,3,2] -> [3,2,1]
    public void rotateModelCC() {
        nodeDeque.addLast(nodeDeque.removeFirst());
    }

    // Rotate deque Clockwise
    public void rotateModelC() {
        nodeDeque.addFirst(nodeDeque.removeLast());
    }

    public SorterNode getDequeNode(int index) {
        int i = 0;
        for (SorterNode n : nodeDeque) {
            if (i == index) return n;
            i++;
        }
        throw new IllegalArgumentException("Bad index: " + index);
    }

    public boolean anyBallsLeft() {
        for (SorterNode n : nodeDeque) {
            if (n.getStoredNode() != SorterNode.NodeOption.EMPTY) return true;
        }
        return false;
    }

    public boolean containsColor(SorterNode.NodeOption color) {
        for (SorterNode n : nodeDeque) {
            if (n.getStoredNode() == color) return true;
        }
        return false;
    }

    public int indexOfFirst(SorterNode.NodeOption color) {
        if (getDequeNode(SHOOT_INDEX).getStoredNode() == color) return SHOOT_INDEX;

        if (getDequeNode(0).getStoredNode() == color) return 0;
        if (getDequeNode(2).getStoredNode() == color) return 2;

        return -1;
    }

    private void updateSensors() {
        raw1 = checkSensor(sensor1);
        raw2 = checkSensor(sensor2);

        filtered1 = sensor1Filter.update(raw1);
        filtered2 = sensor2Filter.update(raw2);

    }
    private SorterNode.NodeOption checkSensor(RevColorSensorV3 node) {
        NormalizedRGBA n = node.getNormalizedColors();
        float[] hsv = new float[3];
        int argb = n.toColor();
        Color.colorToHSV(argb, hsv);

        float hue = hsv[0];

        boolean isGreen = hue >= SorterConstants.Detect.GREEN_HUE_MIN && hue <= SorterConstants.Detect.GREEN_HUE_MAX;
        boolean isPurple = hue >= SorterConstants.Detect.PURPLE_HUE_MIN && hue <= SorterConstants.Detect.PURPLE_HUE_MAX;

        if (isGreen) return SorterNode.NodeOption.GREEN;
        if (isPurple) return SorterNode.NodeOption.PURPLE;
        return SorterNode.NodeOption.EMPTY;
    }

    @Override
    public void periodic() {
        if (MatchValues.robotState == MatchValues.RobotState.INTAKE) {
            updateSensors();
        }

    }

    private final StableNodeFilter sensor1Filter = new StableNodeFilter(6, 8);
    private final StableNodeFilter sensor2Filter = new StableNodeFilter(6, 8);

    private static class StableNodeFilter {
        private SorterNode.NodeOption stable = SorterNode.NodeOption.EMPTY;
        private SorterNode.NodeOption candidate = SorterNode.NodeOption.EMPTY;
        private int streak = 0;

        private final int confirmLoops;   // loops needed to accept a new value
        private final int dropLoops;      // loops needed to drop to EMPTY (optional)

        StableNodeFilter(int confirmLoops, int dropLoops) {
            this.confirmLoops = confirmLoops;
            this.dropLoops = dropLoops;
        }

        SorterNode.NodeOption update(SorterNode.NodeOption raw) {
            // ignore if raw == stable
            if (raw == stable) {
                candidate = raw;
                streak = 0;
                return stable;
            }

            // trakc a candidate change.
            if (raw != candidate) {
                candidate = raw;
                streak = 1;
            } else {
                streak++;
            }

            // diff threshold depends on if empty
            int needed = (candidate == SorterNode.NodeOption.EMPTY) ? dropLoops : confirmLoops;

            if (streak >= needed) {
                stable = candidate;
                streak = 0;
            }

            return stable;
        }
    }


}

