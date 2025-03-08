import java.io.*;
import java.util.*;
import parcs.*;

public class PiEstimation implements AM {

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
            System.err.println("Usage: PiEstimation <total-darts> <number-of-workers>");
            System.exit(1);
        }

        int totalDarts = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);
        System.err.println("Here");

        task curtask = new task();
        curtask.addJarFile("PiEstimation.jar");
        AMInfo info = new AMInfo(curtask, null);

        System.err.println("Distributing work to workers...");
        startTimer();
        channel[] channels = new channel[k];
        int dartsPerWorker = totalDarts / k;

        for (int i = 0; i < k; i++) {
            point p = info.createPoint();
            channel c = p.createChannel();
            int[] darts = new int[1];
            darts[0] = dartsPerWorker;
            p.execute("PiEstimation");
            c.write(darts);
            channels[i] = c;
        }
        stopTimer();

        System.err.println("Collecting results from workers...");

        startTimer();
        int totalHits = 0;
        for (int i = 0; i < k; i++) {
            int newHints = ((int[]) channels[i].readObject())[0];
            totalHits += newHints;
        }
        stopTimer();

        double estimatedPi = 4.0 * totalHits / (double) totalDarts;
        System.out.println("Estimated Pi: " + estimatedPi);

        curtask.end();
    }

    public void run(AMInfo info) {
        int[] dartsPerWorker = (int[]) info.parent.readObject();
        int[]hits = new int[1];
        hits[0] = estimateHits(dartsPerWorker[0]);
        info.parent.write(hits);
    }

    private int estimateHits(int dartsPerWorker) {
        Random random = new Random();
        int hits = 0;
        for (int i = 0; i < dartsPerWorker; i++) {
            double x = random.nextDouble() * 2 - 1;
            double y = random.nextDouble() * 2 - 1;
            if (x * x + y * y <= 1) {
                hits++;
            }
        }
        return hits;
    }
}