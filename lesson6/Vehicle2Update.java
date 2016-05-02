package lesson6;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;

public class Vehicle2Update
{
    private static SensorPort lightPort1 = SensorPort.S1;
    private static SensorPort lightPort2 = SensorPort.S2;

    private LightSensor lightSensorLeft = new LightSensor(lightPort2, false);
    private LightSensor lightSensorRight = new LightSensor(lightPort1, false);

	private int MAX_LIGHT, MIN_LIGHT ;
    

	private static int map(int x, int in_min, int in_max, int out_min, int out_max)
    {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

    public Vehicle2Update()
    {
        MIN_LIGHT = (lightSensorRight.readNormalizedValue() + lightSensorLeft.readNormalizedValue()) / 2; 
        MAX_LIGHT = MIN_LIGHT + 1; //What a hack

        mainLoop();
    }

    private void mainLoop()
    {
        int lightLevelRight = 0;
        int lightLevelLeft = 0;

        int motorRightSpeed = 0;
        int motorLeftSpeed = 0;

        while (!Button.ESCAPE.isDown())
        {
            lightLevelRight = lightSensorRight.readNormalizedValue();
            lightLevelLeft = lightSensorLeft.readNormalizedValue();

            motorRightSpeed = map(lightLevelRight, MIN_LIGHT, MAX_LIGHT, 100, 40);
            motorLeftSpeed = map(lightLevelLeft, MIN_LIGHT, MAX_LIGHT, 100, 40);

            checkAndUpdateMaxAndMin(lightLevelLeft, lightLevelRight);

            Car.forward(motorLeftSpeed, motorRightSpeed);
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


    private void checkAndUpdateMaxAndMin(int lightleft, int lightright) {


        if(lightleft > MAX_LIGHT) MAX_LIGHT = lightleft;
        if(lightright > MAX_LIGHT) MAX_LIGHT = lightright;
        if(lightleft < MIN_LIGHT) MIN_LIGHT = lightleft;
        if(lightright < MIN_LIGHT) MIN_LIGHT = lightright;

    }

	public static void main(String [] args) throws Exception
    {
        Vehicle2Update v = new Vehicle2Update();
    }
}
