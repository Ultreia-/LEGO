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
	private static int preError = 0, error, reading, adjustedPower, 
			thresholdForTurning, turn, derivative = 0, integral = 0;
	private static double pc = 0.625;
	private static double kp = 0.9;
	private static double dt = 0.013;
	private static double ki = (kp * 1.0 * dt)/pc;
	private static double kd = (kp*pc)/(8*0.013);
    private static int power = 90;
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
	            derivative = error - preError;
	            
	            turn = (int)(kp * error) + (int)(ki * integral) + (int)(kd * derivative);

              
                    Car.forward(power+turn,power-turn);
                              
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