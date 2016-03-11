import lejos.nxt.Motor;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 18 - Walking Robots
 * Robot: Shambler
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class Crawler {

	public static int DOWN = 0;
	public static int UP = -145;
	public static int BACKWARD = 140;
	public static int FORWARD = -BACKWARD;
	public static double RATIO = 20.0/12.0;
	
	/**
	 * Test of walking gait
	 * @param args
	 */
	public static void main(String[] args) {
		Motor.A.setSpeed(70); // vertical lifter
		Motor.B.setSpeed(90); // lateral movement
		Motor.C.setSpeed(30); // rotation
		
		for(int i=0;i<4;i++) {
			stepForward(3);
			rotate(90);
		}
	}
	
	public static void stepForward(int steps) {
		for(int i=0;i<steps;i++) {
			Motor.B.rotate(FORWARD);
			Motor.A.rotateTo(UP);
			Motor.B.rotate(BACKWARD);
			Motor.A.rotateTo(DOWN);
		}
	}
	
	public static void rotate(int degrees) {
		Motor.C.rotate((int)(RATIO * degrees));
		Motor.A.rotateTo(UP);
		Motor.C.rotate((int)(RATIO * -degrees));
		Motor.A.rotateTo(DOWN);
	}
	
}
