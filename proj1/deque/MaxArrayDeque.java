package deque;

import java.util.Comparator;

class MaxArrayDeque<T> extends ArrayDeque<T> {

    Comparator<T> c;

    public MaxArrayDeque (Comparator<T> c) {
        this.c = c;
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        } else {
            T max = get(0);
            for (int i = 1; i < size(); i++) {
                if (c.compare(max, get(i)) < 0) {
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
