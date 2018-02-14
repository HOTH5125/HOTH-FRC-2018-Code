package org.usfirst.frc.team5125.robot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;


public class Robot extends IterativeRobot {
	
	VictorSP blMotor = new VictorSP (0);
	VictorSP tlMotor = new VictorSP (1);
	VictorSP trMotor = new VictorSP (2);
	VictorSP brMotor = new VictorSP (3);
	SpeedControllerGroup leftSide = new SpeedControllerGroup (tlMotor,blMotor);
	SpeedControllerGroup rightSide = new SpeedControllerGroup (trMotor,brMotor);
	DifferentialDrive robot = new DifferentialDrive(leftSide,rightSide);	
	Joystick leftStick, rightStick;

	@Override
	public void robotInit() {		
		leftStick = new Joystick(0);
    	rightStick = new Joystick(1);
	}
	
	@Override
	public void teleopPeriodic() {
		robot.setSafetyEnabled(true);
		while (isOperatorControl() && isEnabled()) {
			robot.tankDrive(leftStick.getY(), rightStick.getY());
		Timer.delay(.001);
		}
	}
	
	@Override
	public void autonomousInit() {
		while( isAutonomous() && isEnabled()) {
			
		}
	}
}