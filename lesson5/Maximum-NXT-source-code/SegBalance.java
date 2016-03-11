import lejos.nxt.*;
import lejos.nxt.addon.*;
import lejos.robotics.*;
import lejos.robotics.navigation.*;

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
public class SegBalance {

	public static void main(String[] args) throws Exception {
		EncoderMotor left = new NXTMotor(MotorPort.B);
		EncoderMotor right = new NXTMotor(MotorPort.C);
		
		// Code for HiTechnic/LEGO Gyro Sensor:
		Gyroscope g = new GyroSensor(SensorPort.S1);
		
		// Code for dIMU Gyro Sensor by Dexter Industries:
		//SensorPort.S1.i2cEnable(I2CPort.HIGH_SPEED);
		//DIMUGyro dimu = new DIMUGyro(SensorPort.S1);
		//Gyroscope g = dimu.getAxis(DIMUGyro.Axis.Z);
				
		// Code for single Cruizecore gyro: 
		//SensorPort.S1.i2cEnable(I2CPort.HIGH_SPEED);
		//Gyroscope g = new CruizcoreGyro(SensorPort.S1);
				
		// IMPORTANT: Set wheel size below according to your kit version.
		//Segoway robot = new Segoway(left, right, g, SegowayPilot.WHEEL_SIZE_NXT1);
		Segoway robot = new Segoway(left, right, g, SegowayPilot.WHEEL_SIZE_NXT2);
		
		Button.ENTER.waitForPressAndRelease();
	}
}