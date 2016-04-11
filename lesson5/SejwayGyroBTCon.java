package lesson5;

import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.comm.*;
import java.io.*;

/**
 * A controller for a self-balancing Lego robot with a light sensor
 * on port 2. The two motors should be connected to port B and C.
 *
 * Building instructions in Brian Bagnall: Maximum Lego NXTBuilding 
 * Robots with Java Brains</a>, Chapter 11, 243 - 284
 * 
 * @author Brian Bagnall
 * @version 26-2-13 by Ole Caprani for leJOS version 0.9.1
 */

public class SejwayGyroBTCon 
{
    // PID constants
    private double KP = 35;
    private double KI = 0;
    private double KD = 35;
    private int SCALE = 18;
    private int TP = 45;

    // Global vars:
    int prev_error;
    float int_error;
	
    GyroSensor gyro = new GyroSensor(SensorPort.S2);

    private static double EMAOFFSET = 0.0005; 
    private float gyroSpeed;
    private float gyroAngle;
    private float gOffset = 610;
    private float gAngleGlobal = 0;
    private double tInterval = 0.01; //Maybe we should measure this
	
    private String connected    = "Connected";
    private String waiting    = "Waiting...";
    private String closing    = "Closing...";

    private int steps = 0;

    private BTConnection btc;
    private DataInputStream dis;
    private DataOutputStream dos;


    public SejwayGyroBTCon() 
    {
    	
        LCD.drawString(waiting,0,0);

        btc = Bluetooth.waitForConnection();

        LCD.clear();
        LCD.drawString(connected,0,0);

        dis = btc.openDataInputStream();
    }
	
    public void getBalancePos() 
    {
        // Wait for user to balance and press orange button
        while (!Button.ENTER.isDown())
        {
            
        }
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

			if(Button.LEFT.isDown()) refresh();
		}

		Motor.B.stop();
		Motor.C.stop();
    }


   
    public static void main(String[] args) 
    {
        SejwayBTColor sej = new SejwayBTColor();
        sej.getBalancePos();
        sej.pidControl();
        sej.shutDown();
    }


	
    public void shutDown()
    {
		try{
			
    	dis.close();

        Thread.sleep(100); // wait for data to drain
        btc.close();

		Motor.B.stop();
		Motor.C.stop();
		}
		
		catch(Exception e){
	        btc.close();

			Motor.B.stop();
			Motor.C.stop();

		}
        // Shut down light sensor, motors
        Motor.B.flt();
        Motor.C.flt();
    }
	

    void getGyroData() {
      
      float gyroRaw;

      gyroRaw = gyro.readValue(); // Replace this line?

      gOffset = (float) (EMAOFFSET * gyroRaw + (1-EMAOFFSET) * gOffset);
      gyroSpeed = gyroRaw - gOffset;

      gAngleGlobal += gyroSpeed*tInterval;
      
      gyroAngle = gAngleGlobal;

    }


    public void refresh()
    {
        try 
        {
        
            Motor.B.flt();
            Motor.C.flt();

            //LCD.drawString("Waiting for GO",0,0);

            KP = dis.readInt();
            //LCD.clear();
            //LCD.drawInt((int)KP,7,0,1);
            //LCD.refresh();
            KI = dis.readInt();
            //LCD.drawInt((int)KI,7,0,2);
            //LCD.refresh();
            KD = dis.readInt();
            //LCD.drawInt((int)KD,7,0,3);
            //LCD.refresh();
            
            TP = dis.readInt();
            //LCD.drawInt((int)TP,7,0,4);
            //LCD.refresh();
            //dos.flush();
            
            getBalancePos();


        }
        catch (Exception e)
        {}
    }
}