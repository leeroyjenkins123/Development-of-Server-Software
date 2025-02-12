package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FloatStatisticsTest {
    @Test
    void addValues() {
        FloatStatistics stats = new FloatStatistics();
        stats.add(5.5);
        stats.add(2.2);
        stats.add(7.7);

        assertEquals(3, stats.getCount());
        assertEquals(2.2, stats.getMin(), 0.001);
        assertEquals(7.7, stats.getMax(), 0.001);
        assertEquals(15.4, stats.getSum(), 0.001);
    }

    @Test
    void emptyStats() {
        FloatStatistics stats = new FloatStatistics();
        assertEquals(0, stats.getCount());
        assertEquals(0.0, stats.getAverage());
    }
}
