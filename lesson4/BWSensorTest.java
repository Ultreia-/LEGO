package lesson4;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

public class BWSensorTest {

	public static void main(String[] args) throws Exception{
		BlackWhiteSensor bw = new BlackWhiteSensor(SensorPort.S3);
		
		bw.calibrate();
		
		while(! Button.ESCAPE.isDown()) {
			LCD.clear();
			LCD.drawString("Currently Parked On: ", 0, 0);
			if(bw.black()){
			   LCD.drawString("Black", 0, 1);
			}
			else if(bw.white()){
				LCD.drawString("White", 0, 1);
			}
			Thread.sleep(300);
		}
	}

}
