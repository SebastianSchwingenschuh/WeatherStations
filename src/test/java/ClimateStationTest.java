import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class ClimateStationTest {
    @Test
    void testInheritance() {
        assertEquals(true, Modifier.isAbstract(WeatherStation.class.getModifiers()));

        WeatherStation station = new ClimateStation(40, 20, 20.0);
    }

    @Test
    void testConstructorValidValues() {
        ClimateStation station = new ClimateStation(40, 20, 20.0);

        assertEquals(40, station.getX());
        assertEquals(20, station.getY());
        assertEquals(20.0, station.getSensorRange(), 0.001);
        assertEquals(0, station.getCountByClimate(Climate.POLAR));
        assertEquals(0, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(0, station.getCountByClimate(Climate.TROPICAL));
    }

    @Test
    void testConstructorZeroSensorRange() {
        assertThrows(IllegalArgumentException.class, () -> {
            ClimateStation station = new ClimateStation(40, 20, 0.0);
        });
    }

    @Test
    void testConstructorNegativeSensorRange() {
        assertThrows(IllegalArgumentException.class, () -> {
            ClimateStation station = new ClimateStation(40, 20, -0.1);
        });
    }

    @Test
    void testCalculateDistanceTo() {
        ClimateStation station = new ClimateStation(40, 20, 20.0);

        assertEquals(0.0, station.calculateDistanceTo(40, 20), 0.01);
        assertEquals(3.6055, station.calculateDistanceTo(42, 17), 0.01);
        assertEquals(20.0, station.calculateDistanceTo(40, 40), 0.01);
        assertEquals(21.0, station.calculateDistanceTo(40, 41), 0.01);
        assertEquals(19.2093, station.calculateDistanceTo(55, 8), 0.01);
        assertEquals(80.0, station.calculateDistanceTo(40, 100), 0.01);
        assertEquals(7.0710, station.calculateDistanceTo(35, 25), 0.01);
        assertEquals(14.1421, station.calculateDistanceTo(50, 10), 0.01);
        assertEquals(20.8086, station.calculateDistanceTo(57, 32), 0.01);
        assertEquals(44.7213, station.calculateDistanceTo(0, 0), 0.01);
    }

    @Test
    void testIsInRange() {
        ClimateStation station = new ClimateStation(40, 20, 20.0);

        assertEquals(true, station.isInRange(new SensorReading(40, 20, 5.0)));
        assertEquals(true, station.isInRange(new SensorReading(42, 17, 5.0)));
        assertEquals(true, station.isInRange(new SensorReading(40, 40, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(40, 41, 5.0)));
        assertEquals(true, station.isInRange(new SensorReading(55, 8, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(40, 100, 5.0)));
        assertEquals(true, station.isInRange(new SensorReading(35, 25, 5.0)));
        assertEquals(true, station.isInRange(new SensorReading(50, 10, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(57, 32, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(0, 0, 5.0)));
    }

    @Test
    void testUpdateInRange() {
        ClimateStation station = new ClimateStation(40, 20, 20.0);

        assertEquals(0, station.getCountByClimate(Climate.POLAR));
        assertEquals(0, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(0, station.getCountByClimate(Climate.TROPICAL));

        //exactly at polar limit
        station.update(new SensorReading(40, 20, 0.0));

        assertEquals(1, station.getCountByClimate(Climate.POLAR));
        assertEquals(0, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(0, station.getCountByClimate(Climate.TROPICAL));

        //exactly at tropical limit
        station.update(new SensorReading(42, 17, 25.0));

        assertEquals(1, station.getCountByClimate(Climate.POLAR));
        assertEquals(0, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(1, station.getCountByClimate(Climate.TROPICAL));

        //slightly above polar limit, well below tropical limit
        station.update(new SensorReading(40, 40, 0.1));

        assertEquals(1, station.getCountByClimate(Climate.POLAR));
        assertEquals(1, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(1, station.getCountByClimate(Climate.TROPICAL));

        //somewhere in between polar and tropical
        station.update(new SensorReading(55, 8, 13.0));

        assertEquals(1, station.getCountByClimate(Climate.POLAR));
        assertEquals(2, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(1, station.getCountByClimate(Climate.TROPICAL));

        //slightly below tropical limit, well above polar limit
        station.update(new SensorReading(35, 25, 24.9));

        assertEquals(1, station.getCountByClimate(Climate.POLAR));
        assertEquals(3, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(1, station.getCountByClimate(Climate.TROPICAL));

        //well above tropical limit
        station.update(new SensorReading(50, 10, 33.0));

        assertEquals(1, station.getCountByClimate(Climate.POLAR));
        assertEquals(3, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(2, station.getCountByClimate(Climate.TROPICAL));

        //well below polar limit
        station.update(new SensorReading(40, 21, -5.0));

        assertEquals(2, station.getCountByClimate(Climate.POLAR));
        assertEquals(3, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(2, station.getCountByClimate(Climate.TROPICAL));
    }

    @Test
    void testUpdateOutOfRange() {
        ClimateStation station = new ClimateStation(40, 20, 20.0);

        station.update(new SensorReading(40, 41, 0.0));
        station.update(new SensorReading(40, 100, -5.0));
        station.update(new SensorReading(55, 35, 13.0));
        station.update(new SensorReading(10, 20, 7.0));
        station.update(new SensorReading(57, 32, 25.0));
        station.update(new SensorReading(0, 0, 31.0));

        assertEquals(0, station.getCountByClimate(Climate.POLAR));
        assertEquals(0, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(0, station.getCountByClimate(Climate.TROPICAL));
    }

    @Test
    void testObserver() {
        WeatherStationManager manager = new WeatherStationManager();

        ClimateStation station = new ClimateStation(40, 20, 20.0);

        manager.registerObserver(station);

        assertEquals(0, station.getCountByClimate(Climate.POLAR));
        assertEquals(0, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(0, station.getCountByClimate(Climate.TROPICAL));

        manager.processSensorReading(new SensorReading(35, 25, 24.9));
        manager.processSensorReading(new SensorReading(40, 100, -5.0));
        manager.processSensorReading(new SensorReading(55, 35, 13.0));
        manager.processSensorReading(new SensorReading(40, 21, -5.0));
        manager.processSensorReading(new SensorReading(50, 10, 33.0));
        manager.processSensorReading(new SensorReading(0, 0, 31.0));
        manager.processSensorReading(new SensorReading(57, 32, 25.0));
        manager.processSensorReading(new SensorReading(40, 40, 0.1));
        manager.processSensorReading(new SensorReading(55, 8, 13.0));
        manager.processSensorReading(new SensorReading(40, 41, 0.0));
        manager.processSensorReading(new SensorReading(10, 20, 7.0));
        manager.processSensorReading(new SensorReading(40, 20, 0.0));
        manager.processSensorReading(new SensorReading(42, 17, 25.0));

        assertEquals(2, station.getCountByClimate(Climate.POLAR));
        assertEquals(3, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(2, station.getCountByClimate(Climate.TROPICAL));
    }

    @Test
    void testObserverRegisterUnregister() {
        WeatherStationManager manager = new WeatherStationManager();

        ClimateStation station = new ClimateStation(40, 20, 20.0);

        manager.registerObserver(station);

        manager.processSensorReading(new SensorReading(35, 25, 24.9));
        manager.processSensorReading(new SensorReading(40, 100, -5.0));
        manager.processSensorReading(new SensorReading(55, 35, 13.0));
        manager.processSensorReading(new SensorReading(40, 21, -5.0));

        manager.unregisterObserver(station);

        manager.processSensorReading(new SensorReading(55, 8, 13.0));
        manager.processSensorReading(new SensorReading(40, 41, 0.0));
        manager.processSensorReading(new SensorReading(10, 20, 7.0));
        manager.processSensorReading(new SensorReading(40, 20, 0.0));
        manager.processSensorReading(new SensorReading(42, 17, 25.0));

        manager.registerObserver(station);

        manager.processSensorReading(new SensorReading(50, 10, 33.0));
        manager.processSensorReading(new SensorReading(0, 0, 31.0));
        manager.processSensorReading(new SensorReading(57, 32, 25.0));
        manager.processSensorReading(new SensorReading(40, 40, 0.1));

        assertEquals(1, station.getCountByClimate(Climate.POLAR));
        assertEquals(2, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(1, station.getCountByClimate(Climate.TROPICAL));
    }

    @Test
    void testObserverWithFiles() {
        WeatherStationManager manager = new WeatherStationManager();

        ClimateStation station = new ClimateStation(40, 20, 20.0);

        manager.registerObserver(station);

        manager.processSensorReadingsFromFile("data/readings_celsius.csv", new CelsiusReadingFactory());

        assertEquals(63, station.getCountByClimate(Climate.POLAR));
        assertEquals(290, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(32, station.getCountByClimate(Climate.TROPICAL));

        manager.processSensorReadingsFromFile("data/readings_fahrenheit.csv", new FahrenheitReadingFactory());

        assertEquals(150, station.getCountByClimate(Climate.POLAR));
        assertEquals(596, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(67, station.getCountByClimate(Climate.TROPICAL));
    }

    @Test
    void testObserverWithFilesAndManualAdding() {
        WeatherStationManager manager = new WeatherStationManager();

        ClimateStation station = new ClimateStation(40, 20, 20.0);

        manager.registerObserver(station);

        assertEquals(0, station.getCountByClimate(Climate.POLAR));
        assertEquals(0, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(0, station.getCountByClimate(Climate.TROPICAL));

        manager.processSensorReading(new SensorReading(35, 25, 24.9));
        manager.processSensorReading(new SensorReading(40, 100, -5.0));
        manager.processSensorReading(new SensorReading(55, 35, 13.0));
        manager.processSensorReading(new SensorReading(40, 21, -5.0));
        manager.processSensorReading(new SensorReading(50, 10, 33.0));
        manager.processSensorReadingsFromFile("data/readings_celsius.csv", new CelsiusReadingFactory());
        manager.processSensorReading(new SensorReading(0, 0, 31.0));
        manager.processSensorReading(new SensorReading(57, 32, 25.0));
        manager.processSensorReading(new SensorReading(40, 40, 0.1));
        manager.processSensorReading(new SensorReading(55, 8, 13.0));
        manager.processSensorReadingsFromFile("data/readings_fahrenheit.csv", new FahrenheitReadingFactory());
        manager.processSensorReading(new SensorReading(40, 41, 0.0));
        manager.processSensorReading(new SensorReading(10, 20, 7.0));
        manager.processSensorReading(new SensorReading(40, 20, 0.0));
        manager.processSensorReading(new SensorReading(42, 17, 25.0));

        assertEquals(152, station.getCountByClimate(Climate.POLAR));
        assertEquals(599, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(69, station.getCountByClimate(Climate.TROPICAL));
    }

    @Test
    void testObserverWithFilesAndUnregister() {
        WeatherStationManager manager = new WeatherStationManager();

        ClimateStation station = new ClimateStation(40, 20, 20.0);

        manager.registerObserver(station);

        manager.processSensorReadingsFromFile("data/readings_fahrenheit.csv", new FahrenheitReadingFactory());

        assertEquals(87, station.getCountByClimate(Climate.POLAR));
        assertEquals(306, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(35, station.getCountByClimate(Climate.TROPICAL));

        manager.unregisterObserver(station);

        manager.processSensorReadingsFromFile("data/readings_celsius.csv", new CelsiusReadingFactory());

        assertEquals(87, station.getCountByClimate(Climate.POLAR));
        assertEquals(306, station.getCountByClimate(Climate.TEMPERATE));
        assertEquals(35, station.getCountByClimate(Climate.TROPICAL));
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