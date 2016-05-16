package lesson9;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.util.Delay;

public class Turn {
	public static void main(String[] args) {
		Button.waitForAnyPress();
		Delay.msDelay(3000);
		
		Motor.A.setSpeed(700);
		Motor.B.setSpeed(700);
		while(true) {
		Motor.A.forward();
		Motor.B.backward();
		}
	}
}
