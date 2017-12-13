package ru.relex.practice.suppression;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, в котором получаем список путей до файлов .java.
 * @author Евгений Воронин
 */
public class ClassFinder {
    /**
     * Рекурсивно проходим по всем файлам указанной директории. Если находим файл .java, то его путь добавляем в список.
     * @param path
     * @return classes
     */
    public static List<String> processDirectory(String path) {
        File directory = new File(path);
        ArrayList<String> classes = new ArrayList<String>();

        try {
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                String className = null;

                if (fileName.endsWith(".java")) {
                    className = files[i].getAbsolutePath();
                }

                if (className != null) {
                    classes.add(className);
                }

                File subdir = new File(directory, fileName);
                if (subdir.isDirectory()) {
                    classes.addAll(processDirectory(subdir.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
}
