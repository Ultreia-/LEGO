import java.io.File;
import lejos.nxt.*;

/**
 * Maximum LEGO NXT: Building Robots with Java Brains 2nd Edition
 * ISBN-13: 9780986832222
 * Variant Press (C) 2012
 * Chapter 16 - Sound
 * Robot: NXT Brick
 * Platform: NXT
 * @author Brian Bagnall
 * @version July 20, 2012
 */
public class PlaySound {

  public static void main(String [] args) {
    File myFile = new File("lego.wav");
    System.out.println("Playing " + myFile.getName());
    Sound.playSample(myFile, 100);
    System.out.println("Done hit ESC");
    Button.ESCAPE.waitForPressAndRelease();
  }
}
