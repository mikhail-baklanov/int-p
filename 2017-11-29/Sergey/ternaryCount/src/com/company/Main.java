package com.company;

public class Main {

    /**
     * Основной класс. Считает и выводит числа от 0 до 80 в троичной системе счисления
     * @param args Аргументы коммандной строки
     */
    public static void main(String[] args) {
        int tmp = 0;
        String str = "";
        for (int i = 0; i < 81; i++){
            tmp = i;
            while (tmp > 2) {
                str = str + (tmp%3);
                tmp = tmp/3;
            }
            str = str + (tmp);
            StringBuffer buffer = new StringBuffer(str);
            System.out.println(buffer.reverse());
            str = "";
        }
    }
}
