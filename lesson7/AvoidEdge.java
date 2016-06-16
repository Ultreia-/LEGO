package lesson7;


import lejos.nxt.*;
import lejos.util.Delay;
/*
 * Follow behavior , inspired by p. 304 in
 * Jones, Flynn, and Seiger: 
 * "Mobile Robots, Inspiration to Implementation", 
 * Second Edition, 1999.
 */
import lesson11.SharedPilot;

class AvoidEdge extends Thread
{
    private SharedPilot pilot = new SharedPilot();

	LightSensor light = new LightSensor(SensorPort.S4);
	
	int frontLight, leftLight, rightLight, delta;
	int lightThreshold;
	
    public AvoidEdge(SharedPilot pilot)
    {
       this.pilot = pilot;	
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
	    		pilot.noCommand();
	    		frontLight = light.getLightValue();
	    	}
	    	
        }
    }
}

