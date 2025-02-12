package org.example;

public class FloatStatistics {
    private int count = 0;
    private double min = Double.MAX_VALUE;
    private double max = -Double.MAX_VALUE;
    private double sum = 0.0;

    public void add(double value) {
        count++;
        if (value < min) {
            min = value;
        }
        if (value > max) {
            max = value;
        }
        sum += value;
    }

    public int getCount() {
        return count;
    }
    public double getMin() {
        return count > 0 ? min : 0.0;
    }
    public double getMax() {
        return count > 0 ? max : 0.0;
    }
    public double getSum() {
        return count > 0 ? sum : 0.0;
    }
    public double getAverage() {
        return count > 0 ? sum / count : 0.0;
    }
}
