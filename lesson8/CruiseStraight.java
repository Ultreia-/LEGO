package lesson8;

public class CruiseStraight extends Thread
{
    private SharedCar car;
    private int power = 100;
    
    public CruiseStraight(SharedCar car)
    {
    	this.car = car;
    }
    
	public void run() 
    {				       
        while (true)
        {
        	/*  Drïve forward */
			car.forward(power, power); 
        }
    }
}
