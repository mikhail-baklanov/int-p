
/**
 * @author Ерофеев Александр
 */
public class Main {

    public static void main(String[] args) {
        for(int i = 0; i <= 80; i++)
            System.out.println(getTernary(i));
    }
    /**
     * Метод, который переводит число в троичную систему счисления.
     * @param digit число, необходимое перевести в троичную систему
     * @return число в троичной системе счисления
     */
    public static String getTernary(int digit){
        String ternary = "";
        while(digit > 3){
            ternary += digit % 3;
            digit /= 3;
        }
        ternary += digit;
        return ternary;
    }
}
