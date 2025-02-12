package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

public class Tests {
    @Test
    void TestParseCorrect(){
        String[] args = {"-o", "output", "-p", "result_", "-a", "-s", "file1.txt", "file2.txt"};
        CommandLineArgs commandLineArgs = CommandLineParser.parse(args);

        assertEquals("output", commandLineArgs.getOutputPath());
        assertEquals("result_", commandLineArgs.getFilePrefix());
        assertTrue(commandLineArgs.getAppendMode());
        assertEquals(StatisticMode.SHORT, commandLineArgs.getStatType());
        assertEquals(List.of("file1.txt", "file2.txt"), commandLineArgs.getInputFiles());
    }

    @Test
    void TestParseConflict() {
        String[] args = {"-s", "-f", "file.txt"};
        assertThrows(IllegalArgumentException.class, () -> CommandLineParser.parse(args));
    }

    @Test
    void TestParseUnknownOption() {
        String[] args = {"-x", "file.txt"};
        assertThrows(IllegalArgumentException.class, () -> CommandLineParser.parse(args));
    }

    @Test
    void TestParseNoInputFiles() {
        String[] args = {"-o", "output"};
        assertThrows(IllegalArgumentException.class, () -> CommandLineParser.parse(args));
    }

    @Test
    void TestAddIntNumbers() {
        IntStatistics stats = new IntStatistics();
        stats.add(10);
        stats.add(20);
        stats.add(30);

        assertEquals(3, stats.getCount());
        assertEquals(10, stats.getMin());
        assertEquals(60, stats.getSum());
        assertEquals(30, stats.getMax());
        assertEquals(20.0, stats.getAverage());
    }

    @Test
    void TestFullCreationStatisticsText() {
        String expected = "Целые числа: количество = 1, минимум = 5, максимум = 5, сумма = 5, среднее = 5.0";
        IntStatistics intStatistics = new IntStatistics();
        intStatistics.add(5);
        CreateStatisticsText createStatisticsText = new CreateStatisticsText();
        String actual = createStatisticsText.getIntStatistics(intStatistics, StatisticMode.FULL);

        assertEquals(expected,actual);
    }

    @Test
    void TestShortCreationStatisticsText(){
        String expected = "Строки: количество = 1";
        StringStatistics stringStatistics = new StringStatistics();
        stringStatistics.add("Hello");
        CreateStatisticsText createStatisticsText = new CreateStatisticsText();
        String actual = createStatisticsText.getStringStatistics(stringStatistics, StatisticMode.SHORT);

        assertEquals(expected,actual);
    }
}
