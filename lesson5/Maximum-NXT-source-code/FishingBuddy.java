import lejos.nxt.*;
import lejos.util.Delay;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 5 - My First Robot
 * Robot: Fisherbot
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class FishingBuddy {

	public static void main(String[] args) {
		Motor.A.flt();
		System.out.println("Waiting for fish");
		int tacho = Motor.A.getTachoCount();
		do {
			Delay.msDelay(1000);
		} while(tacho == Motor.A.getTachoCount());
		Sound.beep();
		System.out.println("Fish on hook!");
		Motor.A.setSpeed(70);
		Motor.A.forward();
		System.out.println("ENTER to stop");
		Button.ENTER.waitForPressAndRelease();
		Motor.A.flt();
	}
}
