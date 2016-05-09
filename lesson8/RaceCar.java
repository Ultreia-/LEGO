package lesson8;


import lejos.nxt.*;
import lejos.util.Delay;

/*
 * Behavior control network of Figure 9.9 in chapter 9 in
 * Jones, Flynn, and Seiger: 
 * "Mobile Robots, Inspiration to Implementation", 
 * Second Edition, 1999.
 */
public class RaceCar
{
    public static void main(String [] args)  throws Exception
    {	
    	
    	SharedCar[] car = { new SharedCar(), new SharedCar()};
    	
    	CarDriver cd = new CarDriver();

    	//Cruise cruise = new Cruise(car[0]);
    	//CruiseStraight cruise = new CruiseStraight(car[1]);
    	PIDCruise cruise = new PIDCruise(car[1]);
    	
    	Gyro gyro = new Gyro(car[0], cruise);
    	
    	//Gyro stuff   	

    	Button.waitForAnyPress();
    	
    	Delay.msDelay(1000);
    	
    	Arbiter arbiter = new Arbiter(car, cd);
    	LCD.clear();

        arbiter.setDaemon(true);	   
        arbiter.start();
        cruise.setDaemon(true);
        cruise.start();
        gyro.setDaemon(true);
        gyro.start();
	   	
        while (! Button.ESCAPE.isDown())
        {	
            LCD.drawString("Winner " + arbiter.winner(), 0, 5);
        }
       
    }
}