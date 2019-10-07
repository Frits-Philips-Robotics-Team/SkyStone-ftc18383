package org.firstinspires.ftc.teamcode.NotOpMode;


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class SideGrabber {

    // TODO: find out which values the servos need for these positions
    private Servo arm;
    private Servo gripper;
    private ElapsedTime cycleTime = new ElapsedTime();

    public void init(HardwareMap hwMap) {
        arm = hwMap.get(Servo.class, "side_arm");
        gripper = hwMap.get(Servo.class, "side_gripper");

        cycleTime.reset();
    }

    public void moveGrabber(String armPos, String gripperPos) {
        final double upValue = 0.17;
        final double downValue = 0.85;
        final double openValue = 0.58;
        final double closedValue = 0.38;
        final double initialArmValue = 0.44;

        switch (armPos) {
            case "up":      arm.setPosition(upValue);
            break;
            case "down":    arm.setPosition(downValue);
            break;
        }
        switch (gripperPos) {
            case "open":    gripper.setPosition(openValue);
            break;
            case "closed":  gripper.setPosition(closedValue);
            break;
            case "initial": gripper.setPosition(initialArmValue);
            break;
        }
    }
}
