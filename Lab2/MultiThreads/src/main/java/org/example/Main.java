package org.example;

public class Main {
    public static void main(String[] args) {
        Thread[] threads = new Thread[StaticHolder.getCountThread()];
        for(int i=0;i<StaticHolder.getCountThread();i++){
            final int threadNumber = i+1;
            threads[i] = new Thread(new MultiThreads(threadNumber));
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
    }
}
