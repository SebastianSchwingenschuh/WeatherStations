import java.util.TreeSet;

public class StatisticsStation extends WeatherStation {
    private static final String NO_TEMPS_EXISTING_MSG = "No temperatures have been added yet.";
    private final TreeSet<Double> temperatures;

    public StatisticsStation(int x, int y, double sensorRange) {
        super(x, y, sensorRange);
        temperatures = new TreeSet<>();
    }

    public TreeSet<Double> getTemperatures() {
        return (TreeSet<Double>) temperatures.clone();
    }

    public double getLowestRecordedTemperature() {
        try {
            return this.temperatures.getFirst();
        } catch (Exception e) {
            throw new WeatherException(NO_TEMPS_EXISTING_MSG);
        }
    }

    public double getHighestRecordedTemperature() {
        try {
            return this.temperatures.getLast();
        } catch (Exception e) {
            throw new WeatherException(NO_TEMPS_EXISTING_MSG);
        }
    }

    @Override
    public void update(SensorReading sensorReading) {
        if (isInRange(sensorReading)) {
            temperatures.add((sensorReading.temperatureCelsius()));
        }
    }
}
