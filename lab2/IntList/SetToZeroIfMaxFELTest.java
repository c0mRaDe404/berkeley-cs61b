package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SetToZeroIfMaxFELTest {

    @Test
    public void testZeroOutFELMaxes1() {
        IntList L = IntList.of(1, 22, 15);
        IntListExercises.setToZeroIfMaxFEL(L);
        assertEquals("1 -> 0 -> 15", L.toString());

        L = IntList.of(55, 22, 15);
        IntListExercises.setToZeroIfMaxFEL(L);
        assertEquals("0 -> 0 -> 15", L.toString());

        L = IntList.of(44, 22, 11);
        IntListExercises.setToZeroIfMaxFEL(L);
        assertEquals("0 -> 0 -> 0", L.toString());

        L = IntList.of(1, 21, 11);
        IntListExercises.setToZeroIfMaxFEL(L);
        assertEquals("1 -> 21 -> 0", L.toString());

        L = IntList.of(1, 21, 12);
        IntListExercises.setToZeroIfMaxFEL(L);
        assertEquals("1 -> 21 -> 12", L.toString());
    }

    @Test
    public void testZeroOutFELMaxes2() {
        IntList L = IntList.of(55, 22, 45, 44, 5);
        IntListExercises.setToZeroIfMaxFEL(L);
        assertEquals("0 -> 22 -> 45 -> 0 -> 0", L.toString());
    }

    @Test
    public void testZeroOutFELMaxes3() {
        IntList L = IntList.of(5, 535, 35, 11, 10, 0);
        IntListExercises.setToZeroIfMaxFEL(L);
        assertEquals("5 -> 0 -> 35 -> 0 -> 10 -> 0", L.toString());

        L = IntList.of(5, 55, 33, 11, 9, 0);
        IntListExercises.setToZeroIfMaxFEL(L);
        assertEquals("5 -> 0 -> 0 -> 0 -> 0 -> 0", L.toString());
    }
}
