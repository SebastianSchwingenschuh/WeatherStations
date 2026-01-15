public class CelsiusReadingFactory implements SensorReadingFactory{
    @Override
    public SensorReading createFromString(String s) {
        //"175;60;31.5"
        String[] parts = s.split(";");
        
        if(parts.length != 3){
            throw new WeatherException("Reading has invalid line length!");
        }
        
        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());
        double temperatureCelsius = Double.parseDouble(parts[2].trim());
        
        return new SensorReading(x, y, temperatureCelsius);
    }
}
