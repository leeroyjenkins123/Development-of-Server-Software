package org.example;

public class MultiThreads implements Runnable {
    private final int threadNumber;

    public MultiThreads(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    @Override
    public void run() {
        long threadId = Thread.currentThread().threadId();
        long startTimer = System.currentTimeMillis();

        for (int j = 1; j <= StaticHolder.getProgressbarLength(); j++) {
            ProgressBar.updateProgressBar(threadNumber, threadId, j, StaticHolder.getProgressbarLength());
            try {
                Thread.sleep(StaticHolder.getStepSleep());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long stopTimer = System.currentTimeMillis();

        synchronized (System.out) {
            System.out.printf("Thread %d (ID: %d) finished at %d ms\n", threadNumber, threadId, (stopTimer - startTimer));
        }
    }
}
