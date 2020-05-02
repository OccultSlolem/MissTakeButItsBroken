/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.frc7528.frc2020.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import java.io.File;
import java.text.SimpleDateFormat;

import static com.frc7528.frc2020.robot.common.RobotMap.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
    @Override
    public void robotInit() {
        //Output firmware/code deploy stats
        Shuffleboard.getTab("DEBUG").add("Left Aft Firmware Version",m_leftAft.getFirmwareVersion());
        Shuffleboard.getTab("DEBUG").add("Right Aft Firmware Version",m_rightAft.getFirmwareVersion());
        Shuffleboard.getTab("DEBUG").add("Left Front Firmware Version",m_leftFront.getFirmwareVersion());
        Shuffleboard.getTab("DEBUG").add("Right Front Firmware Version",m_rightFront.getFirmwareVersion());
        File file = new File(Robot.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        Shuffleboard.getTab("DEBUG").add("Last code deploy",sdf.format(file.lastModified()));

        //Config motors to factory default
        m_leftAft.configFactoryDefault();
        m_rightAft.configFactoryDefault();
        m_leftFront.configFactoryDefault();
        m_rightFront.configFactoryDefault();

        //Configure followers and DifferentialDrive
        m_drive = new DifferentialDrive(m_leftFront,m_rightFront);
        m_leftAft.follow(m_leftFront);
        m_rightAft.follow(m_rightFront);

        Shuffleboard.getTab("DRIVETRAIN").add(m_drive); //Add drivetrain to Shuffleboard

        CameraServer.getInstance().startAutomaticCapture(); //Start Camera Capture
    }

    @Override
    public void robotPeriodic() {
        //Right trigger goes forward
        //Left trigger goes backwards

        if(m_gamepad.getTriggerAxis(GenericHID.Hand.kRight) > 0) { //If right trigger pressed down
            m_drive.arcadeDrive(m_gamepad.getX(GenericHID.Hand.kLeft),m_gamepad.getTriggerAxis(GenericHID.Hand.kRight));
        } else if(m_gamepad.getTriggerAxis(GenericHID.Hand.kLeft) > 0) { //If left trigger pressed down
            m_drive.arcadeDrive(-m_gamepad.getTriggerAxis(GenericHID.Hand.kLeft),m_gamepad.getX(GenericHID.Hand.kLeft));
        } else { //If neither trigger pressed down
            m_drive.arcadeDrive(0,m_gamepad.getX(GenericHID.Hand.kLeft));
        }
    }
}
