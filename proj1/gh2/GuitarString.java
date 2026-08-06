package gh2;


import deque.Deque;
import deque.LinkedListDeque;

//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /**
     * Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday.
     */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    private Deque<Double> buffer;
    private int capacity;


    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {

        capacity = (int) Math.round(SR / frequency);
        buffer = new LinkedListDeque<Double>();
        for (int i = 0; i < capacity; i++) {
            buffer.addLast(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {

        double r;
        for (int i = 0; i < capacity; i++) {
            buffer.removeLast();
        }

        for (int i = 0; i < capacity; i++) {
            r = Math.random() - 0.5;
            buffer.addLast(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {

        double sampleFirst = buffer.removeFirst();
        double sampleNext = buffer.get(0);
        double newSample = ((sampleFirst + sampleNext) / 2) * DECAY;
        buffer.addLast(newSample);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {

        return buffer.get(0);
    }
}
