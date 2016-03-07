package lesson4;

import lejos.nxt.*;
import lejos.util.Delay;
import lesson2.Car;
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
	private static int preError, error, reading, adjustedPower, thresholdForTurning, integral, turn;
	private static double kp = 1.0;
	private static double ki = 0.1;
    private static int power = 50;
    private static int minPower = 70, maxPower = 100;
    private static int sampleInterval = 10;
	
  public static void main (String[] aArg)
  throws Exception
  {
     BlackWhiteSensor sensor = new BlackWhiteSensor(SensorPort.S3);
	 
     //sensor.calibrate();
     
     thresholdForTurning = 47;//sensor.getThreshold();
	 
     LCD.clear();
     LCD.drawString("Light: ", 0, 2); 
	 
     while (! Button.ESCAPE.isDown())
     {
     	 
	     LCD.drawInt(sensor.light(),4,10,2);
	     LCD.refresh();

	        {		   
	            reading = sensor.light();

	            error = reading - thresholdForTurning;
	            
	            integral += error;
	            
	            turn = (int)(kp * error) + (int)(ki*integral);

                if ( error > 0 )
                { 
                	//on white
                    //power = Math.min(minPower + power, maxPower);
                    Car.forward(power+turn,power-turn);
                }
                else 
                {
                	//on black
                    //power = Math.min(minPower + Math.abs(power), maxPower);
                    Car.forward(power-turn, power+turn);		    	 
                }

                if(preError > 0 && error <= 0) integral = 0;
                if(preError < 0 && error >= 0) integral = 0;
                
                preError = error;
                
                Delay.msDelay(sampleInterval);
		
	        }
	     
	     Thread.sleep(10);
     }
     
     Car.stop();
     LCD.clear();
     LCD.drawString("Program stopped", 0, 0);
     LCD.refresh();
   }
}