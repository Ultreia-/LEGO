package lesson11;


/**
 * A locomotion module with methods to drive
 * a differential car with two independent motors.
 * The methods turns the parameters for the two motors
 * into commands for a car driver that will fetch the commands
 * and turn them into motor commands for the physical motors. 
 *  
 * @author  Ole Caprani
 * @version 26.3.14
 */
public class SharedPilot implements Pilot 
{    
    private boolean commandReady;
    private CarCommand carCommand;
	                         	
    public SharedPilot()
    {
    	commandReady = false;
    	carCommand = new CarCommand();
    } 
    
    public void stop() 
    {
    	carCommand.command = carCommand.command.STOP;
    	commandReady = true;
    }
    
    public void show() 
    {
    	carCommand.command = carCommand.command.PRINT;
    	commandReady = true;
    }
    
   
    public void rotate(double degrees) {
    	carCommand.command = carCommand.command.ROTATE;
    	carCommand.setValue(degrees);
    	commandReady = true;

    }
    
    public void travel(double distance)
    {
    	carCommand.command = carCommand.command.TRAVEL;
    	carCommand.setValue(distance);
    	commandReady = true;
    }
    
    public boolean ismoving() {
    	return carCommand.ismoving;
    	
    }
    
    public void noCommand()
    {
    	commandReady = false;
    }
    
    public CarCommand getCommand()
    {
    	CarCommand result = null;
    	if ( commandReady )
    	{
    		result = carCommand;
    	}
    	return result;
    }
}
