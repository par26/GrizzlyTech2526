package org.firstinspires.ftc.teamcode.config.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SorterServoV2 extends SubsystemBase{

    private Servo servo;
    private Telemetry m_telemetry;
    private enum Positions {
        POSITION1,
        POSITION2,
        POSITION3
    }

    private Positions pos;
    private double position = 0;
    private double offsetPosition = 0;

    public SorterServoV2(HardwareMap hwMap, Telemetry telemetry) {
        servo = hwMap.get(Servo.class, "sorterServo");
        pos = Positions.POSITION1;
        m_telemetry = telemetry;
    }

    public void rotateNode() {
        switch(pos) {
            case POSITION1:
                position = 0;
                pos = Positions.POSITION2;
                break;
            case POSITION2:
                position = 0.425;
                pos = Positions.POSITION3;
                break;
            case POSITION3:
                position = 0.765;
                pos = Positions.POSITION1;
                break;
        }
    }

    public void applyOffset() {
        offsetPosition = 0.1;
    }
    public void removeOffset() {
        offsetPosition = 0;
    }

    @Override
    public void periodic() {
        servo.setPosition(position + offsetPosition);
        m_telemetry.addData("Data", position + offsetPosition);
        m_telemetry.update();
    }
}
