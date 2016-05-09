package lesson8;

import lejos.nxt.LCD;

public class QueueDriver {

	public static void main(String[] args)
	{
		FixedQueue<String> queue = new FixedQueue<String>(5);
		queue.add("hej1");
		queue.add("hej2");
		queue.add("hej3");
		queue.add("hej4");
		queue.add("hej0");
		queue.add("hej5");
		queue.add("hej6");
	
		LCD.drawString("result: " + queue.take(), 0, 0);
		
		while(true)
		{
			
		}
	}

}
