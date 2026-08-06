package gh2;

import deque.ArrayDeque;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    GuitarString[] keys;
    private String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private int totalKeys;

    public GuitarHero() {
        keys = new GuitarString[keyboard.length()];
        totalKeys = keyboard.length();
    }

    private static double getFrequency(int i) {
        return 440 * Math.pow(2, (double) (i - 24) / 12);
    }

    public int getKeyIndex(char key) {
       return keyboard.indexOf(key);
    }

    public GuitarString getKey(char key) {
        return keys[getKeyIndex(key)];
    }

    public double getSuperPosition() {
        double sum = 0.0;
        for (int i = 0; i < totalKeys; i++) {
            sum +=  keys[i].sample();
        }
        return sum;
    }

    public void advanceTic() {
        for (int i = 0; i < totalKeys; i++) {
           keys[i].tic();
        }
    }

    public void initKeys() {
        for (int i = 0; i < totalKeys; i++) {
            keys[i] = new GuitarString(getFrequency(i));
        }
    }

    public static void main(String[] args) {

        GuitarHero s = new GuitarHero();
        s.initKeys();

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (s.getKeyIndex(key) < 0) {
                    System.out.println("<"+ key +"> Invalid key dude!!!");
                } else {
                    s.getKey(key).pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = s.getSuperPosition();

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            s.advanceTic();
        }
    }
}

