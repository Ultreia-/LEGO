package lesson3;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.SoundSensor;

/**
 * The locomotions of a LEGO 9797 car is controlled by sound detected through a
 * microphone on port 1.
 * 
 * @author Ole Caprani
 * @version 23.08.07
 */
public class PartyCar {
	private static int soundThreshold = 90;
	// private static SoundSensor leftSound = new SoundSensor(SensorPort.S1);
	// private static SoundSensor rightSound = new SoundSensor(SensorPort.S2);

	private static SensorPort leftSoundSensor = SensorPort.S1;
	private static SensorPort rightSoundSensor = SensorPort.S4;



	// Map function stolen from Arduinos implementation because it is nice :)

	private static int map(int x, int in_min, int in_max, int out_min, int out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

	private static int directionDifference(int right, int left)
	{
	
		return right - left;
		
	}
	
	public static void main(String [] args) throws Exception
    {
        LCD.drawString("dB level: ",0,0);
        LCD.refresh();
        //Is This correct?!
		leftSoundSensor.setTypeAndMode(SensorPort.TYPE_SOUND_DB, SensorPort.MODE_RAW);
		rightSoundSensor.setTypeAndMode(SensorPort.TYPE_SOUND_DB, SensorPort.MODE_RAW);



		int leftlevel;
		int rightlevel;
		int rightspeed;
		int leftspeed;
		int direction;
        while (! Button.ESCAPE.isDown())
        {
        	
        	//DIrection = left - right
        	

    		leftlevel = leftSoundSensor.readRawValue();
    		rightlevel = rightSoundSensor.readRawValue();
    		
    		leftspeed = map(leftlevel, 0, 1023, 50, 100);
    		rightspeed = map(rightlevel, 0, 1023, 50, 100);
    		
    		
    		direction = directionDifference(rightspeed, leftspeed);
    		
    		if(direction > 0) leftspeed = leftspeed - 25;
    		if(direction < 0) rightspeed = rightspeed - 25;
    		
    		
    		
    		
    		
    		Car.forward(leftspeed, rightspeed);
        	
        	
       }
       Car.stop();
       LCD.clear();
       LCD.drawString("Program stopped", 0, 0);
       Thread.sleep(2000); 
   }
}
