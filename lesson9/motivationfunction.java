class DetectWall extends Thread implements Behavior {


  private TouchSensor touch = new TouchSensor(SensorPort.S1);
  private int count = 1;
  private boolean _suppressed;
  private int baseMotivation = 1;


  public int takeControl(){

    if (touch.isPressed() {
          if(!_suppressed) {
            count = 1;
          } else {
          count ++
        }
      }

    return count * baseMotivation

  }

}
