import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;

public class Vehicle2
{
    private static SensorPort lightPort1 = SensorPort.S1;
    private static SensorPort lightPort2 = SensorPort.S2;

    private LightSensor lightSensorLeft = new LightSensor(lightPort2, false);
    private LightSensor lightSensorRight = new LightSensor(lightPort1, false);

	private static int threshold = 500;
    private int count = 0;

	private static int map(int x, int in_min, int in_max, int out_min, int out_max)
    {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

    public Vehicle2()
    {
        mainLoop();
    }

    private void mainLoop()
    {
        int lightLevelRight = 0;
        int lightLevelLeft = 0;

        int moterRightSpeed = 0;
        int moterLeftSpeed = 0;

        while (!Button.ESCAPE.isDown())
        {
            lightLevelRight = lightSensorRight.readNormalizedValue();
            lightLevelLeft = lightSensorLeft.readNormalizedValue();

            moterRightSpeed = map(lightLevelRight, 145, 890, 100, 40);
            moterLeftSpeed = map(lightLevelLeft, 145, 890, 100, 40);

            Car.forward(moterLeftSpeed, moterRightSpeed);
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

	public static void main(String [] args) throws Exception
    {
        Vehicle2 v = new Vehicle2();
    }
}
