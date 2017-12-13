package ru.relex.suppression;

import ru.relex.suppression.apps.*;
import ru.relex.suppression.controllers.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new Alexander();
        Scanner input = new Scanner(System.in);
        System.out.print("Введите кол-во итераций: ");
        Controller.start(input.nextInt(), Registrator.getList());
    }
}
