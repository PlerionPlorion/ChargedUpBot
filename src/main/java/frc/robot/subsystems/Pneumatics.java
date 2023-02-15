// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pneumatics extends SubsystemBase {
  Joystick driver;
  Compressor compressor;
  Solenoid solenoid;
  Solenoid solenoid1;

  /** Creates a new Pneumatics. */
  public Pneumatics() {
    compressor = new Compressor(PneumaticsModuleType.CTREPCM);
    solenoid = new Solenoid(PneumaticsModuleType.CTREPCM, 0);
    solenoid1 = new Solenoid(PneumaticsModuleType.CTREPCM, 1);
  }

  public void actuate(int solenoidSelect) {
    if (solenoidSelect == 0) {
      if (solenoid.get() == true) {
        solenoid.set(false);
      } else {
        solenoid.set(true);
      }
    } else if(solenoidSelect == 1){
      if (solenoid1.get() == true) {
        solenoid1.set(false);
      } else {
        solenoid1.set(true);
      }
    }
  }

  public void comp() {
    if (compressor.isEnabled() == false) {
      compressor.enableDigital();
    } else {
      compressor.disable();
    }

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

}
