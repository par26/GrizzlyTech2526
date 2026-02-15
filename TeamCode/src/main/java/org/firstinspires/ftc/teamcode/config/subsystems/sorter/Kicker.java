package org.firstinspires.ftc.teamcode.config.subsystems.sorter;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;

public class Kicker extends SubsystemBase {
    private Servo m_kicker;

    double curKickerAngle;

    public Kicker(HardwareMap hwMap) {
        m_kicker = hwMap.get(Servo.class, "kicker");
        curKickerAngle = SorterConstants.Kicker.RESET_POS;

    }

    public void activate() {
        curKickerAngle = SorterConstants.Kicker.ACTIVATE_POS;
    }
    public void rest() {
        curKickerAngle = SorterConstants.Kicker.RESET_POS;
    }

    @Override
    public void periodic() {
        m_kicker.setPosition(curKickerAngle);
    }



}
