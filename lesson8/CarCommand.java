package lesson8;


public class CarCommand 
{
	public enum Command {
	    FORWARD, BACKWARD, STOP, LEFT, RIGHT 
	}
	public volatile Command command;
	public volatile int leftPower, rightPower;

}
