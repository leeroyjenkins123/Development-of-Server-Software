package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

public class CommandLineParserTest {
    @Test
    void parseValidArgs(){
        String[] args = {"-o", "output", "-p", "result_", "-a", "-s", "file1.txt", "file2.txt"};
        CommandLineArgs commandLineArgs = CommandLineParser.parse(args);

        assertEquals("output", commandLineArgs.getOutputPath());
        assertEquals("result_", commandLineArgs.getFilePrefix());
        assertTrue(commandLineArgs.getAppendMode());
        assertEquals(StatisticMode.SHORT, commandLineArgs.getStatType());
        assertEquals(List.of("file1.txt", "file2.txt"), commandLineArgs.getInputFiles());
    }

    @Test
    void parseConflictFlags() {
        String[] args = {"-s", "-f", "file.txt"};
        assertThrows(IllegalArgumentException.class, () -> CommandLineParser.parse(args));
    }

    @Test
    void parseUnknownOption() {
        String[] args = {"-x", "file.txt"};
        assertThrows(IllegalArgumentException.class, () -> CommandLineParser.parse(args));
    }

    @Test
    void parseNoInputFiles() {
        String[] args = {"-o", "output"};
        assertThrows(IllegalArgumentException.class, () -> CommandLineParser.parse(args));
    }
}
