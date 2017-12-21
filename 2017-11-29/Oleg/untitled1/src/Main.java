//public class Main {
/**
 * Метод, который переводит числа в троичную систему счисления.
 */
//    public static void main(String[] args) {
//        for (int i = 0; i < 81; i++)
//            System.out.print(Integer.toString(i, Integer.parseInt("3")) + " ");
//    }
//}
public class Main {
    /**
     * Метод, который переводит числа в троичную систему счисления и выводит полученные в троичной системе цифры числа справа налево.
     * перебирает числа от нуля до восьмидесяти одного.
     */
    public static void main(String[] args) {
        for (int i = 0; i < 81; i++) {
            int j=i;
            while (j>0) {
                System.out.print(j%3);
                j=j/3;
            }
            System.out.println("");
        }
    }
}