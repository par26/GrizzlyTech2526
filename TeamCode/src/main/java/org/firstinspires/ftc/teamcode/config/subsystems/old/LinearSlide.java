package org.firstinspires.ftc.teamcode.config.subsystems.old;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class LinearSlide {
    DcMotor slideMotor;
    public void init(HardwareMap hardwareMap) {
        slideMotor = hardwareMap.get(DcMotor.class, "slideMotor");
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void extendSlide(int position) {
        // slideMotor.setDirection(DcMotor.Direction.REVERSE);
        slideMotor.setTargetPosition(position);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(1);
    }

    public void stop() {
        slideMotor.setPower(0);
    }
}
