
import lejos.nxt.*;
import lejos.util.Delay;
/*
 * Escape behavior
 */

class Escape extends Thread
{
	private SharedCar car = new SharedCar();

	private int power = 70, ms = 500;
	TouchSensor bumperRight = new TouchSensor(SensorPort.S2);
	TouchSensor bumperLeft = new TouchSensor(SensorPort.S3);
	
	int frontDistance, leftDistance, rightDistance;
	int stopThreshold = 30;

	public Escape(SharedCar car)
	{
		this.car = car;
	}
	
	public void run() 
	{
		while (true)
		{
			boolean bLeft = bumperLeft.isPressed();
			boolean bRight = bumperRight.isPressed();
	
			while(!(bRight || bLeft))
			{
				car.noCommand();

				bLeft = bumperLeft.isPressed();
				bRight = bumperRight.isPressed();
			}

			car.stop();
			Delay.msDelay(ms);

			car.backward(power, power);
			Delay.msDelay(ms*2);
			car.forward(-power, power);
			Delay.msDelay((int)(ms*3.4));

			// if(bLeft)
			// {

			// }
			// else if(bRight)
			// {

			// }
		}
	}
}

