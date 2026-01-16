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

    private void setX(int x) {
        if (x <= 0.0) {
            throw new IllegalArgumentException();
        } else {
            this.x = x;
        }
    }

    private void setY(int y) {
        if (y <= 0.0) {
            throw new IllegalArgumentException();
        } else {
            this.y = y;
        }
    }

    private void setSensorRange(double sensorRange) {
        if (sensorRange <= 0.0) {
            throw new IllegalArgumentException();
        } else {
            this.sensorRange = sensorRange;
        }
    }

    public WeatherStation(int x, int y, double sensorRange) {
        this.x = x;
        this.y = y;
        this.setSensorRange(sensorRange);
    }

    public double calculateDistanceTo(int x, int y) {
        int deltaX = this.x - x;
        int deltaY = this.y - y;
        
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public boolean isInRange(SensorReading sensorReading) {
        double distance = calculateDistanceTo(sensorReading.getX(), sensorReading.getY());
        
        return distance <= this.getSensorRange();
    }

    public void update(SensorReading sensorReading) {
        
    }
}
