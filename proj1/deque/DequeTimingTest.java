package deque;

import edu.princeton.cs.algs4.Stopwatch;

public class DequeTimingTest {

    private static void printTimingTable(ArrayDeque<Integer> Ns, ArrayDeque<Double> times, ArrayDeque<Integer> opCounts) {
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



    public static void timeLinkedListDequeConstruction() {
        ArrayDeque<Integer> N = new ArrayDeque<>();
        ArrayDeque<Double> times = new ArrayDeque<>();
        ArrayDeque<Integer> opCounts = new ArrayDeque<>();
        for (int i = 1000; i <= 128000; i *= 2) {
            LinkedListDeque<Integer> testLinkedList = new LinkedListDeque<>();
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < i; j++) {
                testLinkedList.addLast(j);
            }
            times.addLast(sw.elapsedTime());
            N.addLast(i);
            opCounts.addLast(i);
        }

        printTimingTable(N, times, opCounts);
    }

    public static void timeArrayDequeConstruction() {
        ArrayDeque<Integer> N = new ArrayDeque<>();
        ArrayDeque<Double> times = new ArrayDeque<>();
        ArrayDeque<Integer> opCounts = new ArrayDeque<>();
        for (int i = 1000; i <= 128000; i *= 2) {
            ArrayDeque<Integer> testArray = new ArrayDeque<>();
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < i; j++) {
                testArray.addLast(j);
            }
            times.addLast(sw.elapsedTime());
            N.addLast(i);
            opCounts.addLast(i);
        }

        printTimingTable(N, times, opCounts);
    }


    public static void LinkedListDequeTimeGetMiddle() {
        ArrayDeque<Integer> N = new ArrayDeque<>();
        ArrayDeque<Double> times = new ArrayDeque<>();
        ArrayDeque<Integer> opCounts = new ArrayDeque<>();
        int opCount = 10000;
        for (int i = 1000; i <= 128000; i *= 2) {
            LinkedListDeque<Integer> testArray = new LinkedListDeque<>();
            //Stopwatch sw = new Stopwatch();
            for (int j = 0; j < i; j++) {
                testArray.addLast(j);
            }
            Stopwatch sw = new Stopwatch();

            for (int k = 0; k < opCount; k++) {
                testArray.get(testArray.size() - 1);
            }

            times.addLast(sw.elapsedTime());
            N.addLast(i);
            opCounts.addLast(opCount);
        }

        printTimingTable(N, times, opCounts);
    }


    public static void ArrayDequeTimeGetMiddle() {
        ArrayDeque<Integer> N = new ArrayDeque<>();
        ArrayDeque<Double> times = new ArrayDeque<>();
        ArrayDeque<Integer> opCounts = new ArrayDeque<>();
        int opCount = 10000;
        for (int i = 1000; i <= 128000; i *= 2) {
            ArrayDeque<Integer> testArray = new ArrayDeque<>();
            //Stopwatch sw = new Stopwatch();
            for (int j = 0; j < i; j++) {
                testArray.addLast(j);
            }
            Stopwatch sw = new Stopwatch();

            for (int k = 0; k < opCount; k++) {
                testArray.get(testArray.size() - 1);
            }

            times.addLast(sw.elapsedTime());
            N.addLast(i);
            opCounts.addLast(opCount);
        }

        printTimingTable(N, times, opCounts);
    }


    public static void main(String[] args) {
        System.out.println();
        System.out.println("ArrayDequeConstruction:");
        timeArrayDequeConstruction();


        System.out.println();
        System.out.println("LinkedListDequeConstruction:");
        timeLinkedListDequeConstruction();

        System.out.println();
        System.out.println("LinkedListDequeGetMiddle:");
        LinkedListDequeTimeGetMiddle();

        System.out.println();
        System.out.println("ArrayDequeGetMiddle:");
        ArrayDequeTimeGetMiddle();
    }

}
