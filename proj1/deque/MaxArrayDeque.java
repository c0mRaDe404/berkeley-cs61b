package deque;


import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> c;

    public MaxArrayDeque(Comparator<T> c) {
        this.c = c;
    }

    public T max(Comparator<T> cc) {
        if (isEmpty()) {
            return null;
        } else {
            T max = get(0);
            for (int i = 1; i < size(); i++) {
                int result = cc.compare(max, get(i));
                if (result < 0) {
                    max = get(i);
                }
            }
            return max;
        }
    }

    public T max() {
        return max(c);
    }



}
