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
    private int plateuCount = 0;
    private int[] turnCommands = {1, -1, 180, 0}; //-1: left, 0: nothing, 1: right -180: 180 TURN
    private long programStartTime;
    
    GyroSensor gyro = new GyroSensor(SensorPort.S2);

    FixedQueue<Float> readings;

    private float delta;
    
	private float gyroThreshold = 60;
	private PIDCruise cruise;

    public Gyro(SharedCar car, PIDCruise cruise)
    {
    	this.car = car;
    	this.cruise = cruise;
    	reset();
    }
    
	public void run() 
    {
		
		programStartTime = System.currentTimeMillis();
		
		while (true)
        {
			Delay.msDelay(1500);
			
			fillQueue();
			pauseUntilPlateau();
    
        	LCD.clear();
        	
         	LCD.drawString("Plateau", 1, 2); // We have reached a plateau
         	
         	try{
	         		
	         	switch(turnCommands[plateuCount])
	         	{
	         		case -1:
	         			turnLeft();
	         			break;
	         		case 1:
	             		turnRight();
	         			break;
	         		case 180: 
	         			turn180();
	         			break;
	         		case 0:
	         			stop();
	         			break;
	         	}
	         	
	         	plateuCount++;
	         	
	        	cruise.reset();
	        	reset();
	        
         	}
         	catch(Exception e) //turn off turning behavior
         	{
         		car.noCommand();
         	}	
        }
    }
	
	
	private void stop() { 
		
		turn90Degrees();    	
    	car.forward(100,  100);
    	
    	Delay.msDelay(500);
    	
    	car.floatMotor();
    	
    	gyroThreshold = 200;
    
    	pauseUntilPlateau();
    	
    
		long timestamp = System.currentTimeMillis();
		
		long racetime = timestamp - programStartTime;
		
		LCD.clear();
		
		LCD.drawString("TIME: " + (racetime), 0, 0);
	
		car.stop();
		
		while(true) {
		
			
			
			
		}
		
	}

	private void turn90Degrees() {

		car.forward(100, -100);
    	Delay.msDelay(110);
	
	}

	private void turn180() {
		car.forward(100, -100);
		
    	Delay.msDelay(360);
	}

	private void turnRight() {
		
		car.forward(100, 64);

    	Delay.msDelay(1025);
    
	
	}

	private void turnLeft() {
		
		car.forward(64, 100);

    	Delay.msDelay(1100);
    

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

