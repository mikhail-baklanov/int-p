/**
 * Класс для задачи о выводе чисел в троичной системе счисления в обратном порядке
 */
public class Application {

    /**
     * Диапазон выводимых чисел
     */
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 80;

    /**
     * Основание системы счисления
     */
    private static final int BASE = 3;

    /**
     * Точка входа приложения
     * @param args Аргументы командной строки
     */
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
