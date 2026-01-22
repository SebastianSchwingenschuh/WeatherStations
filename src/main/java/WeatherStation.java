public abstract class WeatherStation implements WeatherObserver {
    private final int x;
    private final int y;
    private final double sensorRange;

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
        if (sensorRange <= 0.0) {
            throw new IllegalArgumentException();
        } else {
            this.sensorRange = sensorRange;
        }
    }

    public double calculateDistanceTo(int x, int y) {
        int deltaX = this.x - x;
        int deltaY = this.y - y;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public boolean isInRange(SensorReading sensorReading) {
        double distance = calculateDistanceTo(sensorReading.x(), sensorReading.y());

        return distance <= this.getSensorRange();
    }

    public void update(SensorReading sensorReading) {
    }
}
