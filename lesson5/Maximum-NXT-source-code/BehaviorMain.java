import lejos.robotics.subsumption.*;
import lejos.robotics.navigation.*;
import lejos.nxt.*;

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
public class BehaviorMain {
   public static void main(String [] args) {
	  UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
      DifferentialPilot robot = new DifferentialPilot(5.6F, 13.0F,Motor.C, Motor.B, true);
      robot.setTravelSpeed(20);
      robot.setRotateSpeed(40);
      Behavior b1 = new BehaviorForward(robot);
      Behavior b2 = new BehaviorProximity(us, robot);
      Behavior b3 = new BehaviorCollision(robot);
      //Behavior [] bArray = {b1, b2, b3};
      Behavior [] bArray = {b1, b3};
      Arbitrator arby = new Arbitrator(bArray);
      arby.start();
   }
}