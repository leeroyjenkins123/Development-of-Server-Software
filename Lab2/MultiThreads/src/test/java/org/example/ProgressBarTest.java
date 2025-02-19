package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class ProgressBarTest {

    @Test
    public void testUpdateProgressBar() throws NoSuchFieldException, IllegalAccessException {
        ProgressBar.updateProgressBar(1, 123, 10, 20);

        Field field = ProgressBar.class.getDeclaredField("progressBars");
        field.setAccessible(true);
        String[] progressBars = (String[]) field.get(null);

        String expected = "Thread 1 (ID: 123) [##########          ]";
        assertEquals(expected, progressBars[0]);
    }

    @Test
    public void testUpdateProgressBarFull() throws NoSuchFieldException, IllegalAccessException {
        ProgressBar.updateProgressBar(2, 456, 20, 20);

        Field field = ProgressBar.class.getDeclaredField("progressBars");
        field.setAccessible(true);
        String[] progressBars = (String[]) field.get(null);

        String expected = "Thread 2 (ID: 456) [####################]";
        assertEquals(expected, progressBars[1]);
    }

    @Test
    public void testProgressBarForMultipleThreads() throws NoSuchFieldException, IllegalAccessException {
        // Инициализация прогресс-бара для нескольких потоков
        ProgressBar.updateProgressBar(1, 123, 5, 10);
        ProgressBar.updateProgressBar(2, 456, 7, 10);
        ProgressBar.updateProgressBar(3, 789, 10, 10);

        // Используем рефлексию для получения доступа к приватному массиву
        Field field = ProgressBar.class.getDeclaredField("progressBars");
        field.setAccessible(true);  // Даем доступ к приватному полю

        String[] progressBars = (String[]) field.get(null);  // Получаем значение массива прогресс-баров

        String expected1 = "Thread 1 (ID: 123) [#####     ]";
        String expected2 = "Thread 2 (ID: 456) [#######   ]";
        String expected3 = "Thread 3 (ID: 789) [##########]";

        assertEquals(expected1, progressBars[0]);
        assertEquals(expected2, progressBars[1]);
        assertEquals(expected3, progressBars[2]);
    }
}
