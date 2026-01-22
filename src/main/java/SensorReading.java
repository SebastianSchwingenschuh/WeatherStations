public record SensorReading(int x, int y, double temperatureCelsius) implements Comparable<SensorReading> {
    private static final double ABSOLUTE_ZERO = -273.15;
    private static final String TEMP_BELOW_ZERO_MSG = String.format("Temp below %s", ABSOLUTE_ZERO);

    public SensorReading(int x, int y, double temperatureCelsius) {
        this.x = x;
        this.y = y;
        if (temperatureCelsius < ABSOLUTE_ZERO) {
            throw new IllegalArgumentException(TEMP_BELOW_ZERO_MSG);
        }
        this.temperatureCelsius = temperatureCelsius;
    }

    @Override
    public int compareTo(SensorReading o) {
        return Double.compare(this.temperatureCelsius, o.temperatureCelsius);
    }
}
