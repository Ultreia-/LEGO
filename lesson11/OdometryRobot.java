package lesson11;


import lejos.nxt.*;

/*
 * Behavior control network of Figure 9.9 in chapter 9 in
 * Jones, Flynn, and Seiger: 
 * "Mobile Robots, Inspiration to Implementation", 
 * Second Edition, 1999.
 */
public class OdometryRobot
{
    public static void main(String [] args)  throws Exception
    {
    	SharedPilot [] pilot = { new SharedPilot(), new SharedPilot(), 
                             new SharedPilot(), new SharedPilot()};
    	
    	CarDriver cd = new CarDriver();
    	
    	/*
    	Escape escape = new Escape(car[0]);
    	Avoid avoid   = new Avoid(car[1]);
    	Follow follow = new Follow(car[2]);
    	Cruise cruise = new Cruise(car[3]);
    	*/
    	Wander wander = new Wander(pilot[0]);
    	
    	Arbiter arbiter = new Arbiter(pilot, cd);
    	
        LCD.drawString("OdometryRobot", 0, 0);
        // Button.waitForAnyPress();

        /*escape.setDaemon(true);
        escape.start();
        avoid.setDaemon(true);
        avoid.start();
        follow.setDaemon(true);
        follow.start();*/
        wander.setDaemon(true);
        wander.start();
        arbiter.setDaemon(true);	   
        arbiter.start();
       	    
        while (! Button.ESCAPE.isDown())
        {	
            LCD.drawString("Winner " + arbiter.winner(), 0, 6);
        }
    }
}