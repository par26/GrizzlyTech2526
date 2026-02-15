package org.firstinspires.ftc.teamcode.config.subsystems;


import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.Subsystem;

public abstract class SubsystemBase implements Subsystem {
    protected String m_name = this.getClass().getSimpleName();

    public SubsystemBase() {
        CommandScheduler.getInstance().registerSubsystem(this);
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public String getSubsystem() {
        return getName();
    }

    public void setSubsystem(String subsystem) {
        setName(subsystem);
    }

    public abstract void periodic();
}
