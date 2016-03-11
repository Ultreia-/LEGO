import lejos.nxt.*;
import lejos.nxt.addon.LaserSensor;
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
 */
public class LaserTest implements ButtonListener {

	LaserSensor laser;
	
	public LaserTest(){
		System.out.println("Hit ESC to end");
		laser = new LaserSensor(SensorPort.S1);
		laser.setLaser(false);
	}
	
	public static void main(String[] args) {
		LaserTest test = new LaserTest();
		Button.ENTER.addButtonListener(test);
		while(!Button.ESCAPE.isDown()) {
			Thread.yield();
		}
	}

	public void buttonPressed(Button b) {}

	public void buttonReleased(Button b) {
		laser.setLaser(!laser.isLaserOn());
		if(laser.isLaserOn())
			System.out.print("ON");
		else
			System.out.print("OFF");
		Delay.msDelay(50);
		System.out.println(": " + laser.getNormalizedLightValue());
	}
}
