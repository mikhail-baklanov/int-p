package com.company;

public class Main {

    public static void main(String[] args) {
        for(int i = 0; i < 81; i++)
            System.out.println(getTernary(i));
    }

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
