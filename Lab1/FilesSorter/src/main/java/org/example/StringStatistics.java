package org.example;

public class StringStatistics {
    private int count = 0;
    private int minLength = Integer.MAX_VALUE;
    private int maxLength = Integer.MIN_VALUE;

    public void add(String value) {
        count++;
        int len = value.length();
        if (len < minLength) {
            minLength = len;
        }
        if (len > maxLength) {
            maxLength = len;
        }
    }

    public int getCount() {
        return count;
    }
    public int getMinLength() {
        return count > 0 ? minLength : 0;
    }
    public int getMaxLength() {
        return count > 0 ? maxLength : 0;
    }
}
