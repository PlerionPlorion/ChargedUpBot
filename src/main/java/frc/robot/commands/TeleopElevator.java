// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Elevator;

public class TeleopElevator extends CommandBase {
  private DoubleSupplier elevSup;
  private DoubleSupplier wristSup;
  private Elevator elevator;
  /** Creates a new TeleopElevator. */
  public TeleopElevator(Elevator elevator, DoubleSupplier elevSup, DoubleSupplier wristSup) {
    this.elevator = elevator;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(elevator);
    this.elevSup = elevSup;
    this.wristSup = wristSup;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double elevVal = MathUtil.applyDeadband(elevSup.getAsDouble(), Constants.stickDeadband);
    double wristVal = MathUtil.applyDeadband(wristSup.getAsDouble(), Constants.stickDeadband);
    elevator.encodedDrive(elevVal);
    elevator.wristDrive(wristVal);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
