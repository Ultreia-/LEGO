package lesson2;

import java.util.Arrays;

import lejos.nxt.*;
import lejos.util.Delay;
/**
 * A simple main program to test the Tracker class.
 * 
 * @author  Ole Caprani
 * @version 02.02.15
 */
public class WallFollower extends Thread
{
	  private boolean running;
	  private UltrasonicSensor us = new UltrasonicSensor(SensorPort.S4);
	  private int distance;
	  private int power; // default values
	  private int rightCommand, leftCommand = 1;
	  private int[] distances = new int[5];
		
	  public void go()
	  {
	        running = true;
	        this.start();
	  }
		
	    public void stop()
	    {
	        running = false;
	    }

	    public void run()
	    {
	        while ( running )
	        {	            
	            rightCommand = 1;
	            leftCommand = 1;
	           
	            power = 70;
	            
	            Car.run(power, leftCommand, power, rightCommand);
	            	        	
	            for(int i = 0;i < 5;i++) {
	            	distances[i] = us.getDistance();
		            Delay.msDelay(150);
	            }
	            
	            Arrays.sort(distances);
	            
	            distance = distances[2];
	            
	            if (distance > 100) {
            		rightCommand = 4; //close to threshold, float
	            	if(distance > 120) {
	            		rightCommand = 3; //kinda far away, turn slightly
	            	}
         		
	            	if (distance > 160) {
	            		rightCommand = 2; //very far away, turn fast
	            	}
	            }

	            else if(distance < 50) { 
	            		leftCommand = 3; //kinda close, turn slightly
	            	if(distance < 30) {
	            		leftCommand = 2; //very close, turn fast
	            	}
	            } 
	            
	            Car.run(power, leftCommand, power, rightCommand);  

	            Delay.msDelay(200);
	        }
	    }
		
	    public int getDistance()
	    {
	        return distance;
	    }
		
	    public int getPower()
	    {
	        return power; 
	    }
}