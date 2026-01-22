import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class WeatherStationManager implements WeatherSubject {
    List<WeatherObserver> observers = new ArrayList<WeatherObserver>();

    public void processSensorReading(SensorReading sensorReading) {
        for (WeatherObserver observer : observers) {
            observer.update(sensorReading);
        }
    }

    public void processSensorReadingsFromFile(String filePath, SensorReadingFactory factory) {
        //x;y;temperature
        //39;29;98.6
        try {
            List<String> lines = Files.readAllLines(Path.of(filePath));
            lines.removeFirst();
            for (String line : lines) {
                processSensorReading(factory.createFromString(line));
            }
        } catch (IOException e) {
            throw new WeatherException("Exception occured while reading file.", e);
        }
    }

    @Override
    public void registerObserver(WeatherObserver weatherObserver) {
        if (!this.observers.contains(weatherObserver)) {
            this.observers.add(weatherObserver);
        }
    }

    @Override
    public void unregisterObserver(WeatherObserver weatherObserver) {
        this.observers.remove(weatherObserver);
    }


}
