import lejos.robotics.subsumption.*;
import lejos.robotics.navigation.*;
import lejos.util.Delay;

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
public class BehaviorForward implements Behavior {

   DifferentialPilot robot;

   public BehaviorForward(DifferentialPilot p) {
      this.robot = p;
   }

   public boolean takeControl() {
	   Delay.msDelay(200);
	   return true;
   }

   public void suppress() {
	   robot.stop();
   }

   public void action() {
	   if(!robot.isMoving()) robot.forward();
	   Delay.msDelay(200);
   }
}
