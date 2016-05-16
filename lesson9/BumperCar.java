package lesson9;

import java.util.Arrays;

import lejos.nxt.*;
import lejos.util.Delay;

/**
 * Demonstration of the Behavior subsumption classes.
 * 
 * Requires a wheeled vehicle with two independently controlled motors connected
 * to motor ports A and C, and a touch sensor connected to sensor port 1 and an
 * ultrasonic sensor connected to port 3;
 * 
 * @author Brian Bagnall and Lawrie Griffiths, modified by Roger Glassey
 *
 *         Uses a new version of the Behavior interface and Arbitrator with
 *         integer priorities returned by takeCaontrol instead of booleans.
 * 
 *         Exit behavior inserted, local distance sampling thread and backward
 *         drive added in DetectWall by Ole Caprani, 23-4-2012
 */
public class BumperCar {

	public static void main(String[] args) {
		
		int fieldwidth = 100;
		
		LightSensor lightsensor = new LightSensor(SensorPort.S1);

		TouchSensor touchsensor = new TouchSensor(SensorPort.S4);

		// Calibrate

		LCD.drawString("Pick White", 0, 0);

		int whitelight = readLight(lightsensor);

		LCD.clear();
		LCD.drawString("Pick Black", 0, 0);

		int blacklight = readLight(lightsensor);

		LCD.clear();

		Behavior b1 = new DriveForward();
		Behavior b2 = new AvoidLine(lightsensor, whitelight, blacklight);
		Behavior b3 = new FollowOthers(fieldwidth);
		Behavior b4 = new Exit();
		Behavior b5 = new HookEnemy(touchsensor);
		
		Behavior[] behaviorList = { b1, b2, b3, b4, b5 };
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		LCD.drawString("Bumper Car", 0, 1);

		Button.waitForAnyPress();
		
		arbitrator.start();
	}

	public static int readLight(LightSensor lightsensor) {

		int reading = 0;

			Delay.msDelay(500);
		
			while (!Button.ENTER.isDown()) {

				reading = lightsensor.getNormalizedLightValue();

			}

			
		return reading;
	}

}

class DriveForward implements Behavior {

	private boolean _suppressed = false;

	public int takeControl() {
		return 2; // this behavior always wants control.
	}

	public void suppress() {
		_suppressed = true;// standard practice for suppress methods
	}

	public void action() {
		Motor.A.setSpeed(200);
		Motor.B.setSpeed(200);

		
		_suppressed = false;
		Motor.A.forward();
		Motor.B.forward();
		LCD.drawString("Drive forward", 0, 2);
		while (!_suppressed) {
			Thread.yield(); // don't exit till suppressed
		}
		Motor.A.stop(); // not strictly necessary, but good programming practice
		Motor.B.stop();
		LCD.drawString("Drive stopped", 0, 2);
	}
}


class FollowOthers extends Thread implements Behavior {

	private UltrasonicSensor ultraSonicSensor;
	private int fieldWith; ; //Representing a reading hitting an object in the other end of the field
	private boolean _supressed = false;
	
	public FollowOthers(int fieeldwidth) {
	
		this.ultraSonicSensor = new UltrasonicSensor(SensorPort.S2);
		this.fieldWith = fieeldwidth;
	
	}
	
	@Override
	public int takeControl() {
		
		int distance = readDistance();
		

		LCD.clear();
		LCD.drawString("read: " + distance , 0, 0);
		
		if(distance < fieldWith) {
			
			return 60;
			
		}
		
		return 0;
		
	}

	@Override
	public void action() {
		
		turn180();
		
	}
	
	private void turn180() {
		
		_supressed = false;
		
		Motor.A.setSpeed(700);
		Motor.B.setSpeed(700);
		
		Motor.A.forward();
		Motor.B.backward();
		
		int count = 0;
		
		while(count < 450 && !_supressed) {
			Delay.msDelay(1);	
			count ++;
			
		}
		
		Motor.B.stop();
		Motor.A.stop();
		
		
	}
	

	@Override
	public void suppress() {
		
		_supressed = true;
		
		
	}
	
	
	private int readDistance() {
		
		int[] distances = new int[5];
		
        for(int i = 0;i < 5;i++) {
        	distances[i] = ultraSonicSensor.getDistance();
            Delay.msDelay(1);
        }
        
        Arrays.sort(distances);
        
        return distances[2];

		
	}
}

class HookEnemy extends Thread implements Behavior {

