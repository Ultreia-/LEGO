import lejos.nxt.*;
import lejos.util.Delay;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 16 - Sound
 * Robot: NXT Brick
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class SoundTest {
	public static void main(String [] args) {
		for(int freq=0;freq<5000;freq+=100) {
			Sound.playTone(freq, 200);
			Delay.msDelay(200);
		}
	}
}
