package org.firstinspires.ftc.teamcode.config.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Solvers extends SubsystemBase{

    private Telemetry m_telemetry;
    public boolean trigger1 = false;
    public boolean trigger2 = false;
    public boolean trigger3 = false;
    public boolean trigger4 = false;

    public Solvers(Telemetry telemetry){
        this.m_telemetry = telemetry;
    }

    public void activateTrigger1() {
        trigger1 = true;
    }

    public void deactiveTrigger1() {
        trigger1 = false;
    }

    public void activateTrigger2() {
        trigger2 = true;
    }

    public void deactiveTrigger2() {
        trigger2 = false;
    }

    public void activateTrigger3() {
        trigger3 = true;
    }

    public void deactivateTrigger3() {
        trigger3 = false;
    }

    public void activateTrigger4() {
        trigger4 = true;
    }

    public void deactivateTrigger4() {
        trigger4 = false;
    }


    private void log() {
        m_telemetry.addData("Trigger 1", trigger1);
        m_telemetry.addData("Trigger 2", trigger2);
        m_telemetry.addData("Trigger 3", trigger3);
        m_telemetry.addData("Trigger 4", trigger4);
    }

    @Override
    public void periodic() {
        log();

        m_telemetry.update();
    }
}
