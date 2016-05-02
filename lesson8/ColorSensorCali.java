package lesson8;

import lejos.nxt.*;
/**
 * A sensor that is able to distinguish a black/dark surface
 * from a white/bright surface.
 * 
 * Light percent values from an active light sensor and a
 * threshold value calculated based on a reading over a 
 * black/dark surface and a reading over a light/bright 
 * surface is used to make the distinction between the two 
 * types of surfaces.
 *  
 * @author  Ole Caprani
 * @version 20.02.13
 */
public class ColorSensorCali {

   private ColorSensor cs; 
   private int blackLightValue;
   private int whiteLightValue;
   private int whiteGreenThreshold;
   private int greenBlackThreshold;
   private int greenLightValue;

   public ColorSensorCali(SensorPort p)
   {
	   cs = new ColorSensor(p);
; 
	   // Use the light sensor as a reflection sensor
	   cs.setFloodlight(true);
   }

   private int read(String color){
	   
	   int lightValue=0;
	   
	   while (Button.ENTER.isDown());
	   
	   LCD.clear();
	   LCD.drawString("Press ENTER", 0, 0);
	   LCD.drawString("to calibrate", 0, 1);
	   LCD.drawString(color, 0, 2);
	   while( !Button.ENTER.isPressed() ){
	      lightValue = cs.getNormalizedLightValue();
	      LCD.drawInt(lightValue, 4, 10, 2);
	      LCD.refresh();
	   }
	   return lightValue;
   }
   
   public void calibrate()
   {
	   blackLightValue = read("black");
	   whiteLightValue = read("white");
	   greenLightValue = read("green");

	   whiteGreenThreshold = (whiteLightValue+greenLightValue)/2;
	   greenBlackThreshold = (greenLightValue+blackLightValue)/2;
   }
   
   public boolean black() {
           return (cs.getNormalizedLightValue()< greenBlackThreshold);
   }
   
   public boolean white() {
	   return (cs.getNormalizedLightValue()> whiteGreenThreshold);
   }
   
   public boolean green() {
	   return (cs.getNormalizedLightValue()>= greenBlackThreshold && cs.getNormalizedLightValue() <= whiteGreenThreshold);
   }
   
   public int light() {
 	   return cs.getNormalizedLightValue();
   }
   
}