import java.util.HashMap;

public class ClimateStation extends WeatherStation{
    HashMap<Climate, Integer> climates;


    public ClimateStation(int x, int y, double sensorRange) {
        super(x, y, sensorRange);
    }
    
    public int getCountByClimate(Climate climate){
        return -1;
    }

    @Override
    public void update(SensorReading sensorReading) {
        super.update(sensorReading);
    }
}
