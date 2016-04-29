package lesson7;


import lejos.nxt.*;
import lejos.util.Delay;
/*
 * Follow behavior , inspired by p. 304 in
 * Jones, Flynn, and Seiger: 
 * "Mobile Robots, Inspiration to Implementation", 
 * Second Edition, 1999.
 */

class FollowWithMotor extends Thread
{
    private SharedCar car = new SharedCar();

	private int power = 70, ms = 250;
	LightSensor light = new LightSensor(SensorPort.S4);
	private MotorPort sensorMotor = MotorPort.A;
	
	int frontLight, leftLight, rightLight, delta;
	int lightThreshold;
	
    private final static int forward  = 1,
            backward = 2,
            stop     = 3;
	
    public FollowWithMotor(SharedCar car)
    {
       this.car = car;	
       lightThreshold = light.getLightValue();
    }
    
	public void run() 
    {				       
        while (true)
        {
	    	// Monitor the light in front of the car and start to follow
	    	// the light if light level is above the threshold
        	frontLight = light.getLightValue();
	    	while ( frontLight <= lightThreshold )
	    	{
	    		car.noCommand();
	    		frontLight = light.getLightValue();
	    	}
	    	
	    	// Follow light as long as the light level is above the threshold
	    	while ( frontLight > lightThreshold )
	    	{
	    		// Get the light to the left

	    		Motor.A.rotateTo(45);
	    		leftLight = light.getLightValue();
	    		
	    		// Get the light to the right
	    		//car.backward(0, power);
	    		Motor.A.rotateTo(-45);
	    		rightLight = light.getLightValue();
	    		
	    		// Turn back to start position
	    		Motor.A.rotateTo(0);
	    		Delay.msDelay(ms);
	    	
	    		// Follow light for a while
	    		delta = leftLight-rightLight;
	    		car.forward(power+delta, power-delta);
	    		Delay.msDelay(ms);
    		
	    		frontLight = light.getLightValue();
	    	}
	    	
	    	car.stop();
	    	Delay.msDelay(ms);
 			
        }
    }
}

