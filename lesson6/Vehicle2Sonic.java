import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import java.util.Arrays;
import lejos.util.Delay;


public class Vehicle2Sonic
{
    private static SensorPort sonicPort3 = SensorPort.S3;
    private static SensorPort sonicPort4 = SensorPort.S4;

    private UltrasonicSensor sonicSensorLeft = new UltrasonicSensor(sonicPort3);
    private UltrasonicSensor sonicSensorRight = new UltrasonicSensor(sonicPort4);

	private static int map(int x, int in_min, int in_max, int out_min, int out_max)
    {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

    public Vehicle2Sonic()
   
    {
        mainLoop();
    }

    private void mainLoop()
    {
        int sonicLevelRight = 0;
        int sonicLevelLeft = 0;

        int motorRightSpeed = 0;
        int motorLeftSpeed = 0;

        int[] distancesRight = new int[8];
        int[] distancesLeft = new int[8];

        int distanceLeft;
        int distanceRight;

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

            motorRightSpeed = map(distanceRight, 0, 255, 40, 100);
            motorLeftSpeed = map(distanceLeft, 0, 255, 40, 100);

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

	public static void main(String [] args) throws Exception
    {
        Vehicle2Sonic v = new Vehicle2Sonic();
    }
}
