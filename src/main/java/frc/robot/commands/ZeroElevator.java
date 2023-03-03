// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;

public class ZeroElevator extends CommandBase {
  Elevator elevator;
  boolean end;
  /** Creates a new ZeroElevator. */
  public ZeroElevator(Elevator elevator) {
    this.elevator = elevator;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(elevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    end = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(elevator.winchEncoder.getPosition() < -5) {
      elevator.winch.set(1);
    } else {
      elevator.winch.set(0);
    }
    if(elevator.wristEncoder.getDistance() > 20) {
      elevator.wrist.set(0.2);
    } else {
      elevator.wrist.set(0);
    }
    if(elevator.elevatorSRX.getSelectedSensorPosition() < -200) {
      elevator.encoderDouble = -200;
      elevator.elevatorSRX.set(ControlMode.Position, elevator.encoderDouble);
    }
    if(elevator.winchEncoder.getPosition() > -5 && elevator.wristEncoder.getDistance() < 20 && elevator.elevatorSRX.getSelectedSensorPosition() > -210) {
      end = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return end;
  }
}
