package ru.relex.practice.suppression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Основной класс, в котором определены пути к директории проекта и к файлу suppressions. Здесь же получаем списки
 * с путями до файлов .java и с регулярными выражениями из suppressions. Сравниваем пути с регулярными выражениями:
 * соответствующие удаляются, остальные считаются ненайденными.
 * @author Евгений Воронин
 */
public class Main {
    private static final String DIR_PATH = "F:\\Projects\\Relex\\SuppressionsChecker\\files.txt";
    private static final String SUPPRESSIONS_FILE = "F:\\Projects\\Relex\\SuppressionsChecker\\checkstyle-suppressions.xml";

    public static void main(String[] args) {
        List<String> classes = ClassFinder.processDirectory(DIR_PATH);
        List<String> suppressions = XmlParser.suppressionParser(SUPPRESSIONS_FILE);
        List<String> deleted = new ArrayList<>();
        for (String suppress : suppressions) {
            boolean exist = false;
            for (String classPath : classes) {
                if (classPath.endsWith(suppress)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                deleted.add(suppress);
            }
        }
        for (String suppress : deleted) {
            System.out.println(suppress);
        }
    }
}
