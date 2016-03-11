import lejos.nxt.*;
import lejos.robotics.RegulatedMotor;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 11 - SCARA Arm
 * Robot: Scarab
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class SCARA {

	// Tacho count for full rotation:
	static final int SHOULDER_FULL_CIRCLE = 360;
	static final int ELBOW_FULL_CIRCLE = 360;
	
	static RegulatedMotor claw;
	static RegulatedMotor fore;
	static RegulatedMotor base;
	
	static int LIFT_CLAW = 600;
	static int OPEN_CLAW = 0;
	static int FOREARM_STRAIGHT = -145;
	static int BASEARM_STRAIGHT = 110;
	
	static double ARM_BASE_LENGTH = 15.5; // length of base arm (shoulder to elbow) cm
	static double ARM_FORE_LENGTH = 16.0; // length of fore arm (elbow to claw) cm
	
	public static void main(String[] args) {
		
		SCARA arm = new SCARA();
		arm.calibrate();
		
		// Must use odd number of coords for this to work:
		int [] x = {25, 26, 30};
		int [] y = {-10, 6, 0};
		
		int i=0;
		for(int r=0;r<16;r++) {
			arm.gotoXY(x[i], y[i]);
			arm.closeClaw();
			i++;
			if(i>=x.length) i=0;
			arm.gotoXY(x[i], y[i]);
			arm.openClaw();
			i++;
			if(i>=x.length) i=0;
		}
	}
	
	public void calibrate() {
		// Calibrate claw:
		claw = calibrate(MotorPort.C, 10, true, OPEN_CLAW, 100);
		
		// calibrate forearm:
		fore = calibrate(MotorPort.B, 30, false, FOREARM_STRAIGHT, 30);
		
		// calibrate base arm:
		base = calibrate(MotorPort.A, 20, true, BASEARM_STRAIGHT, 15);
	}
	
	public RegulatedMotor calibrate(MotorPort port, int pwr, boolean reverse, int target, int speed) {
		
		NXTMotor motor = new NXTMotor(port);
		motor.setPower(pwr);
		if(reverse)
			motor.backward();
		else
			motor.forward();
		int old = -99999999;
		while(motor.getTachoCount() != old) {
			old=motor.getTachoCount();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
		
		Sound.beep();
		RegulatedMotor regMotor = new NXTRegulatedMotor(port);
		regMotor.setSpeed(speed);
		regMotor.resetTachoCount();
		regMotor.rotate(target);
		regMotor.resetTachoCount();
		return regMotor;
	}
	
	public void openClaw() {
		claw.rotateTo(OPEN_CLAW);
	}
	
	public void closeClaw() {
		claw.rotateTo(LIFT_CLAW);
	}
	
	public void gotoXY(double x, double y) {
		
		double c = Math.sqrt(x * x + y * y);
		double angle1a = Math.asin(y/c);
		double angle1b = Math.acos((ARM_BASE_LENGTH * ARM_BASE_LENGTH + c * c - ARM_FORE_LENGTH * ARM_FORE_LENGTH)/(2F *ARM_BASE_LENGTH * c));
		
		double angle1 = angle1a + angle1b;
		double angle2 = Math.acos((ARM_BASE_LENGTH * ARM_BASE_LENGTH + ARM_FORE_LENGTH * ARM_FORE_LENGTH - c * c)/(2F *ARM_BASE_LENGTH * ARM_FORE_LENGTH));
		
		if(x < 0) {
			angle1 = Math.PI - angle1;
			angle2 = Math.PI - angle2 + Math.PI;
		}
		
		rotateShoulder(angle1);
		rotateElbow(angle2);
	}
	
	/**
	 * The neutral straight-arm position has the elbow at 180 degrees. If you try to 
	 * rotateElbow(0) the arm will fold up on itself.
	 * 
	 * @param toAngle Angle in rads (not degrees)
	 */
	public void rotateElbow(double toAngle) {
		// Arm starts at tacho 0 which is actually 180 degrees, hence subtract 1/2 ELBOW_FULL_CIRCLE
		int toCount = (int)(((toAngle/(2*Math.PI)) * ELBOW_FULL_CIRCLE) - ELBOW_FULL_CIRCLE/2F);
		toCount *= -1; // motors are reversed for this robot
		fore.rotateTo(toCount);
	}
	
	/**
	 * 
	 * @param toAngle Angle in rads (not degrees)
	 */
	public void rotateShoulder(double toAngle) {
		int toCount = (int)((toAngle/(2*Math.PI)) * SHOULDER_FULL_CIRCLE);
		toCount *= -1; // motors are reversed for this robot
		base.rotateTo(toCount);
	}
}