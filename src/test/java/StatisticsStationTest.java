import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsStationTest {
    @Test
    void testInheritance() {
        assertEquals(true, Modifier.isAbstract(WeatherStation.class.getModifiers()));

        WeatherStation station = new StatisticsStation(50, 0, 5.0);
    }

    @Test
    void testConstructorValidValues() {
        StatisticsStation station = new StatisticsStation(50, 0, 5.0);

        assertEquals(50, station.getX());
        assertEquals(0, station.getY());
        assertEquals(5.0, station.getSensorRange(), 0.001);

        TreeSet<Double> temperatures = station.getTemperatures();
        assertEquals(0, station.getTemperatures().size());
    }

    @Test
    void testLowestRecordedTemperatureThrowsExceptionIfEmpty() {
        StatisticsStation station = new StatisticsStation(50, 0, 5.0);

        WeatherException ex = assertThrows(WeatherException.class, () -> {
            double haxi = station.getLowestRecordedTemperature();
        });

        assertEquals("No temperatures have been added yet.", ex.getMessage());
    }

    @Test
    void testHighestRecordedTemperatureThrowsExceptionIfEmpty() {
        StatisticsStation station = new StatisticsStation(50, 0, 5.0);

        WeatherException ex = assertThrows(WeatherException.class, () -> {
            double popaxi = station.getHighestRecordedTemperature();
        });

        assertEquals("No temperatures have been added yet.", ex.getMessage());
    }

    @Test
    void testConstructorZeroSensorRange() {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsStation station = new StatisticsStation(50, 0, 0.0);
        });
    }

    @Test
    void testConstructorNegativeSensorRange() {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsStation station = new StatisticsStation(50, 0, -5.0);
        });
    }

    @Test
    void testCalculateDistanceTo() {
        StatisticsStation station = new StatisticsStation(50, 0, 5.0);

        assertEquals(0.0, station.calculateDistanceTo(50, 0), 0.01);
        assertEquals(5.0, station.calculateDistanceTo(50, 5), 0.01);
        assertEquals(6.0, station.calculateDistanceTo(50, 6), 0.01);
        assertEquals(14.1421, station.calculateDistanceTo(40, 10), 0.01);
        assertEquals(50.0, station.calculateDistanceTo(0, 0), 0.01);
        assertEquals(4.2426, station.calculateDistanceTo(53, 3), 0.01);
        assertEquals(5.6568, station.calculateDistanceTo(46, 4), 0.01);
        assertEquals(5.0, station.calculateDistanceTo(46, 3), 0.01);
        assertEquals(3.6055, station.calculateDistanceTo(48, 3), 0.01);
        assertEquals(28.1780, station.calculateDistanceTo(25, 13), 0.01);
    }

    @Test
    void testIsInRange() {
        StatisticsStation station = new StatisticsStation(50, 0, 5.0);

        assertEquals(true, station.isInRange(new SensorReading(50, 0, 5.0)));
        assertEquals(true, station.isInRange(new SensorReading(50, 5, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(50, 6, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(40, 10, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(0, 0, 5.0)));
        assertEquals(true, station.isInRange(new SensorReading(53, 3, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(46, 4, 5.0)));
        assertEquals(true, station.isInRange(new SensorReading(46, 3, 5.0)));
        assertEquals(true, station.isInRange(new SensorReading(48, 3, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(25, 13, 5.0)));
    }

    @Test
    void testUpdateInRange() {
        StatisticsStation station = new StatisticsStation(50, 0, 5.0);

        TreeSet<Double> temperatures = station.getTemperatures();
        assertEquals(0, temperatures.size());

        station.update(new SensorReading(50, 5, 5.0));

        assertEquals(5.0, station.getHighestRecordedTemperature(), 0.001);
        assertEquals(5.0, station.getLowestRecordedTemperature(), 0.001);
        temperatures = station.getTemperatures();
        assertEquals(1, temperatures.size());
        assertEquals(true, temperatures.contains(5.0));

        station.update(new SensorReading(46, 3, 0.0));

        assertEquals(5.0, station.getHighestRecordedTemperature(), 0.001);
        assertEquals(0.0, station.getLowestRecordedTemperature(), 0.001);
        temperatures = station.getTemperatures();
        assertEquals(2, temperatures.size());
        assertEquals(true, temperatures.contains(0.0));
        assertEquals(true, temperatures.contains(5.0));

        station.update(new SensorReading(50, 0, 3.0));

        assertEquals(5.0, station.getHighestRecordedTemperature(), 0.001);
        assertEquals(0.0, station.getLowestRecordedTemperature(), 0.001);
        temperatures = station.getTemperatures();
        assertEquals(3, temperatures.size());
        assertEquals(true, temperatures.contains(0.0));
        assertEquals(true, temperatures.contains(3.0));
        assertEquals(true, temperatures.contains(5.0));

        station.update(new SensorReading(48, 3, 6.0));

        assertEquals(6.0, station.getHighestRecordedTemperature(), 0.001);
        assertEquals(0.0, station.getLowestRecordedTemperature(), 0.001);
        temperatures = station.getTemperatures();
        assertEquals(4, temperatures.size());
        assertEquals(true, temperatures.contains(0.0));
        assertEquals(true, temperatures.contains(3.0));
        assertEquals(true, temperatures.contains(5.0));
        assertEquals(true, temperatures.contains(6.0));

        station.update(new SensorReading(53, 3, -1.0));

        assertEquals(6.0, station.getHighestRecordedTemperature(), 0.001);
        assertEquals(-1.0, station.getLowestRecordedTemperature(), 0.001);
        temperatures = station.getTemperatures();
        assertEquals(5, temperatures.size());
        assertEquals(true, temperatures.contains(-1.0));
        assertEquals(true, temperatures.contains(0.0));
        assertEquals(true, temperatures.contains(3.0));
        assertEquals(true, temperatures.contains(5.0));
        assertEquals(true, temperatures.contains(6.0));
    }

    @Test
    void testUpdateOutOfRange() {
        StatisticsStation station = new StatisticsStation(50, 0, 5.0);

        station.update(new SensorReading(50, 6, 2.0));
        station.update(new SensorReading(40, 10, 4.0));
        station.update(new SensorReading(0, 0, 7.0));
        station.update(new SensorReading(46, 4, -5.0));
        station.update(new SensorReading(25, 13, 13.0));

        TreeSet<Double> temperatures = station.getTemperatures();
        assertEquals(0, temperatures.size());

        assertThrows(WeatherException.class, () -> {
            double haxi = station.getLowestRecordedTemperature();
        });

        assertThrows(WeatherException.class, () -> {
            double popaxi = station.getHighestRecordedTemperature();
        });
    }

    @Test
    void testGetTemperaturesDoesNotChangeValues() {
        StatisticsStation station = new StatisticsStation(50, 0, 5.0);

        station.update(new SensorReading(50, 6, 8.0));
        station.update(new SensorReading(50, 5, 5.0));
        station.update(new SensorReading(48, 3, 6.0));
        station.update(new SensorReading(46, 3, 0.0));
        station.update(new SensorReading(50, 0, 3.0));
        station.update(new SensorReading(25, 13, -10.0));
        station.update(new SensorReading(53, 3, -1.0));
        station.update(new SensorReading(0, 0, 4.0));
        station.update(new SensorReading(46, 4, 20.0));
        station.update(new SensorReading(40, 10, 0.5));

        TreeSet<Double> temperaturesClone = station.getTemperatures();
        temperaturesClone.add(13.0);

        TreeSet<Double> temperaturesOriginal = station.getTemperatures();
        assertEquals(false, temperaturesOriginal.contains(13.0));
    }

    @Test
    void testObserver() {
        WeatherStationManager manager = new WeatherStationManager();

        StatisticsStation station = new StatisticsStation(50, 0, 5.0);

        manager.registerObserver(station);

        manager.processSensorReading(new SensorReading(50, 6, 13.0));
        manager.processSensorReading(new SensorReading(50, 5, 5.0));
        manager.processSensorReading(new SensorReading(48, 3, 6.0));
        manager.processSensorReading(new SensorReading(46, 3, 0.0));
        manager.processSensorReading(new SensorReading(50, 0, 3.0));
        manager.processSensorReading(new SensorReading(25, 13, 2.0));
        manager.processSensorReading(new SensorReading(53, 3, -1.0));
        manager.processSensorReading(new SensorReading(0, 0, -14.0));
        manager.processSensorReading(new SensorReading(46, 4, 11.0));
        manager.processSensorReading(new SensorReading(40, 10, 4.5));

        assertEquals(6.0, station.getHighestRecordedTemperature(), 0.001);
        assertEquals(-1.0, station.getLowestRecordedTemperature(), 0.001);
        TreeSet<Double> temperatures = station.getTemperatures();
        assertEquals(5, temperatures.size());
        assertEquals(true, temperatures.contains(-1.0));
        assertEquals(true, temperatures.contains(0.0));
        assertEquals(true, temperatures.contains(3.0));
        assertEquals(true, temperatures.contains(5.0));
        assertEquals(true, temperatures.contains(6.0));
    }

    @Test
    void testObserverRegisterUnregister() {
        WeatherStationManager manager = new WeatherStationManager();

        StatisticsStation station = new StatisticsStation(50, 0, 5.0);

        manager.registerObserver(station);

        manager.processSensorReading(new SensorReading(50, 6, 13.0));
        manager.processSensorReading(new SensorReading(46, 3, 0.0));
        manager.processSensorReading(new SensorReading(0, 0, -14.0));

        manager.unregisterObserver(station);

        manager.processSensorReading(new SensorReading(50, 5, 5.0));
        manager.processSensorReading(new SensorReading(48, 3, 6.0));
        manager.processSensorReading(new SensorReading(53, 3, -1.0));

        manager.registerObserver(station);

        manager.processSensorReading(new SensorReading(25, 13, 2.0));
        manager.processSensorReading(new SensorReading(50, 0, 3.0));
        manager.processSensorReading(new SensorReading(46, 4, 11.0));
        manager.processSensorReading(new SensorReading(40, 10, 4.5));

        assertEquals(3.0, station.getHighestRecordedTemperature(), 0.001);
        assertEquals(0.0, station.getLowestRecordedTemperature(), 0.001);
        TreeSet<Double> temperatures = station.getTemperatures();
        assertEquals(2, temperatures.size());
        assertEquals(true, temperatures.contains(0.0));
        assertEquals(true, temperatures.contains(3.0));
    }

    @Test
    void testObserverWithFiles() {
        WeatherStationManager manager = new WeatherStationManager();

        StatisticsStation station = new StatisticsStation(50, 0, 5.0);

        manager.registerObserver(station);

        manager.processSensorReadingsFromFile("data/readings_fahrenheit.csv", new FahrenheitReadingFactory());

        assertEquals(19.0, station.getHighestRecordedTemperature(), 0.001);
        assertEquals(3.0, station.getLowestRecordedTemperature(), 0.001);
        TreeSet<Double> temperatures = station.getTemperatures();
        assertEquals(5, temperatures.size());
        assertEquals(true, temperatures.contains(3.0));
        assertEquals(true, temperatures.contains(14.0));
        assertEquals(true, temperatures.contains(16.0));
        assertEquals(true, temperatures.contains(17.0));
        assertEquals(true, temperatures.contains(19.0));

        manager.processSensorReadingsFromFile("data/readings_celsius.csv", new CelsiusReadingFactory());

        assertEquals(22.6, station.getHighestRecordedTemperature(), 0.001);
        assertEquals(-1.6, station.getLowestRecordedTemperature(), 0.001);
        temperatures = station.getTemperatures();
        assertEquals(12, temperatures.size());
        assertEquals(true, temperatures.contains(-1.6));
        assertEquals(true, temperatures.contains(-1.5));
        assertEquals(true, temperatures.contains(3.0));
        assertEquals(true, temperatures.contains(6.4));
        assertEquals(true, temperatures.contains(8.8));
        assertEquals(true, temperatures.contains(14.0));
        assertEquals(true, temperatures.contains(16.0));
        assertEquals(true, temperatures.contains(16.9));
        assertEquals(true, temperatures.contains(17.0));
        assertEquals(true, temperatures.contains(19.0));
        assertEquals(true, temperatures.contains(20.2));
        assertEquals(true, temperatures.contains(22.6));
    }

    @Test
    void testObserverWithFilesAndManualAdding() {
        WeatherStationManager manager = new WeatherStationManager();

        StatisticsStation station = new StatisticsStation(50, 0, 5.0);

        manager.registerObserver(station);

        manager.processSensorReading(new SensorReading(46, 4, 11.0));
        manager.processSensorReading(new SensorReading(40, 10, 4.5));
        manager.processSensorReading(new SensorReading(50, 0, -3.5));
        manager.processSensorReadingsFromFile("data/readings_fahrenheit.csv", new FahrenheitReadingFactory());
        manager.processSensorReading(new SensorReading(50, 6, 13.0));
        manager.processSensorReading(new SensorReading(50, 5, 5.0));
        manager.processSensorReading(new SensorReading(0, 0, -14.0));
        manager.processSensorReading(new SensorReading(46, 3, 0.0));
        manager.processSensorReadingsFromFile("data/readings_celsius.csv", new CelsiusReadingFactory());
        manager.processSensorReading(new SensorReading(48, 3, 27.2));
        manager.processSensorReading(new SensorReading(25, 13, 2.0));
        manager.processSensorReading(new SensorReading(53, 3, -1.0));

        assertEquals(27.2, station.getHighestRecordedTemperature(), 0.001);
        assertEquals(-3.5, station.getLowestRecordedTemperature(), 0.001);
        TreeSet<Double> temperatures = station.getTemperatures();
        assertEquals(17, temperatures.size());
        assertEquals(true, temperatures.contains(-3.5));
        assertEquals(true, temperatures.contains(-1.6));
        assertEquals(true, temperatures.contains(-1.5));
        assertEquals(true, temperatures.contains(-1.0));
        assertEquals(true, temperatures.contains(0.0));
        assertEquals(true, temperatures.contains(3.0));
        assertEquals(true, temperatures.contains(5.0));
        assertEquals(true, temperatures.contains(6.4));
        assertEquals(true, temperatures.contains(8.8));
        assertEquals(true, temperatures.contains(14.0));
        assertEquals(true, temperatures.contains(16.0));
        assertEquals(true, temperatures.contains(16.9));
        assertEquals(true, temperatures.contains(17.0));
        assertEquals(true, temperatures.contains(19.0));
        assertEquals(true, temperatures.contains(20.2));
        assertEquals(true, temperatures.contains(22.6));
        assertEquals(true, temperatures.contains(27.2));
    }

    @Test
    void testObserverWithFilesAndUnregister() {
        WeatherStationManager manager = new WeatherStationManager();

        StatisticsStation station = new StatisticsStation(50, 0, 5.0);

        manager.registerObserver(station);

        manager.processSensorReadingsFromFile("data/readings_fahrenheit.csv", new FahrenheitReadingFactory());

        assertEquals(19.0, station.getHighestRecordedTemperature(), 0.001);
        assertEquals(3.0, station.getLowestRecordedTemperature(), 0.001);
        TreeSet<Double> temperatures = station.getTemperatures();
        assertEquals(5, temperatures.size());
        assertEquals(true, temperatures.contains(3.0));
        assertEquals(true, temperatures.contains(14.0));
        assertEquals(true, temperatures.contains(16.0));
        assertEquals(true, temperatures.contains(17.0));
        assertEquals(true, temperatures.contains(19.0));

        manager.unregisterObserver(station);

        manager.processSensorReadingsFromFile("data/readings_celsius.csv", new CelsiusReadingFactory());

        assertEquals(19.0, station.getHighestRecordedTemperature(), 0.001);
        assertEquals(3.0, station.getLowestRecordedTemperature(), 0.001);
        temperatures = station.getTemperatures();
        assertEquals(5, temperatures.size());
        assertEquals(true, temperatures.contains(3.0));
        assertEquals(true, temperatures.contains(14.0));
        assertEquals(true, temperatures.contains(16.0));
        assertEquals(true, temperatures.contains(17.0));
        assertEquals(true, temperatures.contains(19.0));
    }

    @Test
    void testObserverInvalidFile() {
        WeatherException ex = assertThrows(WeatherException.class, ()->{
            WeatherStationManager manager = new WeatherStationManager();
            manager.processSensorReadingsFromFile("data/readings_kelvin.csv", new CelsiusReadingFactory());
        });

        assertEquals("Exception occured while reading file.", ex.getMessage());
        assertEquals(true, ex.getCause() instanceof IOException);
    }
}