package lesson11;


/**
 * A locomotion interface with methods to drive
 * a differential car with two independent motors. 
 *  
 * @author  Ole Caprani
 * @version 21.3.14
 */
public interface Pilot
{
    public void stop();   
    public void rotate(double degrees);  
    public void travel(double distance);
    public void show();
}
