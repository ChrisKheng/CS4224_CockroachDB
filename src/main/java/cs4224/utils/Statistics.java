package cs4224.utils;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Statistics {

    private HashMap<String, ArrayList<Long>> timeMap = new HashMap<>();

    private static Statistics calculator = new Statistics();
    private Statistics(){}

    public static Statistics getStatisticsCalculator() {
        return calculator;
    }

    public void ingestTime(String transactionType, long transactionTime) {
        if (!timeMap.containsKey(transactionType)) {
            timeMap.put(transactionType, new ArrayList<>());
        }
        timeMap.get(transactionType).add(transactionTime);
    }

    private static double computeMedian(List<Long> lst) {
        if (lst.size() == 0) return 0;
        long mid = lst.get(lst.size() / 2);
        if (lst.size() % 2 != 0) return mid;
        long mid_ = lst.get(lst.size() / 2 - 1);
        return  (mid+mid_)/2.0;
    }

    private static long computePercentile(List<Long> lst, int ptl) {

        int i = lst.size() * ptl / 100;
        i = Math.min(i, lst.size());
        return lst.get(i);
    }

    public void computeTimeStatistics(long totalTime) {
        DecimalFormat f = new DecimalFormat("##.00");
        ArrayList<Long> transactionTimes = new ArrayList<>();
        timeMap.values().forEach(transactionTimes::addAll);
        // Prevents division by a small number
        totalTime = Math.max(totalTime, 1);
        Collections.sort(transactionTimes);
        long totalTransactionTime = transactionTimes.stream().reduce(Long::sum).orElse(0L);
        double noOfTransactions = transactionTimes.size();

        double throughput =  noOfTransactions/totalTime;
        double avgLatency = totalTransactionTime/noOfTransactions;
        double medianLatency = computeMedian(transactionTimes);
        long ninetyFivePtlLatency = computePercentile(transactionTimes, 95);
        long ninetyNinePtlLatency = computePercentile(transactionTimes, 99);


        System.err.println("\n======================================================================");
        System.err.println("Performance measurements: ");
        System.err.printf("a. Number of executed transactions: %d\n", (int) noOfTransactions);
        System.err.printf("b. Total transaction execution time: %ds\n", totalTime);
        System.err.printf("c. Transaction throughput: %s per second\n", f.format(throughput));
        System.err.printf("d. Average transaction latency: %sms\n", f.format(avgLatency));
        System.err.printf("e. Median transaction latency: %sms\n", f.format(medianLatency));
        System.err.printf("f. 95th percentile transaction latency: %dms\n", ninetyFivePtlLatency);
        System.err.printf("g. 99th percentile transaction latency: %dms\n", ninetyNinePtlLatency);
        System.err.println("======================================================================");

        System.err.println("Measurements for each transaction: ");

        for (String key : timeMap.keySet()) {
            System.err.println(key);
            ArrayList<Long> times = timeMap.get(key);
            double total = times.stream().reduce(Long::sum).orElse(0L);
            int n = times.size();
            System.err.printf("-Transaction count: %d\n", n);
            System.err.printf("-Minimum transaction latency: %dms\n", Collections.min(times));
            System.err.printf("-Maximum transaction latency: %dms\n", Collections.max(times));
            System.err.printf("-Average transaction latency: %sms\n", f.format(total/n));
            System.err.printf("-Median transaction latency: %sms\n", f.format(computeMedian(times)));
            System.err.printf("-95th percentile transaction latency: %dms\n", computePercentile(times, 95));
            System.err.printf("-99th percentile transaction latency: %dms\n", computePercentile(times, 99));
        }
    }
}
