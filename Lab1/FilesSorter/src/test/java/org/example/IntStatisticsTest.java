package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntStatisticsTest {
    @Test
    void addValues() {
        IntStatistics stats = new IntStatistics();
        stats.add(5);
        stats.add(-3);
        stats.add(10);

        assertEquals(3, stats.getCount());
        assertEquals(-3, stats.getMin());
        assertEquals(10, stats.getMax());
        assertEquals(12, stats.getSum());
    }

    @Test
    void averageCalculation() {
        IntStatistics stats = new IntStatistics();
        stats.add(2);
        stats.add(4);
        assertEquals(3.0, stats.getAverage());
    }
}
