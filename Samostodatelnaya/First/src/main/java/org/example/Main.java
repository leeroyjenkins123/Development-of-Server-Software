package org.example;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.io.FileWriter;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    private static final int threads_count = 10;
    private static final int files_count = 5;
    static FileOpener fileOpener = new FileOpener();

    private static ReentrantLock[] files_lock = new ReentrantLock[files_count];
    private static Semaphore semaphore = new Semaphore(files_count, true);

    public static void main(String[] args) {
        Thread[] threads = new Thread[threads_count];
        var files = fileOpener.GetFiles("text", files_count);

        for(int i=0;i<files_count;i++){
            files_lock[i] = new ReentrantLock();
        }

        for(int i=0;i<threads_count;i++){
            final int thread_id = i+1;
            threads[i] = new Thread(new FileWriters(files, files_count, semaphore, files_lock));
            threads[i].start();
        }

        for(Thread thread: threads){
            try{
                thread.join();
            }
            catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }

        closeAllFiles(files);
        System.out.println("Все потоки завершили работу.");
    }

    private static void closeAllFiles(FileWriter[] writers){

        for (int i = 0; i < files_count; i++) {
            try {
                if (writers[i] != null) {
                    writers[i].close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}