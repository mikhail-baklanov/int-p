package ru.relex.inttrust.suppresion;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Suppresion suppres = new Suppresion();
        Scanner input = new Scanner(System.in);

        List<String> suppression = null;
        List<String> dirs = null;
        List<String> res = null;
        String temp = "";

        System.out.println("Введите полное имя файла suppresion.xml");
        System.out.print("-> ");
        temp = input.nextLine();

        suppression = suppres.parseSuppression(temp);
        if (suppression == null){
            System.out.println("Возникла ошибка при открытии\\чтении файла");
            return;
        }

        System.out.println("Введите путь к директории или полное имя файла со списком классов");
        System.out.print("-> ");
        temp = input.nextLine();

        dirs = suppres.dir(temp);
        if (dirs == null){
            System.out.println("Возникла ошибка при открытии\\чтении файла\\ директории");
            return;
        }

        res = suppres.findDeletedFiles(suppression, dirs);
        for (String line: res){
            System.out.println(line);
        }
    }
}
