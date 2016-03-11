import lejos.robotics.subsumption.*;
import lejos.robotics.navigation.*;
import lejos.util.Delay;
import lejos.nxt.*;
import lejos.nxt.comm.RConsole;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 14 - Behavior-Based Robotics
 * Robot: Carpet Rover
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class BehaviorCollision implements Behavior {

   boolean isUpToSpeed = false;
   RotateMoveController robot;

   public BehaviorCollision(RotateMoveController p) {
      robot = p;
      Motor.B.setStallThreshold(10, 100);
      Motor.C.setStallThreshold(10, 100);
   }

   public boolean takeControl() {
      	
	  if(Motor.B.isStalled()|Motor.C.isStalled()) {
		  RConsole.print("3 ");
		  Delay.msDelay(200);
		  return true; // TEST THIS!
	  }
	  else
      return false;
	
   }

   public void suppress() {
	   RConsole.println("About to suppress 3");
	   robot.stop();
	   RConsole.println("Suppressed 3");
   }

   public void action() {
	   RConsole.print("A3 ");
	   Sound.twoBeeps();
      robot.travel(-75);
      robot.rotate(-80);
   }
}