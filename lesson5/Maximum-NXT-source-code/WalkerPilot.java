import java.util.ArrayList;
import lejos.nxt.Motor;
import lejos.robotics.navigation.*;
import lejos.util.Delay;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 19 - Walking and Navigation
 * Robot: Shambler
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class WalkerPilot implements RotateMoveController {
	
	WalkerPilot parent = this; // the inner class needs this reference
	TravelThread tt = null; // reference to most recent thread
	
	double GEAR_RATIO = 20.0/12.0; // rotation gear ratio
	
	// Motor constants, in degrees
	public int UP = -145; // Maximum vertical lift
	public int MAX_FORWARD = -140; // Amount to rotate for forward step
	public int FLOOR_BACKWARD = -19; // tacho where foot first hits floor
	public int FLOOR_FORWARD = -128; // tacho where foot lifts off floor
	public int SPREAD = FLOOR_FORWARD - FLOOR_BACKWARD; // degrees covered by one step
		
	private ArrayList<MoveListener> listeners= new ArrayList<MoveListener>();
	
	// Variable used to interrupt the current cycle of a travel movement
	boolean interrupt = false;
	
	public WalkerPilot() {
		Motor.A.setSpeed(70); // vertical lifter
		Motor.B.setSpeed(75); // lateral movement
		Motor.C.setSpeed(30); // rotation motor
	}
	
	public void backward() {
		travel(Double.NEGATIVE_INFINITY, true);
	}

	public void forward() {
		travel(Double.POSITIVE_INFINITY, true);
	}

	public double getMaxTravelSpeed() {
		return 0; // Not implemented
	}

	public double getTravelSpeed() {
		return 0;  // Not implemented
	}

	public boolean isMoving() {
		if(tt == null) {
			return false;
		} else if(tt.done) {
			return false;
		} else 
			return true;
	}

	public void setTravelSpeed(double speed) {
		// Not implemented
	}

	public void stop() {
		// Set interrupt flag.
		interrupt = true;
		// Wait until done is true
		if(tt != null) {
			while(!tt.done) {
				Delay.msDelay(100);
			}
		}
	}

	public void travel(double distance) {
		travel(distance, false);
	}

	public void travel(double distance, boolean immediateReturn) {
		tt = new TravelThread(distance);
		tt.start();
		if(!immediateReturn)
			while(!tt.done) {
				Delay.msDelay(100);
			}
	}

	public void addMoveListener(MoveListener m) {
		listeners.add(m);
	}

	public Move getMovement() {
		return null; // Not implemented
	}

	public double getRotateMaxSpeed() {
		return 0; // Not implemented
	}

	public double getRotateSpeed() {
		return 0; // Not implemented
	}

	public void rotate(double degrees) {
		rotate(degrees, false);
	}

	/** Note: immediateReturn is not implemented */ 
	public void rotate(double degrees, boolean immediateReturn) {
		
		// Notify listeners that rotate has stopped
		for(MoveListener ml:listeners) 
			ml.moveStarted(new Move(Move.MoveType.ROTATE, 0, (float)degrees, true), parent);
		
		double angle = degrees * GEAR_RATIO;
		Motor.C.rotate((int)angle);
		Motor.A.rotateTo(UP);
		Motor.C.rotateTo(0);
		Motor.A.rotateTo(0);
		
		// Notify listeners that rotate has stopped
		for(MoveListener ml:listeners) 
			ml.moveStopped(new Move(Move.MoveType.ROTATE, 0, (float)degrees, false), parent); 
	}

	public void setRotateSpeed(double arg0) {
		// Not implemented
	}

	private class TravelThread extends Thread {
		
		boolean done = false; // Used for blocking methods, such as travel(dist, true);
		
		private double original_dist;
		private double remaining_dist;
		private final double DIST_PER_STEP = 7.0; // cm
				
		TravelThread(double distance) {
			this.original_dist = distance;
			this.remaining_dist = distance;
		}
		
		public void run() {
			
			// notify listeners that move has started
			for(MoveListener ml:listeners) 
				ml.moveStarted(new Move(Move.MoveType.TRAVEL, (float)original_dist, 0, true), parent);
			
			int dist_tally = 0;
			
			boolean TRAVEL_FORWARD = remaining_dist > 0;
			
			for(;remaining_dist!=0;) {
				// Lift to let legs pass under if moving forward.
				if(TRAVEL_FORWARD) Motor.A.rotateTo(0); 
				else Motor.A.rotateTo(UP);
								
				int fwd_tacho = MAX_FORWARD;
				double step_distance = DIST_PER_STEP;
				if(Math.abs(remaining_dist) < DIST_PER_STEP) {
					double ratio = Math.abs(remaining_dist/DIST_PER_STEP);
					fwd_tacho = (int)(ratio * SPREAD) + FLOOR_BACKWARD;
					if(TRAVEL_FORWARD) step_distance = remaining_dist;
					else step_distance = -remaining_dist;
				}
				
				// Now only rotate proper portion of remaining. e.g. 3/7 * Tacho angle.
				Motor.B.rotateTo(fwd_tacho, true);
				while(!interrupt & Motor.B.isMoving()) {
					Delay.msDelay(150);
				}
				// This section calculates partial distance IF interrupted by stop().
				if(interrupt) {
					Motor.B.stop();
					int tacho = Motor.B.getTachoCount();
					Motor.A.rotateTo(UP);
					Motor.B.rotateTo(0);
					// Now calculate partial move.
					if(tacho < FLOOR_BACKWARD) {
						double partial = tacho - FLOOR_BACKWARD;
						double ratio = partial/SPREAD;
						double part_dist = ratio * DIST_PER_STEP;
						dist_tally += part_dist;
					}
					interrupt = false;
					break; // Break out of for-loop
				}
				
				if(TRAVEL_FORWARD) Motor.A.rotateTo(UP);
				else Motor.A.rotateTo(0);
				
				if(interrupt) break;
				Motor.B.rotateTo(0);
				
				// Subtract whatever this distance was, could be full 7 or partial.
				if(TRAVEL_FORWARD) {
					remaining_dist-= step_distance;
					dist_tally += step_distance;
				} else {
					remaining_dist-= -step_distance;
					dist_tally += -step_distance;
				}
				
				if(Math.abs(remaining_dist) < 0.5 && Math.abs(remaining_dist) > -0.5)
					remaining_dist = 0; // safer setting to 0 because sometimes extra digits remain
				
				System.out.println("REMAIN:" + remaining_dist);
				
				if(interrupt) break;
			}
			Motor.A.rotateTo(0);
			
			// Notify listeners that move has stopped
			for(MoveListener ml:listeners) 
				ml.moveStopped(new Move(Move.MoveType.TRAVEL, (float)dist_tally, 0, false), parent);
						
			done = true;
		}
	}
}