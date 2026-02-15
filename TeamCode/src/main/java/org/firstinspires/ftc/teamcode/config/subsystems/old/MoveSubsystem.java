package org.firstinspires.ftc.teamcode.config.subsystems.old;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.config.subsystems.SubsystemBase;

public class MoveSubsystem extends SubsystemBase {

    private DcMotorEx motor1;
    private DcMotorEx motor2;
    private DcMotorEx motor3;
    private DcMotorEx motor4;
    private double MOVE_POWER = 0.5;

    public MoveSubsystem(HardwareMap hwMap) {
        motor1 = hwMap.get(DcMotorEx.class, "rf");
        motor2 = hwMap.get(DcMotorEx.class, "rr");
        motor3 = hwMap.get(DcMotorEx.class, "lf");
        motor4 = hwMap.get(DcMotorEx.class, "lr");

        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        motor3.setDirection(DcMotorSimple.Direction.REVERSE);
        motor4.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void changePower(boolean isMove) {
        MOVE_POWER = isMove ? 0.5 :  0;
    }

    @Override
    public void periodic() {
        motor1.setPower(MOVE_POWER);
        motor2.setPower(MOVE_POWER);
        motor3.setPower(MOVE_POWER);
        motor4.setPower(MOVE_POWER);
    }
}
