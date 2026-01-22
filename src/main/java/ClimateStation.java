import java.util.HashMap;

public class ClimateStation extends WeatherStation {
    HashMap<Climate, Integer> climates = new HashMap<>();

    public ClimateStation(int x, int y, double sensorRange) {
        super(x, y, sensorRange);
    }

    public int getCountByClimate(Climate climate) {
        return climates.getOrDefault(climate, 0);
    }

    @Override
    public void update(SensorReading sensorReading) {
        double temp = sensorReading.temperatureCelsius();
        if (isInRange(sensorReading)) {
            if (temp <= 0.0) {
                climates.put(Climate.POLAR, climates.get(Climate.POLAR) + 1);
            } else if (temp >= 25.0) {
                climates.put(Climate.TROPICAL, climates.get(Climate.TROPICAL) + 1);
            } else {
                climates.put(Climate.TEMPERATE, climates.get(Climate.TEMPERATE) + 1);
            }
        }
    }
}
