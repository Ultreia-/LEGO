package lesson11MonteCarlo;

import java.util.Arrays;

import lejos.nxt.*;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import lejos.util.Delay;
import lesson10.SlaveIOStreams;

public class BumperCar{
	public static SlaveIOStreams PC;

	public static void main(String[] args)
	{		
		PC = new SlaveIOStreams(false);
    	PC.open();
    	
        double rightWheel = 5.41, leftWheel = 5.49, trackWidth = 16.0;
	    double travelSpeed = 5, rotateSpeed = 45;
	    NXTRegulatedMotor left = Motor.B;
	    NXTRegulatedMotor right = Motor.C;
	
	    DifferentialPilot pilot = new DifferentialPilot(leftWheel, rightWheel, trackWidth, left, right, false);
	    OdometryPoseProvider poseProvider = new OdometryPoseProvider(pilot);

	       
	    Pose initialPose = new Pose(0,0,0);
	    poseProvider.setPose(initialPose);

	    pilot.setTravelSpeed(travelSpeed);
	    pilot.setRotateSpeed(rotateSpeed);
	    pilot.addMoveListener(new MoveListener(){

	    	BlackWhiteSensor bwSensor = new BlackWhiteSensor(SensorPort.S1);
	    	
			@Override
			public void moveStarted(Move event, MoveProvider mp) {
				// TODO Auto-generated method stub
				/*
				if(event.getMoveType() == Move.MoveType.ROTATE)
					show(poseProvider.getPose());
				*/
			}

			@Override
			public void moveStopped(Move event, MoveProvider mp) {
				//LCD.drawString("MoveType: " + event.getMoveType(), 0, 2);
				sendMove(event);
				sendLight();
				LCD.drawString("Black: " + bwSensor.black(), 0, 1);
				LCD.drawString("White: " + bwSensor.white(), 0, 2);
				LCD.drawString("Reading: " + bwSensor.light(), 0, 3);
				

			}
			private void sendLight() {
				PC.output(bwSensor.light());
				
			}

			private void sendMove(Move move)
			{	
				PC.output((move.getMoveType() == Move.MoveType.TRAVEL? 0:1 ));
				PC.output(move.getDistanceTraveled());
				PC.output(move.getAngleTurned());
			}
			
			/*private void show(Pose p)
			{
				LCD.clear();
				LCD.drawString("Pose X " + p.getX(), 0, 2);
				LCD.drawString("Pose Y " + p.getY(), 0, 3);
				LCD.drawString("Pose V " + p.getHeading(), 0, 4);
				
				if(PC != null) {
					PC.output(p.getX());
					PC.output(p.getY());
					PC.output(p.getHeading());
				}
			}*/
	    });
	    
	    BlackWhiteSensor sensor = new BlackWhiteSensor(SensorPort.S1);
		 
	    sensor.calibrate();
	    
	    UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S2);
		  
	  Behavior b1 = new AvoidEdge(pilot, sensor);
	  Behavior b2 = new Wander(pilot, poseProvider);	  
	  Behavior b3 = new Exit();
	  Behavior b4 = new AvoidObject(pilot, poseProvider, ultrasonicSensor);
	  //Behavior b4 = new PilotBehavior(route, pilot);
	  
	  Behavior[] behaviorList =
	  {
	    b1,b2,b3,b4
	  };
	  Arbitrator arbitrator = new Arbitrator(behaviorList);

	  while (Button.ENTER.isDown());
	  LCD.clear();
	  
	  arbitrator.start();
	}	
}

class Wander extends Thread implements Behavior {
	DifferentialPilot pilot;
	OdometryPoseProvider poseProvider;
	
	public Wander(DifferentialPilot pilot, OdometryPoseProvider poseProvider) {
		this.pilot = pilot;
		this.poseProvider = poseProvider;
	}
	
	boolean _suppressed = false;
	@Override
	public int takeControl() {
		return 50;
	}

	@Override
	public void action() {
		
		_suppressed = false;

		while(!_suppressed){

			pilot.travel(10, true);
			
			while(!_suppressed && pilot.isMoving()) {
				Thread.yield();
			}

		}

		
	}

