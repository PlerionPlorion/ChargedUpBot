// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autos;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Swerve;
public class AutoBalance extends CommandBase {

  private final PIDController m_XController;
  private Swerve s_Swerve;
  /** Creates a new SwerveWithPIDY. */

  public AutoBalance(Swerve Swerve) 
  {

    
    m_XController = new PIDController(0.0075, 0, 0);

    m_XController.setTolerance(0.05);



    s_Swerve = Swerve;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(s_Swerve);
  

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
    System.out.println("getting scheduled");

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
      double x_SetPoint = -1.6;
    double x_Speed =  m_XController.calculate(s_Swerve.getRoll(), x_SetPoint);
    

    s_Swerve.drive(new Translation2d(-x_Speed*1.6, 0).times(Constants.Swerve.maxSpeed), 0.0, false, true);
    System.out.println("Balance running");


    
    

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    s_Swerve.setX();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(m_XController.atSetpoint() == true) {
           return true;
    }
    return false;
  }
}