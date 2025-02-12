package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StringStatisticsTest {
    @Test
    void addLine() {
        StringStatistics stats = new StringStatistics();
        stats.add("Hello");
        stats.add("Government");
        stats.add("Tea");

        assertEquals(3, stats.getCount());
        assertEquals(10, stats.getMaxLength());
        assertEquals(3, stats.getMinLength());
    }
}
