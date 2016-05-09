package lesson8;


import lejos.nxt.*;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lesson8.CarCommand.Command;
/*
 * A car driver module with a method to drive
 * a differential car with two independent motors. The left motor 
 * should be connected to port B and the right motor
 * to port C.
 * 
 * The car driver method turns car commands into actual commands
 * for the two physical motors.
 *  
 * @author  Ole Caprani
 * @version 21.3.14
 */
public class CarDriver
{
	// Commands for the motors
	final int forward  = 1,
	          backward = 2,
	          stop     = 3;
	
    private MotorPort leftMotor = MotorPort.C;
    private MotorPort rightMotor= MotorPort.B;	
    private DifferentialPilot pilot;

    
	public CarDriver()
	{
		
		pilot = new DifferentialPilot(8.2, 16.0, Motor.C, Motor.B);
		
	}
	
	private void setPilotMode(CarCommand.Command carCommand)
	{
		switch ( carCommand )
		{
		case FORWARD:  pilot.forward();
		case BACKWARD: pilot.backward();
		case STOP:     pilot.stop();
		case LEFT: 		turnleft();
		case RIGHT: 	pilot.arc(20, 90);

		}
		
	}
	
	
	public void turnleft()

	public void perform(CarCommand command)
	{	
		
		pilot.setTravelSpeed((command.leftPower + command.rightPower)/2); // A hack to save the time necessary to refactor sharedCar
		
		setPilotMode(command.command);
		
    }
}

