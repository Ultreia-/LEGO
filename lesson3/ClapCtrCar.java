package lesson3;
import lejos.nxt.*;
import java.util.*;

public class ClapCtrCar 
{
    private static SoundSensor s = new SoundSensor(SensorPort.S1);
	private static DataLogger dl = new DataLogger("Sample.txt");

	private static void waitForClap() throws Exception
    {
		ArrayList<Integer> log = new ArrayList<Integer>();
		boolean clapIsIdentified = false;
		int logMaxSize = 10;
        int soundLevel;
        
        Thread.sleep(500);

        do
        {
			Thread.sleep(25);

            soundLevel = s.readValue();
			log.add(soundLevel);

           	dl.writeSample(soundLevel);

        	try{
			
				for (int i = 2; i < log.size(); i++)
				{
					clapIsIdentified = (log.get(i) < 50);
					if(clapIsIdentified) break;
				}

				clapIsIdentified = (log.get(1) > 85) && clapIsIdentified;
				clapIsIdentified = (log.get(0) < 50) && clapIsIdentified;

				if(log.size() >= logMaxSize) log.remove(0); //trim log
			}
			catch(IndexOutOfBoundsException e){} //ignore
        }
        while ( !clapIsIdentified );
    }

	public static void main(String [] args) throws Exception
	{
		dl.start();
		
		Button.ESCAPE.addButtonListener(createButtonListener());
		
		while (! Button.ESCAPE.isDown())
        { 
            waitForClap();
            LCD.drawString("Forward ",0,1);
            Car.forward(100, 100);

            waitForClap();		    			   
            LCD.drawString("Right   ",0,1);
            Car.forward(100, 0);
		    
            waitForClap();		    			   
            LCD.drawString("Left    ",0,1);
            Car.forward(0, 100);
		    
            waitForClap();		    			   
            LCD.drawString("Stop    ",0,1); 
            Car.stop();
       }

       Car.stop();
       LCD.clear();
       LCD.drawString("Program stopped", 0, 0);
       Thread.sleep(2000); 
	}

	private static ButtonListener createButtonListener() {
		return new ButtonListener(){

			@Override
			public void buttonPressed(Button b) {
				dl.close();
				System.exit(1);
			}

			@Override
			public void buttonReleased(Button b) {
				// TODO Auto-generated method stub
				
			}
		};
	}
}
