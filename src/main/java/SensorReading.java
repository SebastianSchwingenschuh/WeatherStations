public class SensorReading implements Comparable<SensorReading>{
    private int x;
    private int y;
    private double temperatureCelsius;

    public SensorReading(int x, int y, double temperatureCelsius) {
        this.x = x;
        this.y = y;
        if(temperatureCelsius < -273.15){
            throw new IllegalArgumentException("Temp below absolute zero");
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
