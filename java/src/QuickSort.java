import java.io.*;
import java.util.*;
import parcs.*;

public class QuickSort implements AM {

    private static long startTime = 0;

    public static void startTimer() {
        startTime = System.nanoTime();
    }

    public static void stopTimer() {
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        double seconds = timeElapsed / 1_000_000_000.0;
        System.err.println("Time passed: " + seconds + " seconds.");
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: QuickSort <total-darts> <number-of-workers>");
            System.exit(1);
        }

        long totalDarts = Long.parseLong(args[0]);
        int k = Integer.parseInt(args[1]);
        System.err.println("Here");

        task curtask = new task();
        curtask.addJarFile("QuickSort.jar");
        AMInfo info = new AMInfo(curtask, null);

        System.err.println("Distributing work to workers...");
        startTimer();
        channel[] channels = new channel[k];
        long dartsPerWorker = totalDarts / k;

        for (int i = 0; i < k; i++) {
            point p = info.createPoint();
            channel c = p.createChannel();
            p.execute("QuickSort");
            c.write(dartsPerWorker);
            channels[i] = c;
        }
        stopTimer();

        System.err.println("Collecting results from workers...");
        startTimer();
        long totalHits = 0;
        for (int i = 0; i < k; i++) {
            totalHits += (long) channels[i].readObject();
        }
        stopTimer();

        double estimatedPi = 4.0 * totalHits / (double) totalDarts;
        System.out.println("Estimated Pi: " + estimatedPi);

        curtask.end();
    }

    public void run(AMInfo info) {
        long dartsPerWorker = (long) info.parent.readObject();
        long hits = estimateHits(dartsPerWorker);
        info.parent.write(hits);
    }

    private long estimateHits(long dartsPerWorker) {
        Random random = new Random();
        long hits = 0;
        for (long i = 0; i < dartsPerWorker; i++) {
            double x = random.nextDouble() * 2 - 1;
            double y = random.nextDouble() * 2 - 1;
            if (x * x + y * y <= 1) {
                hits++;
            }
        }
        return hits;
    }
}