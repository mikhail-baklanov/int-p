package com.company;

public class Main {

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
            System.out.println(str);
            str = "";
        }
    }
}
