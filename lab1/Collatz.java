/** Class that prints the Collatz sequence starting from a given number.
 *  @author YOUR NAME HERE
 */
public class Collatz {

    /** Find the next collatz number
     * @param n an integer
     * @return integer
     * */
    public static int nextNumber(int n) {
        if (n % 2 == 0){
            return (n / 2);
        } else {
            return (3 * n + 1);
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
        System.out.println();
    }
}

