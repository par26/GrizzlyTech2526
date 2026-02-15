package org.firstinspires.ftc.teamcode.config;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterServo;

public class SorterServoV3 extends SubsystemBase {
    private CRServo servo;

    public SorterServoV3(HardwareMap hwMap) {
        servo = hwMap.get(CRServo.class, "sorterServo");
    }

    @Override
    public void periodic() {
        servo.setPower(0.125);
    }
}
