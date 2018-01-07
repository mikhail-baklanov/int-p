package ru.relex.practice.suppression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс, который получает список путей до классов проекта.
 * @author Евгений Воронин
 */
public class ClassFinder {
    private static final String JAVA_EXTENSION = ".java";

    /**
     * Если передан путь к текстовому файлу, то метод считавыет его и записывает все строки в список. Если передан
     * путь к директории, то метод рекурсивно проходит по всем файлам указанной директории.
     * Если находит класс, то его путь добавляется в список.
     * @param path путь до файла или директории
     * @return classes список путей, найденных классов
     */
    public static List<String> processDirectory(String path) {
        File directory = new File(path);
        List<String> classes = new ArrayList<>();

        if (directory.isFile()) {
            try {
                classes = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                File[] files = directory.listFiles();
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i].getName();
                    String classPath = null;

                    if (fileName.endsWith(JAVA_EXTENSION)) {
                        classPath = files[i].getAbsolutePath();
                        classes.add(classPath);
                    } else {
                        File subdir = new File(directory, fileName);
                        if (subdir.isDirectory()) {
                            classes.addAll(processDirectory(subdir.getAbsolutePath()));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return classes;
    }
}
