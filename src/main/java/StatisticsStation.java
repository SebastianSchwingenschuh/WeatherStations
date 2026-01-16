import java.util.TreeSet;

public class StatisticsStation extends WeatherStation{
    private TreeSet<Double> temperatures;

    public StatisticsStation(int x, int y, double sensorRange) {
        super(x, y, sensorRange);
        temperatures = new TreeSet<>();
    }

    public TreeSet<Double> getTemperatures() {
        return temperatures;
    }

    public double getLowestRecordedTemperature(){
        try{
            return this.temperatures.getFirst();
        }catch (Exception e){
            throw new WeatherException("No temperatures have been added yet.");
        }
    }
    
    public double getHighestRecordedTemperature(){
        try{
            return this.temperatures.getLast();
        }catch (Exception e){
            throw new WeatherException("No temperatures have been added yet.");
        }
    }

    @Override
    public void update(SensorReading sensorReading) {
        
    }
}
