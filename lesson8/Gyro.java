package lesson8;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroSensor;
import lejos.util.Delay;

/*
 * Cruise behavior, p. 303 in
 * Jones, Flynn, and Seiger: 
 * "Mobile Robots, Inspiration to Implementation", 
 * Second Edition, 1999.
 */

class Gyro extends Thread
{
    private SharedCar car;

    GyroSensor gyro = new GyroSensor(SensorPort.S2);

    FixedQueue<Float> readings;;

    private float delta;
    
	private float gyroThreshold = 70;

    public Gyro(SharedCar car)
    {
    	this.car = car;
    	reset();
    }
    
	public void run() 
    {
		
		while (true)
        {
			fillQueue();
			pauseTillDelta();
    
        	LCD.clear();
        	
         	LCD.drawString("Platou", 1, 2); // We have reached a platou
        	
         	
         	car.turnRight();
        	
        	
        	Delay.msDelay(1000);
        	
        	reset();
        	
        }
    }
	
	private void leftTurn() {
		
		
		
	}
	
	private void rightTurn() {
		
		
		
	}

	private void reset() {

		car.noCommand();
    	delta = 0;
    	readings = new FixedQueue<>(10);
	}

	private void pauseTillDelta()
	{
		while ( delta < gyroThreshold )
    	{
    		float value = gyro.readValue();
            readings.add(value);
            
            float lastValue = readings.take();
            delta = Math.abs(lastValue - value);
            
        	LCD.drawString("value " + value, 0, 1);
            LCD.drawString("lastValue " + lastValue, 0, 2);
            LCD.drawString("delta " + delta, 0, 3);
            
    		car.noCommand();
    		
    		Delay.msDelay(50);
    	}
		
	}

	private void fillQueue()
	{
		while (!readings.hasReachedMaxSize())
    	{
    		float value = gyro.readValue();
            readings.add(value);
            
            Delay.msDelay(10);
    	}
		
	}
}

