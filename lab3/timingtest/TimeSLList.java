package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        AList<Integer> N = new AList<> ();
        AList<Double> times = new AList<> ();
        AList<Integer> opCounts = new AList<> ();
        int opCount = 10000;
        for (int i = 1000; i <= 128000/4; i *= 2) {
            SLList<Integer> testArray = new SLList<>();
            //Stopwatch sw = new Stopwatch();
            for (int j = 0; j < i; j++) {
                testArray.addLast(j);
            }
            Stopwatch sw = new Stopwatch();
            for (int k = 0; k < opCount; k++) {
                testArray.getLast();
            }
            times.addLast(sw.elapsedTime());
            N.addLast(i);
            opCounts.addLast(opCount);
        }

        printTimingTable(N, times, opCounts);
    }

}
