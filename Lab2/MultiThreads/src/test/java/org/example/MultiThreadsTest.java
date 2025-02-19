package org.example;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class MultiThreadsTest {

    @Test
    public void testRun() throws InterruptedException {
        MultiThreads multiThreads = new MultiThreads(1);
        Thread thread = new Thread(multiThreads);
        thread.start();
        thread.join();

        // Проверяем, что поток завершился и время выполнения больше 0
        assertTrue(thread.isAlive() == false);
    }
}