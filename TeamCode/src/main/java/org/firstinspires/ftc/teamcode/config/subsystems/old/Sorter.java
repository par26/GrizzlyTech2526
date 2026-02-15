//package org.firstinspires.ftc.teamcode.subsystems.old;
//
//import android.graphics.Color;
//
//import com.bylazar.configurables.annotations.Configurable;
//import com.qualcomm.hardware.rev.RevColorSensorV3;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.NormalizedRGBA;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.teamcode.subsystems.SubsystemBase;
//import org.firstinspires.ftc.teamcode.subsystems.sorter.SorterConstants;
//import org.firstinspires.ftc.teamcode.subsystems.sorter.SorterServo;
//import org.firstinspires.ftc.teamcode.util.MatchConstants;
//import org.firstinspires.ftc.teamcode.util.SorterNode;
//
//import java.util.ArrayDeque;
//import java.util.Queue;
//
//@Configurable
//public class SorterSensor extends SubsystemBase {
//
//    private final SorterServo m_rtp;
//    private final Servo m_kicker;
//
//    private final RevColorSensorV3 sensor1;
//    private final RevColorSensorV3 sensor2;
//    private final RevColorSensorV3 sensor3;
//
//    public SorterNode sorterNode1;
//    public SorterNode sorterNode2;
//    public SorterNode sorterNode3;
//
//    public SorterNode[] sorterNodes;
//    public SorterNode[] motif;
//
//    private final ElapsedTime kickerTimer = new ElapsedTime();
//
//    private SorterState state;
//
//    private double curKickerAngle;
//    private int deviation;
//
//    private boolean isIntakeState = true;
//
//    public SorterSensor(HardwareMap hwMap, SorterServo rtp) {
//        m_rtp = rtp;
//        m_kicker = hwMap.get(Servo.class, SorterConstants.HW.KICKER);
//
//        sensor1 = hwMap.get(RevColorSensorV3.class, SorterConstants.HW.SENSOR2);
//        sensor2 = hwMap.get(RevColorSensorV3.class, SorterConstants.HW.NODE2);
//        sensor3 = hwMap.get(RevColorSensorV3.class, SorterConstants.HW.NODE3);
//
//        sensor1.enableLed(true);
//        sensor2.enableLed(true);
//        sensor3.enableLed(true);
//
//        sorterNode1 = SorterNode.PURPLE;
//        sorterNode2 = SorterNode.PURPLE;
//        sorterNode3 = SorterNode.PURPLE;
//
//        sorterNodes = new SorterNode[] {sorterNode1, sorterNode2, sorterNode3};
//
//        curKickerAngle = SorterConstants.Kicker.RESET_POS;
//        state = isIntakeState ? SorterState.INTAKE : SorterState.SHOOT;
//    }
//
//    //base states
//
//    public void switchState() {
//        state = isIntakeState() ? SorterState.SHOOT : SorterState.INTAKE;
//    }
//
//    public boolean isIntakeState() {
//        return state == SorterState.INTAKE;
//    }
//
//    public boolean isShootState() {
//        return state == SorterState.SHOOT;
//    }
//
//    //node angle changes
//    public void rotateC() {
//        m_rtp.changeAngle(SorterConstants.Index.NODE_ANGLE_DEG);
//    }
//    public void rotateCC() {
//        m_rtp.changeAngle(-SorterConstants.Index.NODE_ANGLE_DEG);
//    }
//
//    public void changeAngle(int angle) {m_rtp.changeAngle(angle);}
//    public int nodeToShoot(SorterNode node) {
//        int changeAngle = 0;
//
//        if (node == sorterNode1) {
//            changeAngle -= SorterConstants.Index.OFFSET_ANGLE_DEG;
//            deviation -= SorterConstants.Index.OFFSET_ANGLE_DEG;
//        } else if (node == sorterNode2) {
//            changeAngle += SorterConstants.Index.OFFSET_ANGLE_DEG;
//            deviation += SorterConstants.Index.OFFSET_ANGLE_DEG;
//        } else {
//            changeAngle += SorterConstants.Index.OFFSET_ANGLE_DEG + SorterConstants.Index.NODE_ANGLE_DEG;
//            deviation += SorterConstants.Index.OFFSET_ANGLE_DEG;
//        }
//
//        return changeAngle;
//    }
//
//    //kicker methods
//    public void kickerActivate() {
//        curKickerAngle = SorterConstants.Kicker.ACTIVATE_POS;
//    }
//    public void kickerReset() {
//        curKickerAngle = SorterConstants.Kicker.RESET_POS;
//        m_rtp.changeAngle(-deviation);
//    }
//
//    //utility
//    public boolean isAtTarget() {return m_rtp.isAtTarget();}
//
//    public boolean isOccupied(SorterNode node) {
//        return node != SorterNode.EMPTY;
//    }
//    public boolean isEmpty() {
//        return !isOccupied(sorterNode1)
//                && !isOccupied(sorterNode2)
//                && !isOccupied(sorterNode3);
//    }
//    public boolean isFull() {
//        return isOccupied(sorterNode1) && isOccupied(sorterNode3);
//    }
//    private void updateNodes() {
//        SorterNode raw1 = checkNode(sensor1);
//        SorterNode raw2 = checkNode(sensor2);
//        SorterNode raw3 = checkNode(sensor3);
//
//        sorterNode1 = node1Filter.update(raw1);
//        sorterNode2 = node2Filter.update(raw2);
//        sorterNode3 = node3Filter.update(raw3);
//    }
//    private SorterNode checkNode(RevColorSensorV3 node) {
//        NormalizedRGBA n = node.getNormalizedColors();
//        float[] hsv = new float[3];
//        int argb = n.toColor();
//        Color.colorToHSV(argb, hsv);
//
//        float hue = hsv[0];
//
//        boolean isGreen = hue >= SorterConstants.Detect.GREEN_HUE_MIN && hue <= SorterConstants.Detect.GREEN_HUE_MAX;
//        boolean isPurple = hue >= SorterConstants.Detect.PURPLE_HUE_MIN && hue <= SorterConstants.Detect.PURPLE_HUE_MAX;
//
//        if (isGreen) return SorterNode.GREEN;
//        if (isPurple) return SorterNode.PURPLE;
//        return SorterNode.EMPTY;
//    }
//
//    //returns whole array, but only uses index 0 b/c we want to preserve order
//    //
//    public SorterNode[] getNodeOrder() {
//        Queue<SorterNode> greens = new ArrayDeque<>();
//        Queue<SorterNode> purples = new ArrayDeque<>();
//        Queue<SorterNode> empties = new ArrayDeque<>();
//
//        SorterNode[] out = new SorterNode[3];
//
//        //first in first out
//        for (SorterNode n : sorterNodes) {
//            switch (n) {
//                case GREEN: greens.add(n);break;
//                case PURPLE: purples.add(n);break;
//                case EMPTY: empties.add(n); break;
//            }
//        }
//
//        //try to match as many to target node
//        for (int i = 0; i < 3; i++) {
//            SorterNode desired = motif[i];
//            if (desired == SorterNode.GREEN && !greens.isEmpty()) {
//                out[i] = greens.poll();
//            } else if (desired == SorterNode.PURPLE && !purples.isEmpty()) {
//                out[i] = purples.poll();
//            }
//        }
//
//        //add any remaining
//        Queue<SorterNode> remaining = new ArrayDeque<>();
//        remaining.addAll(greens);
//        remaining.addAll(purples);
//        remaining.addAll(empties);
//
//        for (int i = 0; i < 3; i++) {
//            if (out[i] == null) {
//                out[i] = remaining.poll();
//            }
//        }
//
//        return out;
//    }
//
//
//    @Override
//    public void periodic() {
//        updateNodes();
//        m_kicker.setPosition(curKickerAngle);
//
//        if (motif == null) {motif = MatchConstants.matchMotif;}
//    }
//
//    private final StableNodeFilter node1Filter = new StableNodeFilter(4, 6);
//    private final StableNodeFilter node2Filter = new StableNodeFilter(4, 6);
//    private final StableNodeFilter node3Filter = new StableNodeFilter(4, 6);
//
//    private static class StableNodeFilter {
//        private SorterNode stable = SorterNode.EMPTY;
//        private SorterNode candidate = SorterNode.EMPTY;
//        private int streak = 0;
//
//        private final int confirmLoops;   // loops needed to accept a new value
//        private final int dropLoops;      // loops needed to drop to EMPTY (optional)
//
//        StableNodeFilter(int confirmLoops, int dropLoops) {
//            this.confirmLoops = confirmLoops;
//            this.dropLoops = dropLoops;
//        }
//
//        SorterNode update(SorterNode raw) {
//            // ignore if raw == stable
//            if (raw == stable) {
//                candidate = raw;
//                streak = 0;
//                return stable;
//            }
//
//            // trakc a candidate change.
//            if (raw != candidate) {
//                candidate = raw;
//                streak = 1;
//            } else {
//                streak++;
//            }
//
//            // diff threshold depends on if empty
//            int needed = (candidate == SorterNode.EMPTY) ? dropLoops : confirmLoops;
//
//            if (streak >= needed) {
//                stable = candidate;
//                streak = 0;
//            }
//
//            return stable;
//        }
//    }
//}
