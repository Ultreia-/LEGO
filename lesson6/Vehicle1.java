import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

public class Vehicle1
{
	private static SensorPort soundSensor = SensorPort.S1;
	
	private static int threshold = 500;
    private int count = 0;

	private static int map(int x, int in_min, int in_max, int out_min, int out_max)
    {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

    public Vehicle1()
    {
        mainLoop();
    }

    private void mainLoop()
    {
        soundSensor.setTypeAndMode(SensorPort.TYPE_SOUND_DB, SensorPort.MODE_RAW);

        int soundLevel;
        int speed1;
        int speed2;

        while (!Button.ESCAPE.isDown())
        {
            //DIrection = left - right
            soundLevel = 1023 - soundSensor.readRawValue();

            speed1 = map(soundLevel, 0, 1023, 60, 100);
            speed2 = map(soundLevel, 0, 1023, 0, -100);

            LCD.drawString("Raw: " + soundLevel, 0, 0);
            LCD.drawString("Speed: " + speed1, 0, 1);

            checkThresholdAndDance(soundLevel, speed1, speed1);
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
        Vehicle1 v = new Vehicle1();
    }

	private void checkThresholdAndDance(int soundlevel, int speed1, int speed2) {

		if(soundlevel > threshold)
        {
			dance(speed1);
		}
        else
        {
			Car.forward(speed1, speed2);
		}
	}

	private void dance(int speed)
    {
		if(count < 4) {

			Car.turnRight(speed);

		} else if (count < 8) {

			Car.turnLeft(speed);

		}

		count++;

		if(count == 9) count = 0;

        try
        {
            Thread.sleep(100);
        }
        catch (Exception e)
        {
            //ignore
        }
	}

}
