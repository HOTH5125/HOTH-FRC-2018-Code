package org.usfirst.frc.team5125.robot;

public class PID extends Robot {

	public void PidRot (double error) {
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
}
//	error = 90;
//	double speed = 1.0;	
//	System.out.println(gyro.getAngle());
//	while (Math.abs(error) != 0) {
//		System.out.println(gyro.getAngle());
//		speed = error / 90;
//		if(speed >1) {
//			speed = .5;
//		}
//		if(speed < -1) {
//			speed = -.5;
//		}
//		if (error < 0) {
//			leftSide.set(-speed);
//			rightSide.set(-speed);					
//		}
//		if(error > 0) {
//			rightSide.set(-speed);
//			leftSide.set(-speed);
//		}
//		error = gyro.getAngle()-90;
//	}
}
