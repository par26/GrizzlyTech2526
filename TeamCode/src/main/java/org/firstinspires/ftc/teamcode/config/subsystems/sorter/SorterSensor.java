package org.firstinspires.ftc.teamcode.config.subsystems.sorter;

import android.graphics.Color;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;
import org.firstinspires.ftc.teamcode.config.util.SorterNode;

public class SorterSensor extends SubsystemBase {

    private final RevColorSensorV3 sensor1;
    private final RevColorSensorV3 sensor2;
    private final Telemetry telemetry;

    public SorterNode sorterNode1;
    public SorterNode sorterNode2;
    public SorterNode sorterNode3;

    private SorterNode.NodeOption raw1;
    private SorterNode.NodeOption raw2;
    public SorterNode.NodeOption filtered1;
    public SorterNode.NodeOption filtered2;

    public SorterNode[] nodes;

    public SorterSensor(HardwareMap hwMap, Telemetry telemetry) {
        sensor1 = hwMap.get(RevColorSensorV3.class, SorterConstants.HW.SENSOR1);
        sensor2 = hwMap.get(RevColorSensorV3.class, SorterConstants.HW.SENSOR2);
        this.telemetry = telemetry;

        sensor1.enableLed(true);
        sensor2.enableLed(true);

        //depending on preload
        sorterNode1 = new SorterNode(SorterNode.NodeOption.PURPLE);
        sorterNode2 = new SorterNode(SorterNode.NodeOption.PURPLE);
        sorterNode3 = new SorterNode(SorterNode.NodeOption.PURPLE);

        nodes = new SorterNode[] {sorterNode1, sorterNode2, sorterNode3};
    }

    public SorterNode getNode(int index) {
        return nodes[index];
    }

    public boolean anyBallsLeft() {
        for (SorterNode n : nodes) {
            if (n.getStoredNode() != SorterNode.NodeOption.EMPTY) return true;
        }
        return false;
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

    private final StableNodeFilter sensor1Filter = new StableNodeFilter(8, 2);
    private final StableNodeFilter sensor2Filter = new StableNodeFilter(8 , 2);

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

