package lesson6;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import java.util.Arrays;
import lejos.util.Delay;
import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;
import java.lang.Math;


public class Vehicle3Bad
{

    private static SensorPort lightPort1 = SensorPort.S1;
    private static SensorPort lightPort2 = SensorPort.S2;

    private static SensorPort sonicPort3 = SensorPort.S3;
    private static SensorPort sonicPort4 = SensorPort.S4;

    private UltrasonicSensor sonicSensorLeft = new UltrasonicSensor(sonicPort3);
    private UltrasonicSensor sonicSensorRight = new UltrasonicSensor(sonicPort4);

    private LightSensor lightSensorLeft = new LightSensor(lightPort2, false);
    private LightSensor lightSensorRight = new LightSensor(lightPort1, false);

    private int lightLevelRight = 0;
    private int lightLevelLeft = 0;

    private int sonicLevelRight = 0;
    private int sonicLevelLeft = 0;

    private int motorRightSpeedSonic = 0;
    private int motorLeftSpeedSonic = 0;
    private int motorRightSpeedLight = 0;
    private int motorLeftSpeedLight = 0;


    private int[] distancesRight = new int[8];
    private int[] distancesLeft = new int[8];

    private int distanceLeft;
    private int distanceRight;

    private int MAX_LIGHT, MIN_LIGHT ;
    


	private static int map(int x, int in_min, int in_max, int out_min, int out_max)
    {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

    public Vehicle3Bad()
   
    {

        MIN_LIGHT = (lightSensorRight.readNormalizedValue() + lightSensorLeft.readNormalizedValue()) / 2; 
        MAX_LIGHT = MIN_LIGHT + 1; //What a hack

        mainLoop();
    }

    private void mainLoop()
    {
    

        while (!Button.ESCAPE.isDown())
        {
            for(int i = 0;i < 8;i++)
            {
                distancesLeft[i] = sonicSensorLeft.getDistance();
                distancesRight[i] = sonicSensorRight.getDistance();

                Delay.msDelay(50);
            }

            Arrays.sort(distancesRight);
            Arrays.sort(distancesLeft);
            
            distanceLeft = distancesLeft[3];
            distanceRight = distancesRight[3];

            LCD.drawString("Left: " + distanceLeft, 0, 0);
            LCD.drawString("Right: " + distanceRight, 0, 1);


            lightLevelRight = lightSensorRight.readNormalizedValue();
            lightLevelLeft = lightSensorLeft.readNormalizedValue();



            motorRightSpeedLight = map(lightLevelRight, MIN_LIGHT, MAX_LIGHT, 40, 100);
            motorLeftSpeedLight = map(lightLevelLeft, MIN_LIGHT, MAX_LIGHT, 40, 100);

            motorRightSpeedSonic = map(distanceRight, 0, 255, 40, 100);
            motorLeftSpeedSonic = map(distanceLeft, 0, 255, 40, 100);


            checkAndUpdateMaxAndMin(lightLevelLeft, lightLevelRight);


           decideBehavior();
    
        }


        Car.stop();
        LCD.clear();
        LCD.drawString("Program stopped", 0, 0);

        try
        {
            Thread.sleep(2000);
        }
        catch (Exception e)
        {
            //ignore
        }
    }

    private void decideBehavior() {

        Car.backward(Math.abs(motorLeftSpeedLight - motorLeftSpeedSonic * 2), Math.abs(motorRightSpeedLight -  motorRightSpeedSonic * 2));


    }

    private void checkAndUpdateMaxAndMin(int lightleft, int lightright) {


        if(lightleft > MAX_LIGHT) MAX_LIGHT = lightleft;
        if(lightright > MAX_LIGHT) MAX_LIGHT = lightright;
        if(lightleft < MIN_LIGHT) MIN_LIGHT = lightleft;
        if(lightright < MIN_LIGHT) MIN_LIGHT = lightright;

    }


	public static void main(String [] args) throws Exception
    {
        Vehicle3Bad v = new Vehicle3Bad();
    }
}
