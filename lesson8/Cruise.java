package lesson8;

import lejos.nxt.SensorPort;
import lejos.util.Delay;
import lesson2.Car;

/*
 * Cruise behavior, p. 303 in
 * Jones, Flynn, and Seiger: 
 * "Mobile Robots, Inspiration to Implementation", 
 * Second Edition, 1999.
 */

class Cruise extends Thread
{
    private SharedCar car;
    private static int power = 100;

    public Cruise(SharedCar car)
    {
    	this.car = car;
    	
    }
    
	public void run() 
    {	
        while (true)
        {
        	/*  Drive forward */          
            Car.forward(power,power);        
        }
    }
}
	

