public class FahrenheitReadingFactory implements SensorReadingFactory {
    @Override
    public SensorReading createFromString(String s) {
        // "90;120;-13.0" ->
        String[] parts = s.split(";");

        if (parts.length != 3) {
            throw new WeatherException("Reading has invalid line length!");
        }

        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());
        //TC =
        //5/9
        //∗ (TF − 32)
        double temperatureCelsius = Math.round(((double) 5 / 9) * (Double.parseDouble(parts[2].trim()) - 32));

        return new SensorReading(x, y, temperatureCelsius);
    }
}
