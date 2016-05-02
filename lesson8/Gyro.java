package lesson8;

/*
 * Cruise behavior, p. 303 in
 * Jones, Flynn, and Seiger: 
 * "Mobile Robots, Inspiration to Implementation", 
 * Second Edition, 1999.
 */

class Gyro extends Thread
{
    private SharedCar car;
    private int power = 70;

    GyroSensor gyro = new GyroSensor(SensorPort.S1);

    Queue valueQueue = new Queue();

    Private int delta = 0;

    public Cruise(SharedCar car)
    {
    	this.car = car;
    }
    
	public void run() 
    {
        float firstValue = gyro.readValue();
     

        while (true)
        {
            float value = gyro.readValue();
        	/*  Drive forward */
			// car.forward(power, power); 
        }
    }
}
	

