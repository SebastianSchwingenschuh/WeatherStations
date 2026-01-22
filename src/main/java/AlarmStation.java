public class AlarmStation extends WeatherStation{
    private double triggerLow;
    private double triggerHigh;
    private int alarmsRaised;

    public AlarmStation(int x, int y, double sensorRange, double triggerLow, double triggerHigh) {
        //AlarmStation(30, 40, 10.0, -10.0, 30.0);
        super(x, y, sensorRange);
        this.triggerLow = triggerLow;
        this.triggerHigh = triggerHigh;
    }

    public double getTriggerLow() {
        return triggerLow;
    }

    public double getTriggerHigh() {
        return triggerHigh;
    }

    public int getAlarmsRaised() {
        return alarmsRaised;
    }

    @Override
    public void update(SensorReading sensorReading) {
        if(isInRange(sensorReading)){
            if(sensorReading.getTemperatureCelsius() <= triggerLow || sensorReading.getTemperatureCelsius() >= triggerHigh){
                alarmsRaised ++;
            }
        }
    }
}
