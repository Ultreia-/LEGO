package lesson8;

public class GreenPlateauReachedException extends RuntimeException {

	private long timeStamp;

	public GreenPlateauReachedException() {

		timeStamp = System.currentTimeMillis();
		
	}
	
	
	public long getTimeStamp() {
		return timeStamp;
	}	
	
}
