package lesson8;


public class CarCommand 
{
	public enum Command {
	    FORWARD, BACKWARD, STOP, FLOAT
	}
	public volatile Command command;
	public volatile int leftPower, rightPower;

}
