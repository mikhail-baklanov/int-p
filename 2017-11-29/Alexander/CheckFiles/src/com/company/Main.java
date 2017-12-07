package com.company;

import java.io.File;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) {
        File[] dir = new File("ru"+File.separator+"intrust"+File.separator+"client").listFiles(); //path указывает на директорию
        String[] fileNames = new String[dir.length];
        for(int i = 0; i < dir.length; i++) {
            fileNames[i] = dir[i].getPath();
            System.out.println(fileNames[i]);
        }

        Pattern pattern = Pattern.compile("ru[\\\\/]intertrust[\\\\/]cmj[\\\\/]client[\\\\/]");

        for(int i = 0; i < dir.length; i++) {
            Matcher m = pattern.matcher(fileNames[i]);
            System.out.println(m.find());
        }
    }
}
