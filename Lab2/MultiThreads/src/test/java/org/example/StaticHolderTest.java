package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class StaticHolderTest {

    @Test
    public void testGetCountThread() {
        assertEquals(3, StaticHolder.getCountThread());
    }

    @Test
    public void testGetProgressbarLength() {
        assertEquals(20, StaticHolder.getProgressbarLength());
    }

    @Test
    public void testGetStepSleep() {
        assertEquals(500, StaticHolder.getStepSleep());
    }
}