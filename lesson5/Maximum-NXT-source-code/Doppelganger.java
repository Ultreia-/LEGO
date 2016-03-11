import java.io.IOException;
import lejos.nxt.comm.*;
import lejos.nxt.*;
import lejos.nxt.remote.*;
import lejos.robotics.navigation.*;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 22 - Multiple NXT Bricks
 * Robot: Carpet Rover
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class Doppelganger {

	static RemoteNXT nxt;
	public static final double DIAM = ArcMoveController.WHEEL_SIZE_NXT2;
	//public static final float DIAM = ArcMoveController.WHEEL_SIZE_NXT1;
	
	public static void main(String [] args) {
		try {
			System.out.println("Connecting...");
			// IMPORTANT Change the name to your slave NXT's name:
			nxt = new RemoteNXT("NXT", Bluetooth.getConnector());
			//nxt = new RemoteNXT("NXT", RS485.getConnector());
			System.out.println("Connected.");			
		} catch (IOException e) {
			System.out.println("Fail " + e.getMessage());
			Button.ESCAPE.waitForPressAndRelease();
			System.exit(1);
		}
			
		DifferentialPilot p1 = new DifferentialPilot(DIAM, 16.0, Motor.B, Motor.C, true);
		Navigator nxt1 = new Navigator(p1);
		DifferentialPilot p2 = new DifferentialPilot(DIAM, 16.0, nxt.B, nxt.C, true);
		Navigator nxt2 = new Navigator(p2);
		
		for(int i=0;i<3;i++) {
			int x = (int)(Math.random() * 100);
			int y = (int)(Math.random() * 100);
			System.out.println(i + ": " + x + ", " + y);
			nxt2.addWaypoint(x, y);
			nxt1.goTo(x, y);
		}
		nxt1.addWaypoint(0, 0);
		nxt2.goTo(0, 0);
		nxt.close();
	}
}