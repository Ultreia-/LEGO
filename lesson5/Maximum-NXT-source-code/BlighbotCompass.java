import lejos.nxt.*;
import lejos.nxt.addon.*;
import lejos.robotics.navigation.*;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 17 - Localization
 * Robot: Carpet Rover
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class BlighbotCompass  {

	public static void main(String [] args) {
		CompassHTSensor cps = new CompassHTSensor(SensorPort.S1);
		CompassPilot pilot = new CompassPilot(cps, 4.32F, 15.5F,Motor.B, Motor.C, true);
		Navigator robot = new Navigator(pilot);
	
		robot.goTo(200,0);
		robot.goTo(100,100);
		robot.goTo(100,-50);
		robot.goTo(0,0);
	}
}
