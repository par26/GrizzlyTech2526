package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.config.SorterServoV3;
import org.firstinspires.ftc.teamcode.config.subsystems.SorterServoV2;
import org.firstinspires.ftc.teamcode.config.subsystems.sorter.SorterServo;

@TeleOp(group="test")
public class TestManualSorter extends CommandOpMode {

    private GamepadEx m_driver;
//    private SorterServoV2 m_servo;
    private SorterServoV3 m_servo;

    @Override
    public void initialize() {
        m_driver = new GamepadEx(gamepad1);
//        m_servo = new SorterServoV3(hardwareMap, telemetry);
        m_servo = new SorterServoV3(hardwareMap);

        register(m_servo);

//        m_driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
//                .whenPressed(new InstantCommand(() -> m_servo.rotateNode(), m_servo));


    }
}
