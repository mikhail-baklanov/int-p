package ru.relex.inttrust.suppresion;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //C:\Users\mrlun\Documents\suppressionChecker\checkstyle-suppressions.xml
        //C:\Users\mrlun\Documents\suppressionChecker\a.txt

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

        System.out.println("Введите путь к директории или полное имя файла со списком классов");
        System.out.print("-> ");
        temp = input.nextLine();

        dirs = suppres.dir(temp);

        res = suppres.findDeletedFiles(suppression, dirs);
        for (String line: res){
            System.out.println(line);
        }
    }
}
