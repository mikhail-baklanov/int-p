package ru.relex.practice.ternarynumbers;

import java.util.ArrayList;

/**
 * Основной класс, в котором десятичные числа переводятся в троичную систему и выводятся справа налево.
 * @author Евгений Воронин
 */
public class Main {
    public static void main(String[] args) {
		int n = 80;
        int dim = 3;
        for (int i = 0; i <= n; i++) {
            int value = i;
            System.out.print(i + " = ");
            while ( value >= dim ) {
                System.out.print(value % dim);
                value /= dim;
            }
            System.out.println(value);
        }
    }
}
