package org.firstinspires.ftc.teamcode.NotOpMode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class FritsBot {

    public XDrive drivetrain = new XDrive();
    //public servoTester servoTest = new servoTester();
    public SideGrabber sideGrabber = new SideGrabber();
    public LiftGrab liftGrab = new LiftGrab();
    public FoundationServo foundationServo = new FoundationServo();
    private Intake fritsIntake = new Intake();
    private BNO055IMU imu;

    private ElapsedTime rotateTimer = new ElapsedTime();
    // objects for driveHoldAngle
    private boolean wasRotating;
    private double rotationSetpoint;
    private double holdAngleOffset;

    private double prevAngle;
    private double fullRotationOffset;

    LinearOpMode opmode;

    public void init(HardwareMap hwMap, LinearOpMode opmode) {
        drivetrain.init(hwMap, opmode);
        //servoTest.init(hwMap);
        sideGrabber.init(hwMap);
        liftGrab.init(hwMap);
        foundationServo.init(hwMap);
        fritsIntake.init(hwMap);
        imu = hwMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        imu.initialize(parameters);

        wasRotating = false;
        rotationSetpoint = getHeadingRadians();

        prevAngle = 0;
        fullRotationOffset = 0;
        holdAngleOffset = 0;

        this.opmode = opmode;
    }

    public void driveSimple(double forward, double strafe, double rotate) {
        drivetrain.drive(forward, strafe, rotate);
    }

//    public void driveFieldCentric(double forward, double strafe, double rotate) {
//        Polar driveP = Polar.fromXYCoord(strafe, forward);
//        double heading = getHeadingRadians();
//
//        driveP.subtractAngle(heading);
//        drivetrain.drive(driveP.getY(), driveP.getX(), rotate);
//    }

    public void driveHoldAngle(double forward, double strafe, double rotate) {
        Polar driveP = Polar.fromXYCoord(strafe, forward);
        double currentHeading = getHeadingRadians();

        double rotateNew;
        final double adjustmentSpeed = 0.4;

        if (rotate == 0 && !wasRotating) {
            rotateNew = adjustmentSpeed * (rotationSetpoint - currentHeading);
        } else if (wasRotating && rotate == 0) {
            wasRotating = false;
            rotationSetpoint = currentHeading;
            rotateNew = 0;
        } else if (!wasRotating) {
            wasRotating = true;
            rotateNew = rotate;
        } else {
            rotateNew = rotate;
        }

        if (Math.abs(rotateNew) < 0.02) {
            rotateNew = 0;
        }

        driveP.subtractAngle(currentHeading - holdAngleOffset);
        drivetrain.drive(driveP.getY(), driveP.getX(), rotateNew);
    }

    public void driveToPoint(double speed, double x, double y, double timeoutS) {
        Polar autonDrive = Polar.fromXYCoord(x, y);
        // Shift distance vector 45 degrees clockwise so we can get diagonal distances from x and y
        autonDrive.subtractAngle(0.7853981633973); // PI/4

        double diagonalRightCM = autonDrive.getX();
        double diagonalLeftCM = autonDrive.getY();

        drivetrain.encoderDrive(speed, diagonalLeftCM, diagonalRightCM, timeoutS);
    }

    // rotate relative to rotation setpoint
    public void rotateAbsolute(double speed, int rotation, double timeoutS) {
        double currentHeading = getHeadingRadians();
        double rotateNew;

        double kP = 1.5;
        double kI = 0;
        double kD = 0;
        long dt = 50;
        double integral = 0;
        double derivative = 0;
        double prevError = 0;

        rotationSetpoint = Math.toRadians(rotation) - holdAngleOffset;

        double error = rotationSetpoint - currentHeading;
        rotateTimer.reset();

        while (rotateTimer.seconds() < timeoutS && error > 0.035 && opmode.opModeIsActive()) {
            error = rotationSetpoint - currentHeading;
            integral = integral + error * dt;
            derivative = (error - prevError) / dt;

            double rotateSpeed = Range.clip(kP * error + kI * integral + kD * derivative, -1, 1);
            drivetrain.drive(0, 0, rotateSpeed);
            prevError = error;
            opmode.sleep(dt);
        }
    }

    public void resetDriveAngle() {
        holdAngleOffset = getHeadingRadians();
    }

    public void setIntakePower(double power) {
        fritsIntake.setIntakePower(power);
    }

    private double getHeadingRadians() {
        Orientation angles;

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        if(angles.firstAngle < 0 && prevAngle > 2.8) {
            fullRotationOffset += 2 * Math.PI;
        }
        else if(angles.firstAngle > 0 && prevAngle < -2.8) {
            fullRotationOffset -= 2 * Math.PI;
        }

        prevAngle = angles.firstAngle;
        return -angles.firstAngle - fullRotationOffset;
    }

    public void reportHeadingRadians(Telemetry telemetry) {
        telemetry.addData("heading:", getHeadingRadians());
    }
}