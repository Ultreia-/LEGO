package lesson8;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import lesson2.Car;

/*
 * Cruise behavior, p. 303 in
 * Jones, Flynn, and Seiger: 
 * "Mobile Robots, Inspiration to Implementation", 
 * Second Edition, 1999.
 */

class PIDCruise extends Thread
{
    private SharedCar car;

	private static int preError = 0, error, leftReading, rightReading, adjustedPower, 
			offset, turn, derivative = 0, integral = 0;
	private static double pc = 1;
	private static double kp = 0.3;
	private static double dt = 0.0014;
	private static double ki = (kp * 1.0 * dt)/pc;
	private static double kd = 0;//(kp*pc)/(8*dt);
    private static int power = 100;
    private static int sampleInterval = 10;
    
	public LightSensor leftSensor = new LightSensor(SensorPort.S3);
	public LightSensor rightSensor = new LightSensor(SensorPort.S1);

    
    public PIDCruise(SharedCar car)
    {
    	this.car = car;
    }
    
	public void run() 
    {	
        while (true)
        {
        	/*  Drive forward */
            leftReading = leftSensor.getNormalizedLightValue();
            rightReading = rightSensor.getNormalizedLightValue() - 51;
            
            error = leftReading - rightReading;
            
            //LCD.drawInt(leftReading, 0, 3);
            //LCD.drawInt(rightReading, 0, 4);
            
            integral += error;
            derivative = error - preError;
            
            turn = (int)(kp * error) + (int)(ki * integral) + (int)(kd * derivative);

          
            car.forward(power+turn,power-turn);
            //car.stop();
              
            
            preError = error;
            Delay.msDelay(sampleInterval);
        }
    }
}
	

