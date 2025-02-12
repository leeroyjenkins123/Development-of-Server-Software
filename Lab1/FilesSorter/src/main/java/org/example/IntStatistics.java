package org.example;

public class IntStatistics {
    private int count = 0;
    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;
    private long sum = 0;

    public void add(int value){
        count++;
        if(value<min){
            min = value;
        }
        if(value>max){
            max=value;
        }
        sum+=value;
    }

    public int getCount(){
        return count;
    }
    public int getMin(){
        return count > 0 ? min : 0;
    }
    public int getMax(){
        return count > 0 ? max : 0;
    }
    public double getAverage(){
        return count > 0 ? (double)sum/count : 0.0;
    }
    public long getSum(){
        return count > 0 ? sum : 0;
    }
}
