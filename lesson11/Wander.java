package lesson11;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;
import lejos.util.Delay;

/*
 * Cruise behavior, p. 303 in
 * Jones, Flynn, and Seiger: 
 * "Mobile Robots, Inspiration to Implementation", 
 * Second Edition, 1999.
 */

class Wander extends Thread
{
    private SharedPilot pilot;
    
    public Wander(SharedPilot pilot)
    {
    	this.pilot = pilot;
 	   
        
        //RConsole.open();
    }
    
	public void run() 
    {				       
        while (true)
        {
            LCD.drawString("HEJHEJ", 0, 0);

          //pilot.travel(Math.random()*40);           
          //while(pilot.ismoving()) Delay.msDelay(1);
         // LCD.drawString("ROTATE", 0, 0);
          
          
          
          //pilot.rotate(180);
          pilot.travel(20);
          
          //while(pilot.ismoving()) Delay.msDelay(1);
           //pilot.show();
     	   //Delay.msDelay(1000);
          //LCD.drawString("TRAVEL", 0, 0);
          
        }
    }
	
}
	

