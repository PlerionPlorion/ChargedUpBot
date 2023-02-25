// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {
  TalonSRX elevator;
  double encoder;
  CANSparkMax wrist;
  CANSparkMax winch;
  DutyCycleEncoder neoEncoder;
  RelativeEncoder winchEncoder;
  /** Creates a new Elevator. */
  public Elevator() {
    elevator = new TalonSRX(55);
    wrist = new CANSparkMax(13, MotorType.kBrushless);
    winch = new CANSparkMax(14, MotorType.kBrushless);
    neoEncoder = new DutyCycleEncoder(9);
    winchEncoder = winch.getEncoder();
  }
  /** Sets talonSRX to requested speed */
  public void encodedDrive(double speed) {
    elevator.set(ControlMode.PercentOutput, speed);
    
  }
  /** Sets NEO wrist to requested speed */
  public void wristDrive(double speed) {
    if(speed > 0.2) {
      speed = 0.2;
    }
    if(speed < -0.2) {
      speed = -0.2;
    }
    if(neoEncoder.getAbsolutePosition() < 0.1) {
      if(speed < 0) {
        speed = 0;
      }
    } if(neoEncoder.getAbsolutePosition() > 0.4) {
      if(speed > 0) {
        speed = 0;
      }
    }
    wrist.set(speed);


    SmartDashboard.putNumber("wristEncoder", neoEncoder.getAbsolutePosition());
  }
  public void winchDrive(double speed) {
    if(winchEncoder.getPosition() > -5) {
      if(speed > 0) {
        speed = 0;
      }
    } if(winchEncoder.getPosition() < -450) {
      if(speed < 0) {
        speed = 0;
      }
    }
    winch.set(speed);
    System.out.println(winchEncoder.getPosition());
    SmartDashboard.putNumber("winchEncoder", winchEncoder.getPosition());
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    encoder = elevator.getSelectedSensorPosition();
    SmartDashboard.putNumber("elevEncoder", encoder);
  }
}
