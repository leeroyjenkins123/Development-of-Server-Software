package org.example;

public class CreateStatisticsText {
    public String getIntStatistics(IntStatistics intStatistics, StatisticMode mode){
        if (mode == StatisticMode.SHORT) {
            return "Целые числа: количество = " + (intStatistics.getCount() > 0 ? intStatistics.getCount() : "N/A");
        } else {
            return "Целые числа: количество = " + (intStatistics.getCount() > 0 ? intStatistics.getCount() : "N/A") +
                    ", минимум = " + (intStatistics.getCount() > 0 ? intStatistics.getMin() : "N/A") +
                    ", максимум = " + (intStatistics.getCount() > 0 ? intStatistics.getMax() : "N/A") +
                    ", сумма = " + (intStatistics.getCount() > 0 ? intStatistics.getSum() : "N/A") +
                    ", среднее = " + (intStatistics.getCount() > 0 ? intStatistics.getAverage() : "N/A");
        }
    }

    public String getFloatStatistics(FloatStatistics floatStatistics, StatisticMode mode){
        if (mode == StatisticMode.SHORT) {
            return "Вещественные числа: количество = " + (floatStatistics.getCount() > 0 ? floatStatistics.getCount() : "N/A");
        } else {
            return "Вещественные числа: количество = " + (floatStatistics.getCount() > 0 ? floatStatistics.getCount() : "N/A") +
                    ", минимум = " + (floatStatistics.getCount() > 0 ? floatStatistics.getMin() : "N/A") +
                    ", максимум = " + (floatStatistics.getCount() > 0 ? floatStatistics.getMax() : "N/A") +
                    ", сумма = " + (floatStatistics.getCount() > 0 ? floatStatistics.getSum() : "N/A") +
                    ", среднее = " + (floatStatistics.getCount() > 0 ? floatStatistics.getAverage() : "N/A");
        }
    }

    public String getStringStatistics(StringStatistics stringStatistics, StatisticMode mode){
        if (mode == StatisticMode.SHORT) {
            return "Строки: количество = " + (stringStatistics.getCount() > 0 ? stringStatistics.getCount() : "N/A");
        } else {
            return "Строки: количество = " + (stringStatistics.getCount() > 0 ? stringStatistics.getCount() : "N/A") +
                    ", длина самой короткой = " + (stringStatistics.getCount() > 0 ? stringStatistics.getMinLength() : "N/A") +
                    ", длина самой длинной = " + (stringStatistics.getCount() > 0 ? stringStatistics.getMaxLength() : "N/A");
        }
    }
}
