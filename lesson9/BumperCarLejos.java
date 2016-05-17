package lesson9;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
//import lejos.robotics.MirrorMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

/**
 * Demonstration of the Behavior subsumption classes.
 * 
 * Requires a wheeled vehicle with two independently controlled
 * motors connected to motor ports A and C, and 
 * a touch sensor connected to sensor  port 1 and
 * an ultrasonic sensor connected to port 3;
 * 
 * @author Brian Bagnall and Lawrie Griffiths, modified by Roger Glassey
 *
 */
public class BumperCarLejos
{
  static RegulatedMotor leftMotor = Motor.A;
  static RegulatedMotor rightMotor = Motor.C;
  
  // Use these definitions instead if your motors are inverted
  // static RegulatedMotor leftMotor = MirrorMotor.invertMotor(Motor.A);
  //static RegulatedMotor rightMotor = MirrorMotor.invertMotor(Motor.C);
  
  public static void main(String[] args)
  {
    leftMotor.setSpeed(400);
    rightMotor.setSpeed(400);
    Behavior b1 = new DriveForwardLejos();
    Behavior b2 = new DetectWallLejos();
    Behavior b3 = new ExitLejos();
    Behavior[] behaviorList =
    {
      b1, b2, b3
    };
    Arbitrator arbitrator = new Arbitrator(behaviorList);
    LCD.drawString("Bumper Car",0,1);
    Button.waitForAnyPress();
    arbitrator.start();
  }
}


class DriveForwardLejos implements Behavior
{

  private boolean _suppressed = false;

  public boolean takeControl()
  {
	  return true;
  }

  public void suppress()
  {
    _suppressed = true;// standard practice for suppress methods
  }

  public void action()
  {
    _suppressed = false;
    BumperCarLejos.leftMotor.forward();
    BumperCarLejos.rightMotor.forward();
    while (!_suppressed)
    {
      Thread.yield(); //don't exit till suppressed
    }
    BumperCarLejos.leftMotor.stop(); 
    BumperCarLejos.leftMotor.stop();
  }
}


class DetectWallLejos implements Behavior
{

	  private boolean _suppressed = false;
	
  public DetectWallLejos()
  {
    touch = new TouchSensor(SensorPort.S1);
    
    sonarReader = createRunnable(new UltrasonicSensor(SensorPort.S3), this);
    
    Thread thread = new Thread(sonarReader);
    
    thread.start();
    
  }

  public boolean takeControl()
  {
	  
    return touch.isPressed() || distance < 25;
  }

  public void suppress()
  {
	  _suppressed = true;
  }

  public void action()
  {
	  _suppressed = false;
	  
	  int count = 0;
	  
	while(!_suppressed && count < 1000){  
	    BumperCarLejos.leftMotor.backward();
	    BumperCarLejos.rightMotor.backward();
	    Delay.msDelay(1);
	    count++;
	}
	BumperCarLejos.leftMotor.rotate(-180, true);// start Motor.A rotating backward
    BumperCarLejos.rightMotor.rotate(-360, true);  // rotate C farther to make the turn
  }
  
  
  private Runnable createRunnable(final UltrasonicSensor ultrasonicsensor, final DetectWallLejos detectwall) {
	  
	  return new Runnable() {
		
		  private DetectWallLejos walldetecter = detectwall;
		  private UltrasonicSensor sonar = ultrasonicsensor;
		  

		public void run() {
			
			while(true) {
				
			sonar.ping();
			
			int reading = sonar.getDistance();
			
			walldetecter.setDistance(reading);
			
			Delay.msDelay(25); // The delay between reads
				
			}
			
		}		   
	  };
  }
  
  private TouchSensor touch;
  private Runnable sonarReader;
  
  public int getDistance() {
	return distance;
}

public void setDistance(int distance) {
	this.distance = distance;
}

private int distance = 0;
  
}

class ExitLejos implements Behavior
{

	public boolean takeControl() {
		if(Button.ESCAPE.isDown()) {
			return true;
		}
		return false;
	}

	public void action() {
		System.exit(0);
	}

	public void suppress() {
		// TODO Auto-generated method stub
		
	}
}


