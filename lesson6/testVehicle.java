package lesson6;
	
import lejos.nxt.Button;

public class testVehicle
{
	public static void main(String [] args) throws Exception
    {
    	while (!Button.ESCAPE.isDown())
        {
        	Car.forward(100, -100);
        	Thread.sleep(100);
    	}
    }
}