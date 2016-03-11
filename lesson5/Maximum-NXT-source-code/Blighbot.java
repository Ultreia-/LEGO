import lejos.nxt.*;
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
public class Blighbot {
  public static void main(String [] args) {
    DifferentialPilot robot = new DifferentialPilot (4.2F, 15.5F, Motor.B, Motor.C, true);
    Navigator nav = new Navigator(robot);
    nav.goTo(200,0);
    nav.goTo(100,100);
    nav.goTo(100,-50);
    nav.goTo(0,0);
  }
}
