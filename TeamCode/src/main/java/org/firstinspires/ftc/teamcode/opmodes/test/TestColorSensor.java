package org.firstinspires.ftc.teamcode.opmodes.test;

import android.graphics.Color;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.seattlesolvers.solverslib.command.CommandOpMode;

@TeleOp(group="test")
public class TestColorSensor extends OpMode {

    private RevColorSensorV3 m_color;

    @Override
    public void init() {
        m_color = hardwareMap.get(RevColorSensorV3.class, "sensor1");
        m_color.enableLed(true );
    }

    @Override
    public void loop() {
        getHue();
    }

    public void getHue() {
        NormalizedRGBA n = m_color.getNormalizedColors();
        float[] hsv = new float[3];
        int argb = n.toColor();
        Color.colorToHSV(argb, hsv);

        float hue = hsv[0];
        telemetry.addData("Hue", hue);
    }
}
