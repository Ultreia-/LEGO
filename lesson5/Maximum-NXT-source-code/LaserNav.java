import java.util.ArrayList;

import lejos.geom.Point;
import lejos.nxt.*;
import lejos.nxt.addon.LaserBeaconLocator;
import lejos.nxt.addon.LaserSensor;
import lejos.robotics.localization.BeaconPoseProvider;
import lejos.robotics.localization.BeaconTriangle;
import lejos.robotics.navigation.*;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 26 - Beacon Navigation
 * Robot: Snowcat with laser mount
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class LaserNav {
		
	public static void main(String[] args) {
		Point beacon1 = new Point(246,243); // by TV
		Point beacon2 = new Point(0,210); // near corner
		Point beacon3 = new Point(0,0); // by couch
		
		// double beaconGearRatio = (20.0/12.0) * (36.0/12.0); // NXT 1.0
		double beaconGearRatio = (24.0/8.0) * (40.0/8.0); // NXT 2.0
		
		LaserSensor ls = new LaserSensor(SensorPort.S1);
		LaserBeaconLocator bl = new LaserBeaconLocator(ls, Motor.A, beaconGearRatio);
		BeaconTriangle bt = new BeaconTriangle(beacon1, beacon2, beacon3);
		
		double TREAD_SIZE = 3.25;
		// double GEAR_RATIO = 12.0/20.0; // NXT 1.0
		double GEAR_RATIO = 1; // NXT 2.0
		
		double LEFT_SIZE = GEAR_RATIO * TREAD_SIZE;
		DifferentialPilot pilot = new DifferentialPilot(LEFT_SIZE, TREAD_SIZE, 20, Motor.B, Motor.C, false);
				
		BeaconPoseProvider pp = new BeaconPoseProvider(bl, bt, pilot, 4);
		Navigator nav = new Navigator(pilot, pp);
		
		for(int i=0;i<10;i++) {
		nav.goTo(150, 75);
		
		nav.goTo(50, 100);
		
		nav.goTo(120, 200); // return home
		}
		Button.ESCAPE.waitForPressAndRelease(); 
	}
}
