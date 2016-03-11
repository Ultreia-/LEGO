import javax.bluetooth.RemoteDevice;
import lejos.addon.icp.*;
import lejos.nxt.*;
import lejos.util.Delay;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 21 - Direct Bluetooth Control
 * Robot: Mobile Turret
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class MobileTurret implements ButtonListener, IcpEventListener {

    private Icp icp;
    private boolean polling = true;
    
    // Use this for NXT 1.0:
    // public static final int CANON_TACHO = 120; 
    public static final int CANON_TACHO = 360;
    
    public MobileTurret(Icp i) {
    	icp = i;
    	Button.ESCAPE.addButtonListener(this);
    	Motor.A.setSpeed(700);
    	Motor.B.setSpeed(150);
    	Motor.C.setSpeed(150);
    }
    
    public static void main(String[] args) {
		System.out.println("Finding iCP...");
		try {
		    // Use this code if the device is paired. 
			// IMPORTANT Use your iCP Bluetooth name:
		    //RemoteDevice btrd = lejos.nxt.comm.Bluetooth.getKnownDevice("iControlPad-0C1A");
		    //System.out.println("Got device: " + btrd.getDeviceAddr());
		    //MobileTurret robot = new IControlPad(Icp.getInstance(new IcpConnectorNxt(btrd.getDeviceAddr())));
		    
			// Use this if unpaired. 
			// IMPORTANT Enter in your own address here:
			MobileTurret robot = new MobileTurret(Icp.getInstance(new IcpConnectorNxt("00066645D00E")));
			System.out.println("Connected!");
		    
			robot.icp.addEventListener(robot);
			
		    while (robot.polling)
		    	Delay.msDelay(1000);
	
		} catch (Exception e) {
			System.out.println("\n" + e);
		}
    }
    
    public void pollInputs() {
		int[] result = null;
		try {
		    while (polling) {
			result = icp.getAnalogs();
			if (result != null) {
			    LCD.clear();
			    for (int i = 0; i < result.length; i++) {
				LCD.drawInt(result[i], 0, i);
			    }
			}
			Thread.sleep(50);
		    }
		} catch (Exception e) {
			System.out.println("Caught " + e.getMessage());
		}
    }
    
    public void buttonPressed(Button b) {
    	polling = false;
    }

    public void buttonReleased(Button b) {}
    
    public void digitalChange(IcpDigital d) {
    	System.out.println("D " + d);
    }

    public void analogOneChange(int x, int y) {
    	System.out.println("A1 " + x + "," + y);
    }

    public void analogTwoChange(int x, int y) {
    	System.out.println("A2 " + x + "," + y);
    }

    public void buttonPressed(IcpButton b) {
		if(b.equals(b.A)) {
			Motor.B.forward();
			Motor.C.backward();
		} else if(b.equals(b.B)) {
			Motor.C.forward();
			Motor.B.backward();
		} else if(b.equals(b.Y)) {
			Motor.C.backward();
			Motor.B.backward();
		} else if(b.equals(b.X)) {
			Motor.A.rotate(CANON_TACHO);
		}
	}

    public void buttonReleased(IcpButton b) {
		if((b.equals(b.A)) || (b.equals(b.B)) || (b.equals(b.Y))) {
			Motor.B.flt();
			Motor.C.flt();
		}
    }
}