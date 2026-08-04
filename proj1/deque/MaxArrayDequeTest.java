package deque;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.Optional;

import static org.junit.Assert.*;

class NumComparator implements Comparator<Integer> {
    public int compare(Integer o1, Integer o2) {
       return o1.compareTo(o2);
    }
}



public class MaxArrayDequeTest {

    @Test
    public void simpleMaxTest() {
        MaxArrayDeque<Integer> arr1 = new MaxArrayDeque<>(new NumComparator());
        for (int i = 0; i < 100; i++) {
            arr1.addLast(i);
        }
        Assert.assertEquals((Integer) 99, arr1.max());
    }
}
