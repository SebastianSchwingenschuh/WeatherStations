public interface WeatherSubject {
    void registerObserver(WeatherObserver weatherObserver);
    
    void unregisterObserver(WeatherObserver weatherObserver);
}
