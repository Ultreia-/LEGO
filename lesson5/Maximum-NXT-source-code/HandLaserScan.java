import lejos.nxt.*;
import lejos.nxt.addon.*;
import lejos.util.Delay;


/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 23 - Building a Laser Sensor
 * Robot: NXT brick with laser sensor
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 *
 * Notes: The setup for this class is just the laser sensor plugged into port 1. You do 
 * not need any motors connected, even though the code below looks like it does.
 * 
 * When you run the program, sweep the laser over the beacon reflector to hear it beep.
 * 
 */
public class HandLaserScan {
	
	public static void main(String[] args) {
		final LaserSensor ls = new LaserSensor(SensorPort.S1);
		
		new Thread() {
			public void run() {
				int count = 0;
				do {
					count++;
					System.out.println(count + ": " + ls.readNormalizedValue());
					Delay.msDelay(500);
				} while(Button.ENTER.isUp()); 
			}
		}.start();
		
		LaserBeaconLocator bl = new LaserBeaconLocator(ls, Motor.A, 1);
		bl.startScan(ls);
		Button.ESCAPE.waitForPressAndRelease();
		bl.stopScan();
		
	}
}
