package lesson4;

import lejos.nxt.*;
/**
 * A simple line follower for the LEGO 9797 car with
 * a light sensor. Before the car is started on a line
 * a BlackWhiteSensor is calibrated to adapt to different
 * light conditions and colors.
 * 
 * The light sensor should be connected to port 3. The
 * left motor should be connected to port C and the right 
 * motor to port B.
 * 
 * @author  Ole Caprani
 * @version 20.02.13
 */
public class PIDLineFollower
{
	private static int error;
	private static int numberOfBlacks;
	private static int numberOfWhites;
	private static int adjustedPower;
	private static float gain = 0.05f;
	
  public static void main (String[] aArg)
  throws Exception
  {
     final int power = 60;
	  
     BlackWhiteSensor sensor = new BlackWhiteSensor(SensorPort.S3);
	 
     sensor.calibrate();
	 
     LCD.clear();
     LCD.drawString("Light: ", 0, 2); 
	 
     while (! Button.ESCAPE.isDown())
     {
     	 
	     LCD.drawInt(sensor.light(),4,10,2);
	     LCD.refresh();
	     
	     
	     if ( sensor.black() ) {
	     	numberOfBlacks += 1;
	        numberOfWhites = 0;
	        adjustedPower = (int) (power * (numberOfBlacks * gain));
	    	Car.forward(adjustedPower, 0);

	     }
	     else {
	         numberOfWhites += 1;
	         numberOfBlacks = 0;
	         adjustedPower = (int) (power * (numberOfWhites * gain));
	    	 Car.forward(0, adjustedPower);
	     }
	     
	     Thread.sleep(10);
     }
     
     Car.stop();
     LCD.clear();
     LCD.drawString("Program stopped", 0, 0);
     LCD.refresh();
   }
}