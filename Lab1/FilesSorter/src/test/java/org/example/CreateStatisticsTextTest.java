package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CreateStatisticsTextTest {
    @Test
    void getIntStatisticsShort() {
        IntStatistics stats = new IntStatistics();
        stats.add(10);
        stats.add(20);

        String result = new CreateStatisticsText().getIntStatistics(stats, StatisticMode.SHORT);
        assertTrue(result.contains("количество = 2"));
        assertFalse(result.contains("минимум"));
    }

    @Test
    void getFloatStatisticsFull() {
        FloatStatistics stats = new FloatStatistics();
        stats.add(3.14);
        stats.add(4.1);
        stats.add(6.2);
        String expected = "Вещественные числа: количество = 3, минимум = 3.14, максимум = 6.2, сумма = 13.440000000000001, среднее = 4.48";

        String result = new CreateStatisticsText().getFloatStatistics(stats, StatisticMode.FULL);
        assertEquals(expected, result);
    }

    @Test
    void getStringStatisticsEmpty() {
        StringStatistics stats = new StringStatistics(); // Предполагается, что класс существует
        String result = new CreateStatisticsText().getStringStatistics(stats, StatisticMode.SHORT);
        assertTrue(result.contains("N/A"));
    }
}
