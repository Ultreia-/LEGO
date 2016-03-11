import lejos.nxt.Button;
import lejos.robotics.navigation.*;
import lejos.util.Delay;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 19 - Walking and Navigation
 * Robot: Shambler
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class Shambler {

	public static void main(String[] args) {
		
		WalkerPilot wp = new WalkerPilot();
		Navigator nav = new Navigator(wp);
		nav.goTo(0, 18);
		nav.goTo(18, 18);
		nav.goTo(0, 0);
		System.out.println("Hit enter to end");
		Button.ENTER.waitForPressAndRelease();
	}
}
