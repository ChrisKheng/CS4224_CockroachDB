package cs4224.utils;

import java.util.Collections;
import java.util.List;

public class Statistics {

    private static long computeMedian(List<Long> lst) {
        if (lst.size() == 0) return 0;
        long mid = lst.get(lst.size() / 2);
        if (lst.size() % 2 != 0) return mid;
        long mid_ = lst.get(lst.size() / 2 - 1);
        return  (mid+mid_)/2;
    }

    private static long computePercentile(List<Long> lst, int ptl) {

        int i = lst.size() * ptl / 100;
        i = Math.min(i, lst.size());
        return lst.get(i);
    }

    public static void computeTimeStatistics(List<Long> transactionTimes, long totalTime) {
        // Prevents division by a small number
        totalTime = Math.max(totalTime, 1);
        Collections.sort(transactionTimes);
        long totalTransactionTime = transactionTimes.stream().reduce(Long::sum).orElse(0L);
        int noOfTransactions = transactionTimes.size();

        long throughput = noOfTransactions / totalTime;
        long avgLatency = totalTransactionTime/noOfTransactions;
        long medianLatency = computeMedian(transactionTimes);
        long ninetyFivePtlLatency = computePercentile(transactionTimes, 95);
        long ninetyNinePtlLatency = computePercentile(transactionTimes, 99);


        System.err.println("\n======================================================================");
        System.err.println("Performance measurements: ");
        System.err.printf("a. Number of executed transactions: %d\n", noOfTransactions);
        System.err.printf("b. Total transaction execution time: %ds\n", totalTime);
        System.err.printf("c. Transaction throughput: %d per second\n", throughput);
        System.err.printf("d. Average transaction latency: %dms\n", avgLatency);
        System.err.printf("e. Median transaction latency: %dms\n", medianLatency);
        System.err.printf("f. 95th percentile transaction latency: %dms\n", ninetyFivePtlLatency);
        System.err.printf("g. 99th percentile transaction latency: %dms\n", ninetyNinePtlLatency);
        System.err.println("======================================================================");

        System.err.println("Raw statistics data:");
        System.err.printf("%d,%d,%d,%d,%d,%d,%d\n",
                noOfTransactions, totalTime, throughput, avgLatency, medianLatency, ninetyFivePtlLatency, ninetyNinePtlLatency);
    }
}
