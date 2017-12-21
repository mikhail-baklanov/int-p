package com.company;

public class Main {

    final static int STEPEN = 3;
    final static int MAX_ITERATION_COUNT = 80;

    /**
     * Основной класс. Считает и выводит  в консоль числа от 0 до 80 в троичной системе счисления
     * @param args Аргументы коммандной строки, не используются в этой программе
     */
    public static void main(String[] args) {
        int tmp = 0;
        String str = "";
        for (int i = 0; i <= MAX_ITERATION_COUNT; i++){
            tmp = i;
            while (tmp >= STEPEN) {
                str += (tmp % STEPEN);
                tmp /= STEPEN;
            }
            str = str + (tmp);
            StringBuffer buffer = new StringBuffer(str);
            System.out.println(buffer.reverse());
            str = "";
        }
    }
}
