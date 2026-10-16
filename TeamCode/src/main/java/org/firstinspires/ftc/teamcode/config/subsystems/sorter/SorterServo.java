package org.firstinspires.ftc.teamcode.config.subsystems.sorter;

import static org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterConstants.Index.intakePositions;
import static org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterConstants.Index.shootPositions;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;
import org.firstinspires.ftc.teamcode.config.util.MatchValues;

public class SorterServo extends SubsystemBase {

    private Servo servo;
    private CRServoEx m_servo;
    private Telemetry m_telemetry;
    private ElapsedTime rotationTimer;

    private double[] curPositions;
    private double curPosition;
    private boolean isIntakePosition = true;

    public static boolean isRotating = false;
    private double rotationTime;

    public SorterServo(HardwareMap hwMap, Telemetry telemetry) {
        rotationTimer = new ElapsedTime();
        servo = hwMap.get(Servo.class, "sorterServo");
        m_telemetry = telemetry;

        if (MatchValues.robotState == MatchValues.RobotState.INTAKE) {
            removeOffset();
        } else {
            applyOffset();
        }
        setPosition(0);
    }

    /**
     * Sets position of the servo depending on the curPositions array
     * @param index index 0-2
     */
    public void setPosition(int index) {
        double current = curPosition;
        double target = curPositions[index];
        double dist = Math.abs(target - current);
        rotationTime = dist * 0.8;

        curPosition = curPositions[index];
        rotationTimer.reset();
    }

    public boolean isFinishedRotating() {
        return rotationTimer.seconds() >= rotationTime;
    }

    public boolean isIntakePosition() {
        return isIntakePosition;
    }

    public void applyOffset() {
        curPositions = shootPositions;
        setPosition(0);
        rotationTimer.reset();
        isIntakePosition = false;
    }
    public void removeOffset() {
        curPositions = intakePositions;
        setPosition(0);
        rotationTimer.reset();
        isIntakePosition = true;
    }

    @Override
    public void periodic() {
        if (isFinishedRotating()) {
            isRotating = false;
        } else {
            isRotating = true;
        }

        servo.setPosition(curPosition);
    }
}
