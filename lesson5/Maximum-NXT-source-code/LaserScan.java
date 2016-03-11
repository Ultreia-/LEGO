import java.util.ArrayList;
import lejos.geom.Point;
import lejos.nxt.*;
import lejos.nxt.addon.*;
import lejos.robotics.*;
import lejos.robotics.localization.BeaconTriangle;
import lejos.robotics.navigation.Pose;


/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 25 - Beacon Localization
 * Robot: Snowcat with Laser Mount
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 *
 * This class scans a room for three beacons, verifies the angles by pointing the laser at the 
 * beacon, and then it feeds the angles into the algorithm to produce coordinates and heading.
 * 
 * This code requires the lejos NXJ library, available at www.lejos.org.
 *
 */
public class LaserScan {
	
	public static void main(String[] args) {
		
		// Set the gear ratio for the laser scanner:
		// double beaconGearRatio = -(24.0/8.0) * (40.0/8.0); // NXT 1.0 gray gears
		double beaconGearRatio = (20.0/12.0) * (36.0/12.0); // NXT 2.0 black gears
		
		// Define the three beacon coordinates:
		Point beacon1 = new Point(150,135);
		Point beacon2 = new Point(0,135);
		Point beacon3 = new Point(0,0);
		
		// Add the beacons to a BeaconTriangle object
		BeaconTriangle bt = new BeaconTriangle(beacon1,beacon2,beacon3); 
		
		// Instantiate a LaserBeaconLocator object:
		final LaserSensor ls = new LaserSensor(SensorPort.S1);
		RegulatedMotor motor = MirrorMotor.invertMotor(Motor.A);
		LaserBeaconLocator bl = new LaserBeaconLocator(ls, motor, beaconGearRatio);
		
		// Perform a scan:
		ArrayList <Double> angles = bl.locateBeacons();
		
		// Output the angles to the LCD:
		for(int i=0;i<angles.size();i++)
			System.out.println("A" + i + ": " + angles.get(i));

		// Point laser at each of the beacons:
		bl.showBeacons(angles, 2000);
		
		// Calculate the Pose and output:
		Pose p = bt.calcPose(angles.get(0), angles.get(1), angles.get(2));
		System.out.println("X " + p.getX());
		System.out.println("Y " + p.getY());
		System.out.println("Head: " + p.getHeading());
		Button.ESCAPE.waitForPressAndRelease();
	}
}