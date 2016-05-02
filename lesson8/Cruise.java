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

	private static int preError = 0, error, reading, adjustedPower, 
			thresholdForTurning, turn, derivative = 0, integral = 0;
	private static double pc = 0.625;
	private static double kp = 0.9;
	private static double dt = 0.013;
	private static double ki = (kp * 1.0 * dt)/pc;
	private static double kd = (kp*pc)/(8*0.013);
    private static int power = 90;
    private static int sampleInterval = 10;
    
	public ColorSensorCali sensor = new ColorSensorCali(SensorPort.S1);
    
    public Cruise(SharedCar car)
    {
    	this.car = car;
    	sensor.calibrate();
    	
    }
    
	public void run() 
    {	
        while (true)
        {
        	/*  Drive forward */
            reading = sensor.light();
            
            error = reading - thresholdForTurning;
            
            integral += error;
            derivative = error - preError;
            
            turn = (int)(kp * error) + (int)(ki * integral) + (int)(kd * derivative);

          
            Car.forward(power+turn,power-turn);
                          
            preError = error;
            Delay.msDelay(sampleInterval);            
        }
    }
}
	

