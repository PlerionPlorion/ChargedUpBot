// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {
  public TalonSRX elevatorSRX;
  double encoder;
  public CANSparkMax wrist;
  public CANSparkMax winch;
  public Encoder wristEncoder;
  public RelativeEncoder winchEncoder;
  public double encoderDouble;
  /** Creates a new Elevator. */
  public Elevator() {
    elevatorSRX = new TalonSRX(55);
    wrist = new CANSparkMax(13, MotorType.kBrushless);
    winch = new CANSparkMax(14, MotorType.kBrushless);
    wristEncoder = new Encoder(9, 8, false, Encoder.EncodingType.k2X);
    winchEncoder = winch.getEncoder();
  }
  
  /** Sets talonSRX to requested speed */
  public void encodedDrive(double speed) {
    if (winchEncoder.getPosition() < -490) {
      if (speed > 0) {
        speed = 0;
      }
      if (speed < 0) {
        speed = 0;
      }
    }
    if (encoderDouble > -200) {
      encoderDouble = -200;
    }
    if (encoderDouble < -30000) {
      encoderDouble = -30000;
    }

      encoderDouble += 300 * speed;
      
    elevatorSRX.set(ControlMode.Position, encoderDouble);
    SmartDashboard.putNumber("encoderDouble", encoderDouble);
  }

  /** Sets NEO wrist to requested speed */
  public void wristDrive(double speed, boolean override) {
    if (speed > 0.2) {
      speed = 0.2;
    }
    if (speed < -0.2) {
      speed = -0.2;
    }
    if (override == true) {
      wrist.set(speed);
    } else {
      if (encoder > -300) {
        if (wristEncoder.getDistance() > 330) {
          if (speed < 0) {
            speed = 0;
          }
        }
      } else {
        if (wristEncoder.getDistance() > 400) {
          System.out.println("dhsfjsd");
          if (speed < 0) {
            speed = 0;
          }
        }
      }
      if (wristEncoder.getDistance() < 20) {
        if (speed > 0) {
          speed = 0;
        }
      }
      wrist.set(speed);

    }
    SmartDashboard.putNumber("wristEncoder", wristEncoder.getDistance());
  }

  public void winchDrive(double speed, boolean override) {
    if (override == true) {
      winch.set(speed);
      System.out.println("hsdfjshdiufh");
    } else {
      if (winchEncoder.getPosition() > -5) {
        if (speed > 0) {
          speed = 0;
        }
      }
      if (encoder > -300) {
        if (winchEncoder.getPosition() < -550) {
          if (speed < 0) {
            speed = 0;
          }
        }
      } else {
        if (winchEncoder.getPosition() < -480) {
          if (speed < 0) {
            speed = 0;
          }
        }
      }
      winch.set(speed);
      System.out.println(winchEncoder.getPosition());
      SmartDashboard.putNumber("winchEncoder", winchEncoder.getPosition());
    }
  }
  public boolean zeroArm(boolean end) {
    if(winchEncoder.getPosition() < -5) {
       winch.set(1);
    } else {
      winch.set(0);
    }
    if(wristEncoder.getDistance() > 20) {
      wrist.set(0.2);
    } else {
      wrist.set(0);
    }
    if(elevatorSRX.getSelectedSensorPosition() < -200) {
      encoderDouble = -200;
      elevatorSRX.set(ControlMode.Position, encoderDouble);
    }
    if(winchEncoder.getPosition() > -5 && wristEncoder.getDistance() < 20 && elevatorSRX.getSelectedSensorPosition() > -210) {
      end = true;
    }
    return end;
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    encoder = elevatorSRX.getSelectedSensorPosition();
    SmartDashboard.putNumber("elevEncoder", encoder);
  }
}
