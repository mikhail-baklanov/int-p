package ru.relex.practice.suppression;

import java.util.Iterator;
import java.util.List;

/**
 * Основной класс, в котором определены пути к директории проекта и к файлу suppressions. Здесь же получаем списки
 * с путями до файлов .java и с регулярными выражениями из suppressions. Сравниваем пути с регулярными выражениями:
 * соответствующие удаляются, остальные считаются ненайденными.
 * @author Евгений Воронин
 */
public class Main {
    private static final String DIR_PATH = "F:\\Projects\\Relex\\SimpleTest\\IntrTrust";
    private static final String SUPPRESSIONS_FILE = "F:\\Projects\\Relex\\SimpleTest\\suppressions.xml";

    public static void main(String[] args) {
            List<String> classes = ClassFinder.processDirectory(DIR_PATH);
            List<String> suppressions = XmlParser.suppressionParser(SUPPRESSIONS_FILE);
            for (Iterator<String> classPathIt = classes.iterator(); classPathIt.hasNext();) {
                String classPath = classPathIt.next();
                for (Iterator<String> regExpIt = suppressions.iterator(); regExpIt.hasNext();) {
                    String regExp = regExpIt.next();
                    if (classPath.matches(".*?"+regExp)) {
                        classPathIt.remove();
                        regExpIt.remove();
                        break;
                    }
                }
            }
            for (String regExp : suppressions) {
                System.out.println(regExp);
            }
    }
}