	@Override
	public void suppress() {
		_suppressed = true;// standard practice for suppress methods 		
	}
	
}

class AvoidEdge extends Thread implements Behavior
{
  private boolean _suppressed = false;
  private BlackWhiteSensor ls; 
  private DifferentialPilot pilot;
  private int lightValue;
  private int edgeCount;
  private boolean isRotating = false;

  public AvoidEdge(DifferentialPilot pilot, BlackWhiteSensor ls)
  {
	  this.ls = ls;
	  this.pilot = pilot;
    this.setDaemon(true);
    this.start();
  }
  
  public void run()
  {
		while (true) {
			if(ls.black() && !isRotating){
				edgeCount++;
			}
			if(ls.white() && edgeCount > 100)
			{
				edgeCount = 0;
			}
			
			//LCD.clear();
			//LCD.drawString("EdgeCount: " + edgeCount, 0, 1);
			Delay.msDelay(10);
			
		}
  }

  public int takeControl()
  {
	    if(ls.white() && edgeCount > 10 && edgeCount < 100) {
	    	edgeCount = 0;
	    	return 150;
	    }
	  return 0;
  }

  public void suppress()
  {
    _suppressed = true;// standard practice for suppress methods  
  }

  public void action()
  {
    _suppressed = false;
    
    isRotating = true;
    pilot.rotate(180);
    isRotating = false;
    /*
    LCD.drawString("LightValue: " + lightValue, 0, 1);
    LCD.drawString("Threshold: " + ls.getThreshold(), 0, 2);
    LCD.drawString("Is black: " + ls.black(), 0, 3);
    LCD.drawString("Is white: " + ls.white(), 0, 4);
    */
  }
}

class Exit implements Behavior
{
  private boolean _suppressed = false;

  public int takeControl()
  {
    if ( Button.ESCAPE.isPressed() )
    	return 200;
    return 0;
  }

  public void suppress()
  {
    _suppressed = true;// standard practice for suppress methods  
  }

  public void action()
  {
    System.exit(0);
  }
}

class AvoidObject implements Behavior {
	
	private UltrasonicSensor ultrasonicSensor;
	private DifferentialPilot pilot;
	private OdometryPoseProvider poseProvider;
	private int objDistance = 20;
	private int ortoTravelDist = 20;
	private int travelDist = 30;
	private int rightTurn = -90;
	private int leftTurn = 90;
	private boolean _suppressed = false;

	public AvoidObject(DifferentialPilot pilot, OdometryPoseProvider poseProvider, UltrasonicSensor ultrasonicSensor)
	  {
		  this.ultrasonicSensor = ultrasonicSensor;
		  this.pilot = pilot;
		  this.poseProvider = poseProvider;
	  }
	

	@Override
	public int takeControl() {
		int distance = readDistance();
		

		LCD.clear();
		LCD.drawString("read: " + distance , 0, 0);
		
		if(distance < objDistance) {
			
			return 100;
			
		}
		
		return 0;
	}

	@Override
	public void action() {
		int currentDistance = readDistance();
		
		Pose initPose = poseProvider.getPose();
		float initX = initPose.getX();
		float initY = initPose.getY();
		
		while(currentDistance < objDistance){
			pilot.rotate(leftTurn);
			pilot.travel(ortoTravelDist);
			pilot.rotate(rightTurn);
			
			currentDistance = readDistance();
		}
		
		Pose newPose = poseProvider.getPose();
		float newX = newPose.getX();
		float newY = newPose.getY();
		
		pilot.travel(travelDist);
		pilot.rotate(rightTurn);
			
		if(readDistance() > objDistance){
			pilot.travel(ortoTravelDist);
		}else{
			pilot.rotate(leftTurn);
		}
		
	}

	@Override
	public void suppress() {
		_suppressed = true;// standard practice for suppress methods
		
	}
	
	private int readDistance() {
		
		int[] distances = new int[5];
		
        for(int i = 0;i < 5;i++) {
        	distances[i] = ultrasonicSensor.getDistance();
            Delay.msDelay(1);
        }
        
        Arrays.sort(distances);
        
        return distances[2];
		
	}
}
