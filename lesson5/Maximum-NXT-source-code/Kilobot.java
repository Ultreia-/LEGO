import lejos.nxt.*;
import lejos.robotics.navigation.OmniPilot;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 27 - Omnidirectional Robots
 * Robot: Kilobot
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class Kilobot {

	public static void main(String[] args) throws Exception {
		float wheelDistanceFromCenter = 8.2F;
		float wheelDiameter = 4.8F;
		boolean centralWheelFrontal = false;
		boolean motorReverse = false;
		
		OmniPilot p = new OmniPilot(wheelDistanceFromCenter, wheelDiameter, Motor.B, Motor.A, Motor.C, centralWheelFrontal, motorReverse);
		
		// Travel square
		for(int i=0;i<4;i++) {
			p.travel(80);	
			p.rotate(90);
		}
		System.out.println("Press ENTER");
		Button.ENTER.waitForPressAndRelease();
		
		// Travel square facing north
		for(int i=0;i<360;i+=90) {
			p.moveStraight(15, i);
			Thread.sleep(5500);
			p.stop();
		}
		
		System.out.println("Press ENTER again");
		Button.ENTER.waitForPressAndRelease();
		
		// Travel square while spinning
		for(int i=0;i<360;i+=90) {
			p.spinningMove(15, 70, i);
			Thread.sleep(5500);
			p.stop();
		}
		
	}
}