package lesson5;
import lejos.nxt.BasicMotorPort;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroSensor;
import lesson3.DataLogger;

/**
 * Test of Gyro Sensor
 * Records the minimum, maximum and current values
 * 
 * @author Lawrie Griffiths
 */
public class GyroTest2 
{	
	
    // PID constants
    private double KP = 400;
    private double KI = 0;
    private double KD = 200;
    private int SCALE = 18;
    private int TP = 100;

    private static GyroSensor gyro;
    
    // Global vars:
    int prev_error;
    float int_error;

    private static double EMAOFFSET = 0.005; 
    private float gyroSpeed;
    private float gyroAngle;
    private float gOffset = 0;
    private float gAngleGlobal = 0;
    private double tInterval = 0.003; //Maybe we should measure this
    
	public static void main(String[] args) throws Exception
	{	
	    gyro = new GyroSensor(SensorPort.S2);
	    gyro.setOffset(617);
	    
		GyroTest2 sej = new GyroTest2();
		sej.pidControl();
	}
	
    public void pidControl() 
    {
		while (!Button.ESCAPE.isDown()) 
		{
            getGyroData();

			// Proportional Error:
			float error = gyroAngle;
			// Adjust far and near light readings:
			if (error < 0) error = (int)(error * 1.8F);
			
			// Integral Error:
			int_error = ((int_error + error) * 2)/3;
			
			// Derivative Error:
			int deriv_error = (int) (error - prev_error);
			prev_error = (int) error;
			
			int pid_val = (int)(KP * error + KI * int_error + KD * deriv_error) / SCALE;
			
			if (pid_val > 100)
				pid_val = 100;
			if (pid_val < -100)
				pid_val = -100;

			// Power derived from PID value:
			int power = Math.abs(pid_val);
			power = 55 + (power * TP) / 100; // NORMALIZE POWER

			if (pid_val > 0) {
				MotorPort.B.controlMotor(power, BasicMotorPort.FORWARD);
				MotorPort.C.controlMotor(power, BasicMotorPort.FORWARD);
			} else {
				MotorPort.B.controlMotor(power, BasicMotorPort.BACKWARD);
				MotorPort.C.controlMotor(power, BasicMotorPort.BACKWARD);
			}
		}

		Motor.B.stop();
		Motor.C.stop();
		
    }
	
    void getGyroData() {
        
        float gyroRaw;

        gyroRaw = gyro.readValue(); // Replace this line?
        
        LCD.drawString("Raw: " + gyroRaw, 0, 0);
        
        gOffset = (float) (EMAOFFSET * gyroRaw + (1-EMAOFFSET) * gOffset);

        gyroSpeed = gyroRaw - gOffset;
        
        LCD.drawString("Offset: " + gOffset, 0, 1);
        
        gAngleGlobal += gyroSpeed*tInterval;
        
        gyroAngle = gAngleGlobal;
        
        LCD.drawString("Angle: " + gyroAngle, 0, 2);

      }

}