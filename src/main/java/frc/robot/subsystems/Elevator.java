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
  TalonSRX elevatorSRX;
  double encoder;
  CANSparkMax wrist;
  CANSparkMax winch;
  Encoder wristEncoder;
  RelativeEncoder winchEncoder;
  double encoderDouble;
  public boolean macroEnd;
  public boolean winchDone;
  public boolean wristDone;

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
    if (winchEncoder.getPosition() < -540) {
      speed = 0;
    }
    if (encoderDouble > -200) {
      encoderDouble = -200;
    }
    if (encoderDouble < -30000) {
      encoderDouble = -30000;
    }

      encoderDouble += 500 * speed;
      
    elevatorSRX.set(ControlMode.Position, encoderDouble);
    SmartDashboard.putNumber("encoderDouble", encoderDouble);
  }

  /** Sets NEO wrist to requested speed */
  public void wristDrive(double speed, boolean override) {
    if (speed > 1) {
      speed = 1;
    }
    if (speed < -1) {
      speed = -1;
    }
    if (override == true) {
      wrist.set(speed/3.33);
    } else {
        if (wristEncoder.getDistance() > 800) {
          System.out.println("dhsfjsd");
          if (speed < 0) {
            speed = 0;
          }
        }
      
      if (wristEncoder.getDistance() < 20) {
        if (speed > 0) {
          speed = 0;
        }
      }
      wrist.set(speed/3.33);

    }
    SmartDashboard.putNumber("wristEncoder", wristEncoder.getDistance());
  }

  public void winchDrive(double speed, boolean override) {
    if (override == true) {
      winch.set(speed);
    } else {
      if (winchEncoder.getPosition() > -5) {
        if (speed > 0) {
          speed = 0;
        }
      }
      if (encoder > -150) {
        if (winchEncoder.getPosition() < -275) {
          if (speed < 0) {
            speed = 0;
          }
        }
      } else {
        if (winchEncoder.getPosition() < -265) {
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

  public void macro(double wristLength, double winchLength, double elevatorLength){
    if(wristDone == true && winchEncoder.getPosition() < winchLength - 15) {
      winch.set(1);
   } if(wristDone == true && winchEncoder.getPosition() > winchLength + 15) {
     winch.set(-1);
   } else if(winchEncoder.getPosition() > winchLength - 10 && winchEncoder.getPosition() < winchLength + 10) {
     winch.set(0);
     winchDone = true;
   }
   if(wristEncoder.getDistance() > wristLength + 20) {
     wrist.set(0.2);
   } if (wristEncoder.getDistance() < wristLength - 20){
     wrist.set(-0.2);
   } else if(wristEncoder.getDistance() > wristLength - 10 && wristEncoder.getDistance() < wristLength + 10) {
     wrist.set(0);
     wristDone = true;
   }
     encoderDouble = -1000;
     if(wristDone == true){
     encoderDouble = elevatorLength;
     }
     elevatorSRX.set(ControlMode.Position, encoderDouble);
   if(winchDone == true && wristDone == true && elevatorSRX.getSelectedSensorPosition() < elevatorLength + 700 && elevatorSRX.getSelectedSensorPosition() > elevatorLength - 700) {
     macroEnd = true;
   }
   SmartDashboard.putBoolean("done", macroEnd);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    encoder = elevatorSRX.getSelectedSensorPosition();
    SmartDashboard.putNumber("elevEncoder", encoder);
  }
}
