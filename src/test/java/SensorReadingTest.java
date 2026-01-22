import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SensorReadingTest {
    @Test
    void testConstructor() {
        SensorReading reading = new SensorReading(50, 100, 22.0);

        assertEquals(50, reading.x());
        assertEquals(100, reading.y());
        assertEquals(22.0, reading.temperatureCelsius(), 0.001);
    }

    @Test
    void testConstructorExtremeCold() {
        SensorReading reading = new SensorReading(50, 100, -273.15);

        assertEquals(50, reading.x());
        assertEquals(100, reading.y());
        assertEquals(-273.15, reading.temperatureCelsius(), 0.001);
    }

    @Test
    void testConstructorInvalidTemperatures() {
        assertThrows(IllegalArgumentException.class, () -> {
            SensorReading reading = new SensorReading(50, 100, -273.16);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            SensorReading reading = new SensorReading(50, 100, -274.0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            SensorReading reading = new SensorReading(50, 100, -300.0);
        });
    }

    @Test
    void testComparable() {
        SensorReading readingA = new SensorReading(150, 125, 15.0);
        SensorReading readingB = new SensorReading(50, 100, 22.0);
        SensorReading readingC = new SensorReading(75, 0, 23.0);
        SensorReading readingD = new SensorReading(175, 50, 25.5);
        SensorReading readingE = new SensorReading(10, 25, 31.5);

        List<SensorReading> readings = new LinkedList<>();
        readings.add(readingA);
        readings.add(readingB);
        readings.add(readingC);
        readings.add(readingD);
        readings.add(readingE);

        Collections.shuffle(readings);

        Collections.sort(readings);

        assertEquals(readingA, readings.get(0));
        assertEquals(readingB, readings.get(1));
        assertEquals(readingC, readings.get(2));
        assertEquals(readingD, readings.get(3));
        assertEquals(readingE, readings.get(4));
    }
}