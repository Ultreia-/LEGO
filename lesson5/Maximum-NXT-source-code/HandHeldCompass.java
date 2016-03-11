import lejos.nxt.*;
import lejos.nxt.addon.*;
import javax.microedition.lcdui.Graphics;

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
public class HandHeldCompass {
  
  public static void main(String [] args) throws Exception {
    CompassHTSensor cmps = new CompassHTSensor(SensorPort.S1);
    Graphics g = new Graphics();
    while(!Button.ENTER.isPressed()) {
      g.clear();
      int angle = (int)cmps.getDegrees();
      LCD.drawInt(angle, 0, 0);
      g.fillArc(10,0,62, 62, angle-4, 8);
      LCD.refresh();
      Thread.sleep(200);
    }  
  }
}
