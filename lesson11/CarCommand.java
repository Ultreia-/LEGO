package lesson11;


public class CarCommand 
{
	
	
	public enum Command {
	    TRAVEL, ROTATE, STOP, PRINT;
	}
	public volatile Command command;
	public volatile double value;
	public boolean ismoving = false;
	
	double getValue() {
		return value;
	}
	
	void setValue(double value) {
		this.value = value;
		
	}
	
	
	
	

	
	
}
