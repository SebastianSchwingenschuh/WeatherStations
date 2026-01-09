import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherStationManagerTest {
    @Test
    void testMultipleObservers() {
        WeatherStationManager manager = new WeatherStationManager();

        AlarmStation alarmStationA = new AlarmStation(30, 40, 10.0, -10.0, 30.0);
        AlarmStation alarmStationB = new AlarmStation(5, 10, 7.0, 5.0, 27.5);
        ClimateStation climateStationA = new ClimateStation(40, 20, 20.0);
        ClimateStation climateStationB = new ClimateStation(47, 10, 15.0);
        StatisticsStation statisticsStationA = new StatisticsStation(50, 0, 5.0);
        StatisticsStation statisticsStationB = new StatisticsStation(25, 25, 12.0);

        manager.registerObserver(alarmStationA);
        manager.registerObserver(alarmStationB);
        manager.registerObserver(climateStationA);
        manager.registerObserver(climateStationB);
        manager.registerObserver(statisticsStationA);
        manager.registerObserver(statisticsStationB);

        manager.processSensorReading(new SensorReading(30, 40, 30.0));
        manager.processSensorReading(new SensorReading(50, 0, 3.0));
        manager.processSensorReading(new SensorReading(46, 4, 11.0));
        manager.processSensorReading(new SensorReading(50, 6, 13.0));
        manager.processSensorReading(new SensorReading(26, 44, 29.9));
        manager.processSensorReading(new SensorReading(33, 45, -11.0));
        manager.processSensorReading(new SensorReading(53, 3, -1.0));
        manager.processSensorReadingsFromFile("data/readings_fahrenheit.csv", new FahrenheitReadingFactory());
        manager.processSensorReading(new SensorReading(40, 50, 39.0));
        manager.processSensorReading(new SensorReading(55, 35, 13.0));
        manager.processSensorReading(new SensorReading(30, 145, -19.0));
        manager.processSensorReading(new SensorReading(40, 41, 0.0));
        manager.processSensorReading(new SensorReading(40, 100, -5.0));
        manager.processSensorReading(new SensorReading(40, 10, 4.5));
        manager.processSensorReading(new SensorReading(57, 32, 25.0));
        manager.processSensorReading(new SensorReading(30, 51, -11.0));
        manager.processSensorReading(new SensorReading(35, 25, 24.9));
        manager.processSensorReading(new SensorReading(40, 21, -5.0));
        manager.processSensorReading(new SensorReading(10, 20, 7.0));
        manager.processSensorReading(new SensorReading(46, 3, 0.0));
        manager.processSensorReading(new SensorReading(25, 13, 2.0));
        manager.processSensorReading(new SensorReading(40, 40, 0.1));
        manager.processSensorReading(new SensorReading(55, 32, -14.5));
        manager.processSensorReadingsFromFile("data/readings_celsius.csv", new CelsiusReadingFactory());
        manager.processSensorReading(new SensorReading(50, 10, 33.0));
        manager.processSensorReading(new SensorReading(42, 17, 25.0));
        manager.processSensorReading(new SensorReading(55, 8, 13.0));
        manager.processSensorReading(new SensorReading(50, 5, 5.0));
        manager.processSensorReading(new SensorReading(24, 36, -9.9));
        manager.processSensorReading(new SensorReading(48, 3, 6.0));
        manager.processSensorReading(new SensorReading(0, 0, 31.0));
        manager.processSensorReading(new SensorReading(0, 0, 34.5));
        manager.processSensorReading(new SensorReading(0, 0, -14.0));
        manager.processSensorReading(new SensorReading(25, 40, -10.0));
        manager.processSensorReading(new SensorReading(35, 41, 38.0));
        manager.processSensorReading(new SensorReading(40, 20, 0.0));
        manager.processSensorReading(new SensorReading(10, 40, 31.0));
        manager.processSensorReading(new SensorReading(32, 42, 11.0));

        assertEquals(21, alarmStationA.getAlarmsRaised());
        assertEquals(47, alarmStationB.getAlarmsRaised());

        assertEquals(154, climateStationA.getCountByClimate(Climate.POLAR));
        assertEquals(605, climateStationA.getCountByClimate(Climate.TEMPERATE));
        assertEquals(69, climateStationA.getCountByClimate(Climate.TROPICAL));
        assertEquals(63, climateStationB.getCountByClimate(Climate.POLAR));
        assertEquals(228, climateStationB.getCountByClimate(Climate.TEMPERATE));
        assertEquals(26, climateStationB.getCountByClimate(Climate.TROPICAL));

        assertEquals(16, statisticsStationA.getTemperatures().size());
        assertEquals(22.6, statisticsStationA.getHighestRecordedTemperature());
        assertEquals(-1.6, statisticsStationA.getLowestRecordedTemperature());
        assertEquals(177, statisticsStationB.getTemperatures().size());
        assertEquals(37.0, statisticsStationB.getHighestRecordedTemperature());
        assertEquals(-23.0, statisticsStationB.getLowestRecordedTemperature());
    }
}