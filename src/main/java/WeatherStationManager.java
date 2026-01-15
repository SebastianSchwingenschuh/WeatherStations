import java.util.List;

public class WeatherStationManager implements WeatherSubject{
    List<WeatherObserver> observers;
    
    
    
    public void processSensorReading(SensorReading sensorReading){
        
    }
    
    public void processSensorReadingsFromFile(String s, SensorReadingFactory factory){
        
    }

    @Override
    public void registerObserver(WeatherObserver weatherObserver) {
        
    }

    @Override
    public void unregisterObserver(WeatherObserver weatherObserver) {
        
    }


}
