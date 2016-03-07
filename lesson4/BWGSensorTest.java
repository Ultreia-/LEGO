package lesson4;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

public class BWGSensorTest {

	public static void main(String[] args) throws Exception{
		ThreeColorSensor bwg = new ThreeColorSensor(SensorPort.S3);
		
		bwg.calibrate();
		
		while(! Button.ESCAPE.isDown()) {
			LCD.clear();
			LCD.drawString("Currently Parked On: ", 0, 0);
			if(bwg.black()){
			   LCD.drawString("Black", 0, 1);
			}
			else if(bwg.white()){
				LCD.drawString("White", 0, 1);
			}
			else if(bwg.green()){
				LCD.drawString("Green", 0, 1);
			}
			Thread.sleep(300);
		}
	}

}
