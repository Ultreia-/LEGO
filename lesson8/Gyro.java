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
    private int leftRightCounter = 1;

    GyroSensor gyro = new GyroSensor(SensorPort.S2);

    FixedQueue<Float> readings;

    private float delta;
    
	private float gyroThreshold = 60;

    public Gyro(SharedCar car)
    {
    	this.car = car;
    	reset();
    }
    
	public void run() 
    {
		
		Delay.msDelay(2000);
		
		while (true)
        {
			fillQueue();
			pauseUntilPlateau();
    
        	LCD.clear();
        	
         	LCD.drawString("Plateau", 1, 2); // We have reached a platou
         	
         	if(leftRightCounter % 2 == 1) {
         		
         		turnRight();
         		
         	} else {
         		
         		turnLeft();
         		
         	}
         	
         	leftRightCounter++;
         	
         	
        	Delay.msDelay(1550);
        	
        	reset();
        	
        }
    }
	
	private void turnRight() {
		
		car.forward(100, 75);
	
	}

	private void turnLeft() {
		
		car.forward(70, 100);

	}
	private void reset() {

		car.noCommand();
    	delta = 0;
    	readings = new FixedQueue<>(12);
	}

	private void pauseUntilPlateau()
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
    		
    		Delay.msDelay(25);
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

