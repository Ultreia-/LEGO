package lesson3;
import lejos.nxt.*;
import java.util.*;

public class ClapCtrCarDraft 
{
    private static SoundSensor sound = new SoundSensor(SensorPort.S1);
	
	private static  void waitForLoudSound() throws Exception
    {
        // int soundLevel;

        // Thread.sleep(500);
        // do
        // {
        //     soundLevel = sound.readValue();
        //     LCD.drawInt(soundLevel,4,10,0); 
        // }
        // while ( soundLevel < soundThreshold );
    }

	public static void main(String [] args) throws Exception
	{
		SoundSensor s = new SoundSensor(SensorPort.S1);
		DataLogger dl = new DataLogger("Sample.txt");
		int soundLevel;
		
		boolean clapIsIdentified = false;

		ArrayList<Integer> log = new ArrayList<Integer>();

		int logMaxSize = 10;
		//LCD.drawString("Level: ", 0, 0);

		while (!Button.ESCAPE.isDown())
		{		  
			soundLevel = s.readValue();
			log.add(soundLevel);

			try{
			
				for (int i = 2; i < log.size(); i++)
				{
					clapIsIdentified = (log.get(i) < 50);
					if(clapIsIdentified) break;
				}

				clapIsIdentified = (log.get(1) > 75) && clapIsIdentified;

				clapIsIdentified = (log.get(0) < 50) && clapIsIdentified;

				if(clapIsIdentified)
				{
		        	LCD.drawString("U CLAPPED MOFO?",0,1);

					clapIsIdentified = false;
					Thread.sleep(1000);
					LCD.drawString("               ",0,1);
					Thread.sleep(1000);
				}

				if(log.size() >= logMaxSize) log.remove(0); //trim log
			}
			catch(IndexOutOfBoundsException e){} //ignore

			Thread.sleep(25);
		}

		LCD.clear();
		LCD.drawString("Program stopped", 0, 0);
		Thread.sleep(2000);
	}
}
