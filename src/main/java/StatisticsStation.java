import java.util.TreeSet;

public class StatisticsStation extends WeatherStation{
    private TreeSet<Double> temperatures;

    public StatisticsStation(int x, int y, double sensorRange) {
        super(x, y, sensorRange);
    }

    public TreeSet<Double> getTemperatures() {
        return temperatures;
    }

    public double getLowestRecordedTemperature(){
        return -1;
    }
    
    public double getHighestRecordedTemperature(){
        return -1;
    }

    @Override
    public void update(SensorReading sensorReading) {
        super.update(sensorReading);
    }
}
