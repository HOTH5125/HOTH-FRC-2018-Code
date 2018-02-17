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
	ADXRS450_Gyro gyro;
	double error = 0.0;
	double gyroP = 1/90; //go full speed per 90 deg
	
	@Override
	public void robotInit() {		
		leftStick = new Joystick(0);
    	rightStick = new Joystick(1);
    	gyro = new ADXRS450_Gyro();
		gyro.calibrate();
		gyro.reset();
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
		gyro.reset();
		while( isAutonomous() && isEnabled()) {
			System.out.println(gyro.getAngle());
			error = 90;
			double speed = 1.0;	
			System.out.println(gyro.getAngle());
			while (Math.abs(error) != 0) {
				System.out.println(gyro.getAngle());
				speed = error / 90;
				if(speed >1) {
					speed = .5;
				}
				if(speed < -1) {
					speed = -.5;
				}
				if (error < 0) {
					leftSide.set(-speed);
					rightSide.set(-speed);					
				}
				if(error > 0) {
					rightSide.set(-speed);
					leftSide.set(-speed);
				}
				error = gyro.getAngle()-90;
			}
			System.out.println(gyro.getAngle());
			leftSide.set(0.0);
			rightSide.set(0.0);
		}
	}
}
