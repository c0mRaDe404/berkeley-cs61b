package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void addThreeRemoveThree() {
        AListNoResizing<Integer> list1 = new AListNoResizing<>();
        BuggyAList<Integer> list2 = new BuggyAList<>();

        for (int i = 1; i <= 1000; i++) {
            list1.addLast(i);
            list2.addLast(i);
            Assert.assertEquals(list1.getLast(), list2.getLast());
        }

        for (int i = 1; i <= 1000; i++) {
            Assert.assertEquals(list1.removeLast(), list2.removeLast());
        }
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> list1 = new AListNoResizing<>();
        BuggyAList<Integer> list2 = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                list1.addLast(randVal);
                list2.addLast(randVal);
                Assert.assertEquals(list1.getLast(), list2.getLast());
            } else if (operationNumber == 1) {
                // size
                Assert.assertEquals(list1.size(), list2.size());
            } else if (operationNumber == 2) {
                if (list1.size() > 0 && list2.size() > 0) {
                    Assert.assertEquals(list1.getLast(), list2.getLast());
                }
            } else if (operationNumber == 3) {
                if (list1.size() > 0 && list2.size() > 0) {
                    Assert.assertEquals(list1.removeLast(), list2.removeLast());
                }
            }
        }

    }
}
