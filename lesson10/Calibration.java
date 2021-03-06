package lesson10;

import lejos.nxt.*;
import lejos.nxt.comm.RConsole;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.Pose;
import lejos.util.Delay;
/**
 * A program that uses the DifferentialPilot to make a differential
 * driven car drive in a square. During the drive the OdometryPoseProvider
 * is used to display the Pose of the car on the LCD after each
 * movement of the car.
 * 
 * Use the PC tool nxjconsoleviewer to show the LCD output on a PC
 * for easier inspection.
 * 
 * @author  Ole Caprani
 * @version 13.05.15
 */
public class Calibration 
{
   private static void show(Pose p)
   {
      LCD.clear();
       LCD.drawString("Pose X " + p.getX(), 0, 2);
       LCD.drawString("Pose Y " + p.getY(), 0, 3);
       LCD.drawString("Pose V " + p.getHeading(), 0, 4);
   }

   public static void main(String [] args)  
   throws Exception 
   {
       
       double travelSpeed = 5, rotateSpeed = 45;
       NXTRegulatedMotor left = Motor.B;
       NXTRegulatedMotor right = Motor.C;
       
       double rightWheel = 5.41, leftWheel = 5.49, trackWidth = 16;
       DifferentialPilot pilot = new DifferentialPilot(leftWheel, rightWheel, trackWidth, left, right, false);
       
       OdometryPoseProvider poseProvider = new OdometryPoseProvider(pilot);
       Pose initialPose = new Pose(0,0,0);
       //RConsole.open();
       pilot.setTravelSpeed(travelSpeed);
       pilot.setRotateSpeed(rotateSpeed);
       poseProvider.setPose(initialPose);
       
       LCD.clear();
       LCD.drawString("Pilot square", 0, 0);	   

           //pilot.travel(50);    
           pilot.rotate(360);
           show(poseProvider.getPose());
                
       
       pilot.stop();
       LCD.drawString("Program stopped", 0, 0);
       Button.waitForAnyPress();
       Thread.sleep(2000);
       //RConsole.close();
   }
}
