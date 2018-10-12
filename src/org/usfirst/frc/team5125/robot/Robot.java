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

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;


public class Robot extends IterativeRobot  {
	VictorSP blMotor = new VictorSP (1);
	VictorSP tlMotor = new VictorSP (0);
	VictorSP trMotor = new VictorSP (3);
	VictorSP brMotor = new VictorSP (2);
	VictorSP leftClaw = new VictorSP (4);
	VictorSP rightClaw = new VictorSP (5);
	VictorSP arm1 = new VictorSP (6);
//	VictorSP arm2 = new VictorSP(7);
	Timer autoTimer;
	double time = 0.05;
	
	

	SpeedControllerGroup leftSide = new SpeedControllerGroup (tlMotor,blMotor);
	SpeedControllerGroup rightSide = new SpeedControllerGroup (trMotor,brMotor);
	//SpeedControllerGroup arm = new SpeedControllerGroup (arm1, arm2);
	DifferentialDrive robot = new DifferentialDrive(leftSide,rightSide);	
	
	Joystick leftStick, rightStick, logController;
	JoystickButton butX, butY, butA, butB;
	ADXRS450_Gyro gyro;
	
	String gameData;
	
	double gyroP = 1/90; //go full speed per 90 deg
	Encoder enc;
	Encoder encL;
	DigitalInput limitSwitch;

	public void moveForward (double length) {
		double dist = 0.0;
		if ( dist < length ) {
			dist = (enc.getDistance() * (6*Math.PI)); //possibility is to multiply everything by 2
			leftSide.set(-.25);
			rightSide.set(.25);
		}
	}
	public void PIDRotate (double angle) {
		double error = -angle;
		double speed = 1.0;	
		while (Math.abs(error) != 0) {
			speed = error / angle;
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
				rightSide.set(speed); //Used to be "-speed"
				leftSide.set(speed);
			}
			error = gyro.getAngle()-angle;
		}
}
	
	@Override
	public void robotInit() {		
		leftStick = new Joystick(0);
    	rightStick = new Joystick(1);
    	logController = new Joystick(2);
    	autoTimer = new Timer();
    	
    	butX = new JoystickButton(logController,3);
    	butY = new JoystickButton(logController,4);
    	butA = new JoystickButton(logController,1);
    	butB = new JoystickButton(logController,2);
    	
		gameData = DriverStation.getInstance().getGameSpecificMessage();

    	
    	gyro = new ADXRS450_Gyro();
		gyro.calibrate();
		gyro.reset();
		
		enc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
		enc.setDistancePerPulse(1.0/1440);		
		enc.setReverseDirection(true);
		enc.setMaxPeriod(.1);
		enc.setMinRate(10);
		enc.setSamplesToAverage(7);
		
		encL = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
		encL.setDistancePerPulse(1.0/1440);		
		encL.setReverseDirection(true);
		encL.setMaxPeriod(.1);
		encL.setMinRate(10);
		encL.setSamplesToAverage(7);
		
		limitSwitch = new DigitalInput(4);
		
		 
	            new Thread(() -> {
	                UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
	                camera.setResolution(640, 480);
	                
	                CvSink cvSink = CameraServer.getInstance().getVideo();
	                CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
	                
	                Mat source = new Mat();
	                Mat output = new Mat();
	                
	                while(!Thread.interrupted()) {
	                    cvSink.grabFrame(source);
	                    Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
	                    outputStream.putFrame(output);
	                }
	            }).start();
	    }
	
	
	@Override
	public void teleopPeriodic() {
		robot.setSafetyEnabled(true);
		//while (isOperatorControl() && isEnabled()) { Ok future me error fix pt 2, try and remove the loop from here so that it can work (?)
			robot.tankDrive(-leftStick.getY(), -rightStick.getY()); //Hey future me, I changed the left code to negative number just in case that s the error thats going on
		Timer.delay(.001);
		
//		if(limitSwitch.get()) {	
//			leftClaw.set(0.0);
//			rightClaw.set(0.0);
//		}else if(butX.get()){
//			leftClaw.set (-1.0);
//			rightClaw.set (1.0);
//		}else if (butB.get()) {	
//			leftClaw.set(1.0);
//			rightClaw.set(-1.0);
//		}else {
//			leftClaw.set(0.0);
//			rightClaw.set(0.0);
//		}
	
		if(butX.get()) {	
			leftClaw.set(-1.0);
			rightClaw.set(1.0);
//		}else if (limitSwitch.get()) {	
//			leftClaw.set(0.0);
//			rightClaw.set(0.0);
		}else if (butB.get()) {	
			leftClaw.set(1.0);
			rightClaw.set(-1.0);
		}else {
			leftClaw.set(0.0);
			rightClaw.set(0.0);
		}
		
		if(butY.get()) {
			arm1.set(1.0);
		}else if(butA.get()){
			arm1.set(-1.0);
		}else {
			arm1.set(0.0);
		}
	
	}
	@Override
	public void autonomousPeriodic() {
		while( isAutonomous() && isEnabled()) {
			if(autoTimer.get() < 1.5) {
 			tlMotor.setSpeed(0.5);
			blMotor.setSpeed(0.5);
			trMotor.setSpeed(-0.5);
			brMotor.setSpeed(-0.5);
			}else {
			tlMotor.setSpeed(0.0);
			blMotor.setSpeed(0.0);
			trMotor.setSpeed(0.0);
			brMotor.setSpeed(0.0);
			autoTimer.stop();
			}
			
//			
//			if(gameData.length() > 0)
//	         {
//				  if(gameData.charAt(0) == 'L')
//				  {
//					//Put left auto code here
//					  leftSide.set(0.0);
//						rightSide.set(0.0);
//						PidRot(90.0);			//Not actual left code
//						leftSide.set(0.0);
//						rightSide.set(0.0);
//				  } else {
//					//Put right auto code here
//				  }
//	        }
		}
	}
	
	@Override
	public void autonomousInit() {
		gyro.reset();
		autoTimer.start();		
	}
}