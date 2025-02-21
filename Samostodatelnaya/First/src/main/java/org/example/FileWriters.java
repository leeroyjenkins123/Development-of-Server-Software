package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class FileWriters implements Runnable{
    private final FileWriter[] files;
    private final int files_count;
    private final Semaphore semaphore;
    private static ReentrantLock[] files_lock;

    public FileWriters(FileWriter[] files, int files_count, Semaphore semaphore, ReentrantLock[] files_lock) {
        this.files = files;
        this.files_count = files_count;
        this.semaphore = semaphore;
        this.files_lock = files_lock;
    }

    @Override
    public void run(){
        long threadId = Thread.currentThread().threadId();

        for(int i=0;i<files_count;i++){
            files_lock[i].lock();
            int fileIndex = i;
            try {
                semaphore.acquire();

                FileWriter writer = files[fileIndex];

                synchronized (writer) {
                    writer.write("Поток " + threadId + " записал: " + (fileIndex + 100) + "\n");
                    writer.flush();
                    System.out.printf("Поток %d записал число %d в файл text%d.txt.%n",
                            threadId, (fileIndex + 100), (fileIndex + 1));
                }

                Thread.sleep(1000 + (int) (Math.random() * 1000));
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
                files_lock[i].unlock();// Освобождаем семафор
            }
        }
    }
}
