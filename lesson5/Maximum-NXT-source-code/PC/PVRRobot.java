import java.util.ArrayList;
import lejos.nxt.*;
import lejos.util.Delay;
import net.java.games.input.*;
import net.java.games.input.Component.Identifier;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 12 - Game Controllers
 * Robot: Snowcat
 * Platform: PC
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class PVRRobot {
	
	public static int HEART_BEAT = 100; // 100ms = 10 times per second
	public static int MAX_SPEED = 700;
	public static double GEAR_REDUCTION = 12.0/20.0; // 12 tooth:20 tooth
	//public static double GEAR_REDUCTION = 1; // No gear reduction
	
	static Component buttonPauseMode;
	static Controller c;
	
	public static void main(String[] args) {
	
		System.out.println("JInput version " + Version.getVersion());
		ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
		Controller[] controllers = ce.getControllers();
		c = null;
		
		for(int i=0;i<controllers.length;i++) {
			Controller.Type type = controllers[i].getType();
			if (type == Controller.Type.GAMEPAD | type == Controller.Type.STICK)
				c = controllers[i];
		}
		
		Component rightStickY = c.getComponent(Identifier.Axis.RY); // right stick control
		Component leftStickY = c.getComponent(Identifier.Axis.Y); // left stick control
		Component buttonStart = c.getComponent(Identifier.Button._7); // start button
		Component buttonReplay = c.getComponent(Identifier.Button._0); // A(Xbox) or X(PS3) button
		Component buttonClear = c.getComponent(Identifier.Button._1); // B(Xbox) or O(PS3) button
		buttonPauseMode = c.getComponent(Identifier.Button._3); // Y(Xbox) or triangle(PS3) button
		
		ArrayList <Double> leftMoves = new ArrayList<Double>();
		ArrayList <Double> rightMoves = new ArrayList<Double>();
		
		Sound.beepSequenceUp();
		
		// Keep repeating until start is pressed:
		while(buttonStart.getPollData() == 0) {
			c.poll(); // refresh button data
			
			// Replay all moves up to this point:
			if(buttonReplay.getPollData() != 0)
				replayMoves(leftMoves, rightMoves);
			
			// Clear all the moves:
			if(buttonClear.getPollData() != 0) {
				leftMoves.clear();
				rightMoves.clear();
				Sound.beep();
			}
			
			// Pause mode: stops recording, can rewind and advance moves
			if(buttonPauseMode.getPollData() != 0) {
				Sound.beepSequenceUp();
				pauseMode(leftMoves, rightMoves);
			}
			
			double left = -leftStickY.getPollData();
			double right = -rightStickY.getPollData();
			
			leftMoves.add(left);
			rightMoves.add(right);
			
			playMove(left, right, HEART_BEAT);
			
		}
		Sound.beepSequence();
		Motor.B.flt();
		Motor.C.flt();
		System.out.println("Final size: " + leftMoves.size());
		Delay.msDelay(500); // wait for send before exiting
	}
	
	static void playMove(double left, double right, long heartBeat) {
		Motor.B.setSpeed((int)Math.abs(MAX_SPEED * left));
		Motor.C.setSpeed((int)Math.abs(MAX_SPEED * right * GEAR_REDUCTION));
		if(left>0) Motor.B.forward();
		else Motor.B.backward();
		if(right>0) Motor.C.forward();
		else Motor.C.backward();
					
		try {Thread.sleep(heartBeat);} catch (InterruptedException e) {}
	}
	
	static void replayMoves(ArrayList <Double> leftMoves, ArrayList <Double> rightMoves) {
		Sound.beep();
		for(int i=0;i<leftMoves.size();i++) {
			playMove(leftMoves.get(i), rightMoves.get(i), HEART_BEAT);
		}
		Sound.beep();
	}
	
	static void pauseMode(ArrayList <Double> leftMoves, ArrayList <Double> rightMoves) {
		while(buttonPauseMode.getPollData() !=0) {
			c.poll(); // wait for user to release button
		}
		
		Component buttonRewindAdvance = c.getComponent(Identifier.Axis.Z); // trigger buttons (both are same axis)
		int pointer = leftMoves.size() - 1; // current location of pointer
		
		while(buttonPauseMode.getPollData() ==0) {
			c.poll();
			double zAxis = buttonRewindAdvance.getPollData();
			
			if(zAxis > 0.1) { // Rewind
				pointer--;
				if(pointer <= 0) 
					pointer = 0;
				else 
					playMove(-leftMoves.get(pointer), -rightMoves.get(pointer), HEART_BEAT);
			} else if(zAxis < -0.1) { // Fast forward
				pointer++;
				if(pointer >= leftMoves.size()) 
					pointer = leftMoves.size() - 1;
				else	
					playMove(leftMoves.get(pointer), rightMoves.get(pointer), HEART_BEAT);
			} else {
				playMove(0,0,HEART_BEAT);
			}
		}
		// Delete all moves after pointer:
		for(;pointer<leftMoves.size();pointer++) {
			leftMoves.remove(pointer);
			rightMoves.remove(pointer);
		}
			
		Sound.beep();
		while(buttonPauseMode.getPollData() !=0) {
			c.poll(); // wait for user to release button
		}
	}
}