package org.example;

public class ProgressBar {
    private static final String[] progressBars = new String[StaticHolder.getCountThread()];
    private static final Object lock = new Object();

    public static void updateProgressBar(int threadNumber, long threadId, int progress, int total) {
        StringBuilder barBuilder = new StringBuilder(total);

        String icon = "#";
        barBuilder.append(icon.repeat(Math.max(0, progress)));
        barBuilder.append(" ".repeat(Math.max(0, total - progress)));

        synchronized (lock) {
            progressBars[threadNumber - 1] = String.format("Thread %d (ID: %d) [%s]", threadNumber, threadId, barBuilder.toString());

            System.out.print("\033[H\033[2J");
            for (String line : progressBars) {
                if (line != null) System.out.println(line);
            }
        }
    }
}
