public abstract class WeatherStation implements WeatherObserver {
    private int x;
    private int y;
    private double sensorRange;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getSensorRange() {
        return sensorRange;
    }

    public WeatherStation(int x, int y, double sensorRange) {
        this.x = x;
        this.y = y;
        this.sensorRange = sensorRange;
    }
    
    public double calculateDistanceTo(int x, int y){
        return -1;
    }
    
    public boolean isInRange(SensorReading sensorReading){
        return false;
    }
    
    public void update(SensorReading sensorReading){
        
    }
}
