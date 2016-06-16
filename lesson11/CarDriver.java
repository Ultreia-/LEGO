package lesson11;


import lejos.nxt.*;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;
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
	
    double wheelDiameter = 5.5, trackWidth = 16.5;
    double travelSpeed = 5, rotateSpeed = 45;
    NXTRegulatedMotor left = Motor.B;
    NXTRegulatedMotor right = Motor.C;
    DifferentialPilot pilot;
    OdometryPoseProvider poseProvider;
    
    
	public CarDriver()
	{
        pilot = new DifferentialPilot(wheelDiameter, trackWidth, left, right);
        pilot.setTravelSpeed(travelSpeed);
        pilot.setRotateSpeed(rotateSpeed);

        poseProvider = new OdometryPoseProvider(pilot);
        Pose initialPose = new Pose(0,0,0);
        

		
	}

	public void perform(CarCommand command)
	{		

		LCD.drawString(command.command + "", 0, 0);
		command.ismoving = true;
		switch(command.command) {
		case TRAVEL: travel(command.getValue());
		case ROTATE: rotate(command.getValue());
		case STOP: stop();
		//case PRINT: show(poseProvider.getPose());
		
		}
		
		command.ismoving = false;
    }
	
	void travel(double distance) {
		pilot.travel(distance);

	}
	
	
	void stop() {
		pilot.stop();
	}
	
	
	void rotate(double degrees) {
		pilot.rotate(degrees);
	}
	

	   private static void show(Pose p) {
	      LCD.clear();
	       LCD.drawString("Pose X " + p.getX(), 0, 2);
	       LCD.drawString("Pose Y " + p.getY(), 0, 3);
	       LCD.drawString("Pose V " + p.getHeading(), 0, 4);
	   }
}

