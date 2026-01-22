public class SensorReading implements Comparable<SensorReading>{
    private static final double ABSOLUTE_ZERO = -273.15;
    private static final String TEMP_BELOW_ZERO_MSG = String.format("Temp below %s", ABSOLUTE_ZERO);
    private int x;
    private int y;
    private double temperatureCelsius;
    

    public SensorReading(int x, int y, double temperatureCelsius) {
        this.x = x;
        this.y = y;
        if(temperatureCelsius < ABSOLUTE_ZERO){
            throw new IllegalArgumentException(TEMP_BELOW_ZERO_MSG);
        }
        this.temperatureCelsius = temperatureCelsius;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    @Override
    public int compareTo(SensorReading o) {
        return Double.compare(this.temperatureCelsius, o.temperatureCelsius);
    }
}
