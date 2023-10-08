package frc.robot;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.autos.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Controllers */
    private final Joystick driver = new Joystick(0);
    private final Joystick operator = new Joystick(1);
    /* Drive Controls */
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;
    /* Operator Controls */
    private final int wristAxis = XboxController.Axis.kLeftY.value;
    private final int winchAxis = XboxController.Axis.kRightY.value;
    private final int leftTrigger = XboxController.Axis.kLeftTrigger.value;
    private final int rightTrigger = XboxController.Axis.kRightTrigger.value;
    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kLeftStick.value);
    private final JoystickButton right90 = new JoystickButton(driver, XboxController.Button.kRightBumper.value);
    private final JoystickButton left90 = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);
    private final JoystickButton xLock = new JoystickButton(driver, XboxController.Button.kX.value);
    private final JoystickButton slowDrive = new JoystickButton(driver, XboxController.Button.kB.value);
    // private final JoystickButton limeOnOff = new JoystickButton(driver, XboxController.Button.kX.value);
    /* Operator Buttons */
    private final JoystickButton winchOverride = new JoystickButton(operator, XboxController.Button.kStart.value);
    private final JoystickButton armZero = new JoystickButton(operator, XboxController.Button.kY.value);
    private final JoystickButton armMiddle = new JoystickButton(operator, XboxController.Button.kX.value);
    private final JoystickButton armTop = new JoystickButton(operator, XboxController.Button.kB.value);
    // private final JoystickButton armBottom = new JoystickButton(operator, XboxController.Button.kA.value);
    private final JoystickButton armHuman = new JoystickButton(operator, XboxController.Button.kA.value);
    private final JoystickButton actuate = new JoystickButton(operator, XboxController.Button.kRightBumper.value);
    private final JoystickButton comp = new JoystickButton(operator, XboxController.Button.kLeftBumper.value);

    /* Subsystems */
    private final Swerve s_Swerve = new Swerve();
    private final Pneumatics pneumatics = new Pneumatics();
    //private final Limelight limelight = new Limelight();
    private final Elevator elevator = new Elevator();

    /* Path Planner */
    PathPlannerTrajectory BalanceFinal = PathPlanner.loadPath("BalanceFinal", new PathConstraints(4, 3));
    PathPlannerTrajectory OutOfTheWay = PathPlanner.loadPath("OutOfTheWay", new PathConstraints(4, 3));
    PathPlannerTrajectory PushCone = PathPlanner.loadPath("PushCone", new PathConstraints(4, 3));
    PathPlannerTrajectory CableBalance = PathPlanner.loadPath("CableBalance", new PathConstraints(4, 3));
    PathPlannerTrajectory Test = PathPlanner.loadPath("Test", new PathConstraints(2, 3));
    SendableChooser<Command> chooser = new SendableChooser<>();
    SwerveAutoBuilder autoBuilder = new SwerveAutoBuilder(
        s_Swerve::getPose, 
        s_Swerve::resetOdometry,
        Constants.Swerve.swerveKinematics,
        new PIDConstants(Constants.AutoConstants.kPXController, 0,0), //original p = 5, 1st attempt: p = 5, d = 0.5, 2nd attempt: p= 5, d = 0.5, 3rd attempt: p = 5, d = 3 this caused the wheels to shutter
        new PIDConstants(Constants.AutoConstants.kPYController, 0, 0),
        s_Swerve::setModuleStates,
        Constants.AutoConstants.eventMap,
        true,
        s_Swerve);
        Command BalanceCommand = autoBuilder.fullAuto(BalanceFinal).andThen(new AutoBalance(s_Swerve));
        Command OutOfTheWayCommand = autoBuilder.fullAuto(OutOfTheWay);
        Command PushConeCommand = autoBuilder.fullAuto(PushCone);
        Command CableBalanceCommand = autoBuilder.fullAuto(CableBalance).andThen(new AutoBalance(s_Swerve));
        Command TestCommand = new AutoScore(pneumatics, elevator, -29000, -150, 460).andThen(new AutoScore(pneumatics, elevator, -200, 0, 0)).andThen(autoBuilder.fullAuto(Test)).andThen(new AutoBalance(s_Swerve)).andThen(new InstantCommand(() -> s_Swerve.ReverseGyro()));
    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        chooser.setDefaultOption("Balance", BalanceCommand);
        chooser.addOption("OutOfTheWay", OutOfTheWayCommand);
        chooser.addOption("PushCone", PushConeCommand);
        chooser.addOption("CableBalance", CableBalanceCommand);
        chooser.addOption("Test", TestCommand);
        s_Swerve.setDefaultCommand(
                new TeleopSwerve(
                        s_Swerve,
                        () -> -driver.getRawAxis(translationAxis)*.5,
                        () -> -driver.getRawAxis(strafeAxis)*.5,
                        () -> -driver.getRawAxis(rotationAxis)*.5,
                        () -> robotCentric.getAsBoolean()));

        elevator.setDefaultCommand(
                new TeleopElevator(
                        elevator,
                        () -> operator.getRawAxis(leftTrigger) - operator.getRawAxis(rightTrigger),
                        () -> operator.getRawAxis(wristAxis),
                        () -> -operator.getRawAxis(winchAxis),
                        () -> winchOverride.getAsBoolean()));

        pneumatics.setDefaultCommand(
                new TeleopPneumatics(
                        pneumatics));

        // limelight.setDefaultCommand(
        //         new TeleopLimelight(
        //                 limelight,
        //                 () -> limeOnOff.getAsBoolean()));
        // Configure the button bindings
        configureButtonBindings();
        SmartDashboard.putData(chooser);
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
     * it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        /* Driver Buttons */
        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));
        left90.onTrue(new InstantCommand(() -> s_Swerve.rotateLeft()));
        right90.onTrue(new InstantCommand(() -> s_Swerve.rotateRight()));
        xLock.onTrue(new InstantCommand(() -> s_Swerve.setX()));
        slowDrive.onTrue(new InstantCommand(() -> s_Swerve.slowDrive()));
        actuate.onTrue(new InstantCommand(() -> pneumatics.actuate()));
        comp.onTrue(new InstantCommand(() -> pneumatics.comp()));
        armZero.debounce(0.1).onTrue(new MacroElevator(elevator, -200, 0, 0));
        armMiddle.debounce(0.1).onTrue(new MacroElevator(elevator, -8000, -140, 480));
        armTop.debounce(0.1).onTrue(new MacroElevator(elevator, -29000, -150, 460));
        // armBottom.debounce(0.1).onTrue(new MacroElevator(elevator, -200, -525,300));
        armHuman.debounce(0.1).onTrue(new MacroElevator(elevator, -200, -75, 300));
        //bruh
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {

        // return new AutoBalance(s_Swerve);
       
            return chooser.getSelected();
    }
}
