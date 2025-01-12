/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.NotOpMode.FritsBot;


@TeleOp(name="Field_relative_TeleOp", group="Iterative Opmode")
//@Disabled
public class FieldRelativeTeleOp extends OpMode
{
    private FritsBot robot = new FritsBot();
    private ElapsedTime runtime = new ElapsedTime();

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        robot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
    }

    // Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    @Override
    public void init_loop() {
    }

    // Code to run ONCE when the driver hits PLAY
    @Override
    public void start() {
        runtime.reset();
    }

    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
        double speed = 0.3 + Range.scale(gamepad1.right_trigger, 0, 1, 0, 0.7);
        robot.driveHoldAngle(speed * -gamepad1.left_stick_y, speed * gamepad1.left_stick_x, speed *  gamepad1.right_stick_x);

        if(gamepad1.right_bumper) {
            robot.resetDriveAngle();
        }

        robot.liftGrab.setLiftPower(-gamepad2.left_stick_y);

        robot.liftGrab.slideArm(gamepad2.right_stick_x);

        if(gamepad1.a) {
            robot.drivetrain.setSpeed(0.75);
        }
        if(gamepad1.y) {
            robot.drivetrain.setSpeed(1);
        }

        if(gamepad2.b) {
            robot.intake.setPower(1);
        }
        else if(gamepad2.x) {
            robot.intake.setPower(-1);
        }
        else {
            robot.intake.setPower(0);
        }

        if(gamepad1.dpad_left) {
            robot.foundationServo.moveUp(false);
        }
        else if(gamepad1.dpad_right
        ) {
            robot.foundationServo.moveUp(true);
        }

        if(gamepad2.dpad_left) {
            robot.liftGrab.moveGrabber("", "open");
        }
        else if(gamepad2.dpad_right) {
            robot.liftGrab.moveGrabber("", "closed");
        }

        if(gamepad2.dpad_up) {
            robot.sideGrabber.moveRightGrabber("upBlock", "");
        }
        else if (gamepad2.dpad_down) {
            robot.sideGrabber.moveRightGrabber("down", "");
        }
        else if (gamepad2.right_bumper) {
            robot.sideGrabber.moveRightGrabber("upEmpty", "initial");
        }

        if (gamepad2.a) {
            robot.sideGrabber.moveRightGrabber("", "closed");
        }
        else if (gamepad2.y) {
            robot.sideGrabber.moveRightGrabber("", "open");
        }

        // Show telemetry
        //telemetry.addData("Status", "Run Time: " + runtime.toString());
        robot.drivetrain.reportSpeeds(telemetry);
        robot.drivetrain.reportEncoders(telemetry);
    }

    // Code to run ONCE after the driver hits STOP
    @Override
    public void stop() {
    }

}
