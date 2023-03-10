// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;

public class MacroElevator extends CommandBase {
  Elevator elevator;
  double elevatorSup;
  double winchSup;
  double wristSup;
  /** Creates a new MiddleElevator. */
  public MacroElevator(Elevator elevator, double elevatorSup, double winchSup, double wristSup) {
    this.elevator = elevator;
    this.elevatorSup = elevatorSup;
    this.winchSup = winchSup;
    this.wristSup = wristSup;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(elevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    elevator.macroEnd = false;
    elevator.winchDone = false;
    elevator.wristDone = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    elevator.macro(wristSup, winchSup, elevatorSup);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    elevator.macroEnd = false;
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return elevator.macroEnd;
  }
}
