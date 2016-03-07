package lesson3;
import lejos.nxt.*;
/**
 * A simple sonar sensor test program.
 * 
 * The sensor should be connected to port 1. In the
 * known bugs and limitations of leJOS NXJ version alfa_03
 * it is mentioned that a gap of at least 300 msec is 
 * needed between calls of getDistance. This is the reason 
 * for the delay of 300 msec between sonar readings in the 
 * loop.
 * 
 * @author  Ole Caprani
 * @version 30.08.07
 */
public class SoundSensorTest 
{

	public static void main(String [] args)  
	throws Exception 
	{
		
		SoundSensor ss = new SoundSensor(SensorPort.S1);
		
		LCD.drawString("Ultrasonic ", 0, 0);
		LCD.drawString("Reading: ", 0, 2);
		
		int highestValue 	= 0;
		int time 			= 0;

		while (! Button.ESCAPE.isDown())
		{
			if(ss.readValue() > highestValue) highestValue = ss.readValue();

			LCD.drawInt(highestValue,3,13,2);

			time += 300;
			if(time > 3000)
			{
				time = 0; highestValue = 0;
			} 
			
			Thread.sleep(300);
		}
		LCD.clear();
		LCD.drawString("Program stopped", 0, 0);
		Thread.sleep(2000);
	}
}
