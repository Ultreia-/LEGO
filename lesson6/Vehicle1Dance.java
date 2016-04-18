import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

public class Vehicle1Dance
{
	private static SensorPort soundSensor = SensorPort.S1;
	
	private static int THRESHOLD = 400;
    private int COUNT = 0;

	private static int map(int x, int in_min, int in_max, int out_min, int out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

    public Vehicle1Dance()
    {
        mainLoop();
    }

    private void mainLoop() {
        //Is This correct?!
        soundSensor.setTypeAndMode(SensorPort.TYPE_SOUND_DB, SensorPort.MODE_RAW);

        int soundLevel;
        int speed1;
        int speed2;

        while (!Button.ESCAPE.isDown()) {
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

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            //ignore
        }
    }

	public static void main(String [] args) throws Exception
    {
        Vehicle1Dance v = new Vehicle1Dance();
    }

	private void checkThresholdAndDance(int soundlevel, int speed1, int speed2) {

		if(soundlevel > THRESHOLD)
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
		if(COUNT < 4) {

			Car.turnRight(speed);

		} else if (COUNT < 8) {

			Car.turnLeft(speed);

		}

		COUNT++;

		if(COUNT == 9) {

			COUNT = 0;
		}

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            //ignore
        }
	}

}
