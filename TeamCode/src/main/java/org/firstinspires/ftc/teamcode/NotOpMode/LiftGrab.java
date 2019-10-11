package org.firstinspires.ftc.teamcode.NotOpMode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class LiftGrab {

    private Servo rotateArm;
    private Servo gripper;
    private DcMotor lift;

    private ElapsedTime cycleTime = new ElapsedTime();

    public void init(HardwareMap hwMap) {
        rotateArm = hwMap.get(Servo.class, "lift_arm");
        gripper = hwMap.get(Servo.class, "lift_gripper");
        lift = hwMap.get(DcMotor.class, "lift");

        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //moveGrabber("in", "initial");
        cycleTime.reset();
    }

    public void moveGrabber(String armPos, String gripperPos) {
        final double inValue = 1;
        final double outValue = 0;

        final double openValue = 0.58;
        final double closedValue = 0.38;
        final double initialGripperValue = 0.17;

        switch (armPos) {
            case "in": rotateArm.setPosition(inValue);
            break;
            case "out":rotateArm.setPosition(outValue);
        }
        switch (gripperPos) {
            case "open":    gripper.setPosition(openValue);
            break;
            case "closed":  gripper.setPosition(closedValue);
            break;
            case "initial": gripper.setPosition(initialGripperValue);
            break;
            default: break;
        }
    }

    public void setLiftPower(double liftPower) {
        lift.setPower(liftPower);
    }
}