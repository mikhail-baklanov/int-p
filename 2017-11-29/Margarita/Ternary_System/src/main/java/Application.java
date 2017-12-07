public class Application {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 80;
    private static final int BASE = 3;

    public static void main(String[] args) {
        for (int i = MIN_VALUE; i <= MAX_VALUE; i++) {
            int decimal = i;
            System.out.print(decimal + " = ");
            while (decimal > BASE) {
                System.out.print(decimal % BASE);
                decimal /= BASE;
            }
            System.out.println(decimal);
        }
    }
}
