package lesson11;

import lejos.nxt.LCD;

/*
 *  Arbiter pass the highest priority car command
 *  from the behaviors to the car driver.   
 */
public class Arbiter extends Thread
{
	private SharedPilot [] pilot;
	private CarDriver cd;
	private int winner;
	
    public Arbiter(SharedPilot [] pilot, CarDriver cd)
    {
    	this.pilot = pilot;
    	this.cd = cd;
    }
    	
	public void run()
	{
	    while ( true )
	    {
		    for (int i=0; i < pilot.length; i++)
		    {
		    	CarCommand carCommand = pilot[i].getCommand();
		    	if ( carCommand != null)
		    	{
		    		cd.perform(carCommand);
		    		winner = i;
		    		break;
		    	}
		    }
		   		   
	    }

    }
	
	public int winner()
	{
		return winner;
	}
}
	

