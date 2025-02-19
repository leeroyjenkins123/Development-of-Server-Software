package org.example;

public class StaticHolder {
    private static final int COUNT_THREAD = 3;
    private static final int PROGRESSBAR_LENGTH = 20;
    private static final int STEP_SLEEP = 500;

    public static int getCountThread() {
        return COUNT_THREAD;
    }

    public static int getProgressbarLength(){
        return PROGRESSBAR_LENGTH;
    }

    public static int getStepSleep(){
        return STEP_SLEEP;
    }
}
