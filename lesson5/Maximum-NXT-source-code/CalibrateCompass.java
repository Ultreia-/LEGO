import lejos.nxt.addon.*;
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
public class CalibrateCompass   {
  
  public static void main(String [] args) {
    CompassHTSensor cmps = new CompassHTSensor(SensorPort.S1);
    
    CompassPilot hector = new CompassPilot(cmps, 4.32F, 15.5F,Motor.B, Motor.C, true);
    hector.calibrate();
  }
}
