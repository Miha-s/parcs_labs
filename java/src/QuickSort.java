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
        if (args.length != 1) {
            System.err.println("Usage: QuickSort <number-of-workers>");
            System.exit(1);
        }
        int k = Integer.parseInt(args[0]);
    
        task curtask = new task();
        curtask.addJarFile("QuickSort.jar");
        AMInfo info = new AMInfo(curtask, null);
    
    
        System.err.println("Forwarding parts to workers...");
        startTimer();
        channel[] channels = new channel[k];
        for (int i = 0; i < k; i++) {
            int[] part = {1};
            point p = info.createPoint();
            channel c = p.createChannel();
            p.execute("QuickSort");
            c.write(part);
            channels[i] = c;
        }
        stopTimer();
    
        System.err.println("Getting results from workers...");
        startTimer();
        int[][] parts = new int[k][];
        for (int i = 0; i < k; i++) {
            parts[i] = (int[]) channels[i].readObject();
        }
        stopTimer();

    
        curtask.end();
    }


    public void run(AMInfo info) {
        int[] arr = (int[])info.parent.readObject();
        info.parent.write(arr);
    }
}