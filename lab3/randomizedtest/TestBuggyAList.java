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
}