	private TouchSensor touchSensor;
	private boolean _supressed = true;
	
	
	public HookEnemy(TouchSensor touchsensor) {
		this.touchSensor = touchsensor;
	}


	@Override
	public int takeControl() {
		if(touchSensor.isPressed()) {
			
			return 70;
			
		}
		
		return 0;
	}
	

	
	
	@Override
	public void action() {
		
		_supressed = false;
		
		
		Motor.A.setSpeed(700);		
		Motor.B.setSpeed(700);
			
		Motor.A.forward();
		Motor.B.forward();
		
		
		Motor.C.setSpeed(200);
		
		Motor.C.backward();
		
		
		int count = 0;
		while(count < 200 && !_supressed) {
			
			Thread.yield();
			Delay.msDelay(1);
			count ++;
			
		}

			
		Motor.C.stop();
		
		Motor.A.setSpeed(300);		
		Motor.B.setSpeed(300);
				
	
		
	}

	@Override
	public void suppress() {
		_supressed = true;
		
		

		
	}
	
	
	
}

class AvoidLine extends Thread implements Behavior {

	private LightSensor lightSensor;
	private int lastReading;
	private int threshold;
	private boolean _suppressed;

	public AvoidLine(LightSensor lightsensor, int whitelight, int blacklight) {

		this.lightSensor = lightsensor;
		this.threshold = blacklight + ((whitelight - blacklight) / 2);

		lightSensor.setFloodlight(true);

	}

	@Override
	public int takeControl() {
		
		lastReading = lightSensor.getNormalizedLightValue();

		if (lastReading >= threshold) {

			return 100;

		}

		return 0;

	}

	@Override
	public void action() {

		_suppressed = false;
		
		Motor.C.setSpeed(700);
		Motor.C.forward();
		
		Delay.msDelay(100);
		
		
		Motor.C.stop();

		int count = 0;
		
		while(count < 700){
			Motor.A.backward();
			Motor.B.backward();
			Delay.msDelay(1);	
			count ++;
			
		}

	}

	@Override
	public void suppress() {

		_suppressed = true;

		Motor.A.stop();
		Motor.B.stop();

	}

}

class DetectWall extends Thread implements Behavior {
	private boolean _suppressed = false;
	private boolean active = false;
	private int distance = 255;

	public DetectWall() {
		touch = new TouchSensor(SensorPort.S1);
		sonar = new UltrasonicSensor(SensorPort.S3);
		this.setDaemon(true);
		this.start();
	}

	public void run() {
		while (true)
			distance = sonar.getDistance();
	}

	public int takeControl() {
		if (touch.isPressed() || distance < 25)
			return 100;
		if (active)
			return 50;
		return 0;
	}

	public void suppress() {
		_suppressed = true;// standard practice for suppress methods
	}

	public void action() {
		_suppressed = false;
		active = true;
		Sound.beepSequenceUp();

		// Backward for 1000 msec
		LCD.drawString("Drive backward", 0, 3);
		Motor.A.backward();
		Motor.C.backward();
		int now = (int) System.currentTimeMillis();
		while (!_suppressed && ((int) System.currentTimeMillis() < now + 1000)) {
			Thread.yield(); // don't exit till suppressed
		}

		// Stop for 1000 msec
		LCD.drawString("Stopped       ", 0, 3);
		Motor.A.stop();
		Motor.C.stop();
		now = (int) System.currentTimeMillis();
		while (!_suppressed && ((int) System.currentTimeMillis() < now + 1000)) {
			Thread.yield(); // don't exit till suppressed
		}

		// Turn
		LCD.drawString("Turn          ", 0, 3);
		Motor.A.rotate(-180, true);// start Motor.A rotating backward
		Motor.C.rotate(-360, true); // rotate C farther to make the turn
		while (!_suppressed && Motor.C.isMoving()) {
			Thread.yield(); // don't exit till suppressed
		}
		Motor.A.stop();
		Motor.C.stop();
		LCD.drawString("Stopped       ", 0, 3);
		Sound.beepSequence();
		active = false;

	}

	private TouchSensor touch;
	private UltrasonicSensor sonar;
}

class Exit implements Behavior {
	private boolean _suppressed = false;

	public int takeControl() {
		if (Button.ESCAPE.isPressed())
			return 200;
		return 0;
	}

	public void suppress() {
		_suppressed = true;// standard practice for suppress methods
	}

	public void action() {
		System.exit(0);
	}
}
