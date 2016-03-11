import lejos.nxt.*;
import lejos.robotics.subsumption.*;
import lejos.robotics.navigation.*;

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
public class BehaviorProximity implements Behavior {
	
   UltrasonicSensor us;
   RotateMoveController robot;   

   public BehaviorProximity(UltrasonicSensor us, RotateMoveController p) {
      this.us = us;
      this.robot = p;
   }
   
   public boolean takeControl() {
      // The following is a kludge due to a bug in NXJ reading Ultrasonic.
	  // If using LEGO Firmware, this line can be deleted:
	  try {Thread.sleep(210);}catch(Exception e){}
	  
      int dist = us.getDistance();
      return (dist < 40);
   }

   public void suppress() {
      robot.stop();
   }

   public void action() {
      robot.travel(-20);
      robot.rotate(80);
   }
}