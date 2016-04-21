import lejos.nxt.button;
import lejos.nxt.lcd;
import lejos.nxt.sensorport;

public class vehicle1dance
{
	private static sensorport soundsensor = sensorport.s1;
	
	private static int threshold = 400;
    private int count = 0;

	private static int map(int x, int in_min, int in_max, int out_min, int out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

    public vehicle1dance()
    {
        mainloop();
    }

    private void mainloop() {
        //is this correct?!
        soundsensor.settypeandmode(sensorport.type_sound_db, sensorport.mode_raw);

        int soundlevel;
        int speed1;
        int speed2;

        while (!button.escape.isdown()) {
            //direction = left - right
            soundlevel = 1023 - soundsensor.readrawvalue();

            speed1 = map(soundlevel, 0, 1023, 60, 100);
            speed2 = map(soundlevel, 0, 1023, 0, -100);

            lcd.drawstring("raw: " + soundlevel, 0, 0);
            lcd.drawstring("speed: " + speed1, 0, 1);

            checkthresholdanddance(soundlevel, speed1, speed1);
        }

        car.stop();
        lcd.clear();
        lcd.drawstring("program stopped", 0, 0);

        try {
            thread.sleep(2000);
        } catch (exception e) {
            //ignore
        }
    }

	public static void main(string [] args) throws exception
    {
        vehicle1dance v = new vehicle1dance();
    }

	private void checkthresholdanddance(int soundlevel, int speed1, int speed2) {

		if(soundlevel > threshold)
        {
			dance(speed1);
		}
        else
        {
			car.forward(speed1, speed2);
		}
	}

	private void dance(int speed)
    {
		if(count < 4) {

			car.turnright(speed);

		} else if (count < 8) {

			car.turnleft(speed);

		}

		count++;

		if(count == 9) {

			count = 0;
		}

        try {
            thread.sleep(100);
        } catch (exception e) {
            //ignore
        }
	}

}
