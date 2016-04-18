package lesson6;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lesson3.Car;

public class Vehicle1 {
	
	private static SensorPort soundSensor = SensorPort.S1;
	
	
	private static int map(int x, int in_min, int in_max, int out_min, int out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
	public static void main(String [] args) throws Exception
    {
        //Is This correct?!
		soundSensor.setTypeAndMode(SensorPort.TYPE_SOUND_DB, SensorPort.MODE_RAW);

		int soundLevel;
		int speed1;
		int speed2;

		
        while (! Button.ESCAPE.isDown())
        {
        	//DIrection = left - right
    		soundLevel = soundSensor.readRawValue();
    		speed1 = map(soundLevel, 0, 1023, 0, 100);	
    		speed2 = map(soundLevel, 0, 1023, 0, -100);	
    		
    		
    		
    		LCD.drawString("Raw: " + soundLevel,0,0);
    		LCD.drawString("Speed: " + speed1,0,1);
    		    		
			Car.forward(speed1, speed2);

    		
       }



       Car.stop();
       LCD.clear();
       LCD.drawString("Program stopped", 0, 0);
       Thread.sleep(2000); 
   }
}