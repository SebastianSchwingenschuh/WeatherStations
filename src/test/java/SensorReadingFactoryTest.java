import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class SensorReadingFactoryTest {
    @Test
    void testInterfaceAndInheritance() {
        assertEquals(true, Modifier.isInterface(SensorReadingFactory.class.getModifiers()));

        CelsiusReadingFactory celsiusReadingFactory = new CelsiusReadingFactory();
        assertEquals(true, celsiusReadingFactory instanceof SensorReadingFactory);

        FahrenheitReadingFactory fahrenheitReadingFactory = new FahrenheitReadingFactory();
        assertEquals(true, fahrenheitReadingFactory instanceof SensorReadingFactory);
    }
    @Test
    void testCelsiusSensorReadingFactoryValidLine() {
        CelsiusReadingFactory readingFactory = new CelsiusReadingFactory();

        SensorReading reading = readingFactory.createFromString("175;60;31.5");

        assertEquals(175, reading.x());
        assertEquals(60, reading.y());
        assertEquals(31.5, reading.temperatureCelsius(), 0.001);
    }

    @Test
    void testCelsiusSensorReadingFactoryValidLineWithWhitespace() {
        CelsiusReadingFactory readingFactory = new CelsiusReadingFactory();

        SensorReading reading = readingFactory.createFromString("   25   ;   140   ;   -3.0     ");

        assertEquals(25, reading.x());
        assertEquals(140, reading.y());
        assertEquals(-3.0, reading.temperatureCelsius(), 0.001);
    }

    @Test
    void testCelsiusSensorReadingFactoryInvalidLineTooCold() {
        CelsiusReadingFactory readingFactory = new CelsiusReadingFactory();

        assertThrows(IllegalArgumentException.class, () -> {
            SensorReading reading = readingFactory.createFromString("175;60;-285.0");
        });
    }

    @Test
    void testCelsiusSensorReadingFactoryInvalidLineTooShort() {
        CelsiusReadingFactory readingFactory = new CelsiusReadingFactory();

        WeatherException e = assertThrows(WeatherException.class, () -> {
            SensorReading reading = readingFactory.createFromString("175;60");
        });

        assertEquals("Reading has invalid line length!", e.getMessage());
    }

    @Test
    void testCelsiusSensorReadingFactoryInvalidLineTooLong() {
        CelsiusReadingFactory readingFactory = new CelsiusReadingFactory();

        WeatherException e = assertThrows(WeatherException.class, () -> {
            SensorReading reading = readingFactory.createFromString("175;60;31.5;20%");
        });

        assertEquals("Reading has invalid line length!", e.getMessage());
    }

    @Test
    void testFahrenheitSensorReadingFactoryValidLine() {
        FahrenheitReadingFactory readingFactory = new FahrenheitReadingFactory();

        SensorReading reading = readingFactory.createFromString("90;120;-13.0");

        assertEquals(90, reading.x());
        assertEquals(120, reading.y());
        assertEquals(-25.0, reading.temperatureCelsius(), 0.001);
    }

    @Test
    void testFahrenheitSensorReadingFactoryValidLineWithRounding() {
        FahrenheitReadingFactory readingFactory = new FahrenheitReadingFactory();

        SensorReading reading = readingFactory.createFromString("90;120;13.5");

        assertEquals(90, reading.x());
        assertEquals(120, reading.y());
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Use Math.round() inside the factory!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        assertEquals(-10.0, reading.temperatureCelsius(), 0.001);
    }

    @Test
    void testFahrenheitSensorReadingFactoryValidLineWithWhitespace() {
        FahrenheitReadingFactory readingFactory = new FahrenheitReadingFactory();

        SensorReading reading = readingFactory.createFromString("   90   ;     120      ;    55.4   ");

        assertEquals(90, reading.x());
        assertEquals(120, reading.y());
        assertEquals(13.0, reading.temperatureCelsius(), 0.001);
    }

    @Test
    void testFahrenheitSensorReadingFactoryInvalidLineTooCold() {
        FahrenheitReadingFactory readingFactory = new FahrenheitReadingFactory();

        assertThrows(IllegalArgumentException.class, () -> {
            SensorReading reading = readingFactory.createFromString("90;120;-461.2");
        });
    }

    @Test
    void testFahrenheitSensorReadingFactoryInvalidLineTooShort() {
        FahrenheitReadingFactory readingFactory = new FahrenheitReadingFactory();

        WeatherException e = assertThrows(WeatherException.class, () -> {
            SensorReading reading = readingFactory.createFromString("90;14.0");
        });

        assertEquals("Reading has invalid line length!", e.getMessage());
    }

    @Test
    void testFahrenheitSensorReadingFactoryInvalidLineTooLong() {
        FahrenheitReadingFactory readingFactory = new FahrenheitReadingFactory();

        WeatherException e = assertThrows(WeatherException.class, () -> {
            SensorReading reading = readingFactory.createFromString("90;120;50%;-13.0");
        });

        assertEquals("Reading has invalid line length!", e.getMessage());
    }
}