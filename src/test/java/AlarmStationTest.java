import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class AlarmStationTest {
    @Test
    void testInheritance() {
        assertEquals(true, Modifier.isAbstract(WeatherStation.class.getModifiers()));

        WeatherStation station = new AlarmStation(30, 40, 10.0, -10.0, 30.0);
    }

    @Test
    void testConstructorValidValues() {
        AlarmStation station = new AlarmStation(30, 40, 10.0, -10.0, 30.0);

        assertEquals(30, station.getX());
        assertEquals(40, station.getY());
        assertEquals(10.0, station.getSensorRange(), 0.001);
        assertEquals(-10.0, station.getTriggerLow());
        assertEquals(30.0, station.getTriggerHigh());
        assertEquals(0, station.getAlarmsRaised());
    }

    @Test
    void testConstructorZeroSensorRange() {
        assertThrows(IllegalArgumentException.class, () -> {
            AlarmStation station = new AlarmStation(30, 40, 0.0, -10.0, 30.0);
        });
    }

    @Test
    void testConstructorNegativeSensorRange() {
        assertThrows(IllegalArgumentException.class, () -> {
            AlarmStation station = new AlarmStation(30, 40, -1.0, -10.0, 30.0);
        });
    }

    @Test
    void testCalculateDistanceTo() {
        AlarmStation station = new AlarmStation(30, 40, 10.0, -10.0, 30.0);

        assertEquals(0.0, station.calculateDistanceTo(30, 40), 0.01);
        assertEquals(5.8309, station.calculateDistanceTo(33, 45), 0.01);
        assertEquals(10.0, station.calculateDistanceTo(30, 50), 0.01);
        assertEquals(11.0, station.calculateDistanceTo(30, 51), 0.01);
        assertEquals(20.0, station.calculateDistanceTo(10, 40), 0.01);
        assertEquals(105.0, station.calculateDistanceTo(30, 145), 0.01);
        assertEquals(7.0710, station.calculateDistanceTo(35, 45), 0.01);
        assertEquals(14.1421, station.calculateDistanceTo(40, 50), 0.01);
        assertEquals(26.2488, station.calculateDistanceTo(55, 32), 0.01);
        assertEquals(50.0, station.calculateDistanceTo(0, 0), 0.01);
    }

    @Test
    void testIsInRange() {
        AlarmStation station = new AlarmStation(30, 40, 10.0, -10.0, 30.0);

        assertEquals(true, station.isInRange(new SensorReading(30, 40, 5.0)));
        assertEquals(true, station.isInRange(new SensorReading(33, 45, 5.0)));
        assertEquals(true, station.isInRange(new SensorReading(30, 50, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(30, 51, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(10, 40, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(30, 145, 5.0)));
        assertEquals(true, station.isInRange(new SensorReading(35, 45, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(40, 50, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(55, 32, 5.0)));
        assertEquals(false, station.isInRange(new SensorReading(0, 0, 5.0)));
    }

    @Test
    void testAlarmsRaisedInRange() {
        AlarmStation station = new AlarmStation(30, 40, 10.0, -10.0, 30.0);

        assertEquals(0, station.getAlarmsRaised());

        //under min limit
        station.update(new SensorReading(33, 45, -11.0));

        assertEquals(1, station.getAlarmsRaised());

        //exactly upper limit
        station.update(new SensorReading(30, 40, 30.0));

        assertEquals(2, station.getAlarmsRaised());

        //between limits
        station.update(new SensorReading(24, 36, -9.9));

        assertEquals(2, station.getAlarmsRaised());

        //above upper limit
        station.update(new SensorReading(35, 41, 38.0));

        assertEquals(3, station.getAlarmsRaised());

        //between limits
        station.update(new SensorReading(26, 44, 29.9));

        assertEquals(3, station.getAlarmsRaised());

        //exactly lower limit
        station.update(new SensorReading(25, 40, -10.0));

        assertEquals(4, station.getAlarmsRaised());

        //between limits
        station.update(new SensorReading(32, 42, 11.0));

        assertEquals(4, station.getAlarmsRaised());
    }

    @Test
    void testAlarmsRaisedOutOfRange() {
        AlarmStation station = new AlarmStation(30, 40, 10.0, -10.0, 30.0);

        station.update(new SensorReading(30, 51, -11.0));
        station.update(new SensorReading(10, 40, 31.0));
        station.update(new SensorReading(30, 145, -19.0));
        station.update(new SensorReading(40, 50, 39.0));
        station.update(new SensorReading(55, 32, -14.5));
        station.update(new SensorReading(0, 0, 34.5));

        assertEquals(0, station.getAlarmsRaised());
    }

    @Test
    void testObserver() {
        WeatherStationManager manager = new WeatherStationManager();

        AlarmStation station = new AlarmStation(30, 40, 10.0, -10.0, 30.0);

        manager.registerObserver(station);

        manager.processSensorReading(new SensorReading(30, 145, -19.0));
        manager.processSensorReading(new SensorReading(32, 42, 11.0));
        manager.processSensorReading(new SensorReading(26, 44, 29.9));
        manager.processSensorReading(new SensorReading(30, 51, -11.0));
        manager.processSensorReading(new SensorReading(0, 0, 34.5));
        manager.processSensorReading(new SensorReading(33, 45, -11.0));
        manager.processSensorReading(new SensorReading(24, 36, -9.9));
        manager.processSensorReading(new SensorReading(35, 41, 38.0));
        manager.processSensorReading(new SensorReading(30, 40, 30.0));
        manager.processSensorReading(new SensorReading(55, 32, -14.5));
        manager.processSensorReading(new SensorReading(25, 40, -10.0));
        manager.processSensorReading(new SensorReading(40, 50, 39.0));
        manager.processSensorReading(new SensorReading(10, 40, 31.0));

        assertEquals(4, station.getAlarmsRaised());
    }

    @Test
    void testObserverRegisterUnregister() {
        WeatherStationManager manager = new WeatherStationManager();

        AlarmStation station = new AlarmStation(30, 40, 10.0, -10.0, 30.0);

        manager.registerObserver(station);

        manager.processSensorReading(new SensorReading(30, 145, -19.0));
        manager.processSensorReading(new SensorReading(32, 42, 11.0));
        manager.processSensorReading(new SensorReading(26, 44, 29.9));
        manager.processSensorReading(new SensorReading(30, 51, -11.0));
        manager.processSensorReading(new SensorReading(33, 45, -11.0));

        manager.unregisterObserver(station);

        manager.processSensorReading(new SensorReading(0, 0, 34.5));
        manager.processSensorReading(new SensorReading(24, 36, -9.9));
        manager.processSensorReading(new SensorReading(35, 41, 38.0));
        manager.processSensorReading(new SensorReading(30, 40, 30.0));

        manager.registerObserver(station);

        manager.processSensorReading(new SensorReading(55, 32, -14.5));
        manager.processSensorReading(new SensorReading(25, 40, -10.0));
        manager.processSensorReading(new SensorReading(40, 50, 39.0));
        manager.processSensorReading(new SensorReading(10, 40, 31.0));

        assertEquals(2, station.getAlarmsRaised());
    }

    @Test
    void testObserverWithFiles() {
        WeatherStationManager manager = new WeatherStationManager();

        AlarmStation station = new AlarmStation(30, 40, 10.0, -10.0, 30.0);

        manager.registerObserver(station);

        manager.processSensorReadingsFromFile("data/readings_celsius.csv", new CelsiusReadingFactory());

        assertEquals(8, station.getAlarmsRaised());

        manager.processSensorReadingsFromFile("data/readings_fahrenheit.csv", new FahrenheitReadingFactory());

        assertEquals(17, station.getAlarmsRaised());
    }

    @Test
    void testObserverWithFilesAndManualAdding() {
        WeatherStationManager manager = new WeatherStationManager();

        AlarmStation station = new AlarmStation(30, 40, 10.0, -10.0, 30.0);

        manager.registerObserver(station);

        manager.processSensorReading(new SensorReading(30, 145, -19.0));
        manager.processSensorReading(new SensorReading(32, 42, 11.0));
        manager.processSensorReading(new SensorReading(26, 44, 29.9));
        manager.processSensorReading(new SensorReading(30, 51, -11.0));
        manager.processSensorReading(new SensorReading(0, 0, 34.5));
        manager.processSensorReadingsFromFile("data/readings_celsius.csv", new CelsiusReadingFactory());
        manager.processSensorReading(new SensorReading(33, 45, -11.0));
        manager.processSensorReading(new SensorReading(24, 36, -9.9));
        manager.processSensorReading(new SensorReading(35, 41, 38.0));
        manager.processSensorReading(new SensorReading(30, 40, 30.0));
        manager.processSensorReadingsFromFile("data/readings_fahrenheit.csv", new FahrenheitReadingFactory());
        manager.processSensorReading(new SensorReading(55, 32, -14.5));
        manager.processSensorReading(new SensorReading(25, 40, -10.0));
        manager.processSensorReading(new SensorReading(40, 50, 39.0));
        manager.processSensorReading(new SensorReading(10, 40, 31.0));

        assertEquals(21, station.getAlarmsRaised());
    }

    @Test
    void testObserverWithFilesAndUnregister() {
        WeatherStationManager manager = new WeatherStationManager();

        AlarmStation station = new AlarmStation(30, 40, 10.0, -10.0, 30.0);

        manager.registerObserver(station);

        manager.processSensorReadingsFromFile("data/readings_celsius.csv", new CelsiusReadingFactory());

        assertEquals(8, station.getAlarmsRaised());

        manager.unregisterObserver(station);

        manager.processSensorReadingsFromFile("data/readings_fahrenheit.csv", new FahrenheitReadingFactory());

        assertEquals(8, station.getAlarmsRaised());
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