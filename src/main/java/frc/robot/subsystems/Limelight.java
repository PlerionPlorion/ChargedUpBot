// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {
  /** Creates a new Limelight. */
  NetworkTableEntry tvEnt;
  NetworkTableEntry txEnt;
  NetworkTableEntry tyEnt;
  NetworkTableEntry taEnt;
  NetworkTable table;
  double ta;
  public static Boolean TargetDetected;

  public Limelight() {
    table = NetworkTableInstance.getDefault().getTable("limelight");
  }
  //Turns limelight on/off in a cycle
  public void limePower() {

    if (table.getEntry("ledMode").getInteger(0) == 1) {
      table.getEntry("ledMode").setNumber(3);
    } else {
      table.getEntry("ledMode").setNumber(1);
    }

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    tvEnt = table.getEntry("tv");
    txEnt = table.getEntry("tx");
    tyEnt = table.getEntry("ty");
    taEnt = table.getEntry("ta");
    ta = taEnt.getDouble(0);
    TargetDetected = ta > 0;
  }
}
