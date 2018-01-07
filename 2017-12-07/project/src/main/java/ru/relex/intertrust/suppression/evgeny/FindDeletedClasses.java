package ru.relex.intertrust.suppression.evgeny;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс вызывает методы по парсингу xml файла, чтению путей из текстового файла или директории,
 * нахождению ненайденных выражений в путях проекта и вывода имени разработчика.
 * @author Евгений Воронин
 */
public class FindDeletedClasses implements SuppressionChecker {

    private static final String SEPARATOR = "[\\\\/]";
    private static final String JAVA_EXTENSION = ".java";

    /**
     * Метод получает спискок выражений из указанного xml файла.
     * @param fullFileName полный путь к файлу suppressions.xml
     * @return suppressionLists список выражений из файла
     */
    public List<String> parseSuppression(String fullFileName) {
        final List<String> suppressList = new ArrayList<>();

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
                    if (qName.equals("suppress")) {
                        for (int i = 0; i < attributes.getLength(); i++) {
                            if ("files".equals(attributes.getQName(i))) {
                                if (attributes.getValue(i).endsWith(JAVA_EXTENSION)) {
                                    suppressList.add(attributes.getValue(i).replace(SEPARATOR, File.separator));
                                }
                            }
                        }
                    }
                }
            };

            saxParser.parse(fullFileName, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return suppressList;
    }

    /**
     * Если передан путь к текстовому файлу, то метод считавыет его и записывает все строки в список. Если передан
     * путь к директории, то метод рекурсивно проходит по всем файлам указанной директории.
     * Если находит класс, то его путь добавляется в список.
     * @param path путь до файла или директории
     * @return classes список путей, найденных классов
     */
    public List<String> dir(String path) {
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
                            classes.addAll(dir(subdir.getAbsolutePath()));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return classes;
    }

    /**
     * Метод сравнивает пути проекта с выражениями из xml файла: если путь наден, то поднимается флаг,
     * если флаг не поднят, то соответствующее выражение из xml файла считается не найденным и добавляется в список.
     * @param suppressedFileNames список выражений из suppressions.xml
     * @param dir список путей проекта
     * @return deleted список ненайденных в проекте выражений из suppressions.xml
     */
    public List<String> findDeletedFiles(List<String> suppressedFileNames, List<String> dir) {
        List<String> deleted = new ArrayList<>();
        for (String suppress : suppressedFileNames) {
            boolean exist = false;
            for (String classPath : dir) {
                if (classPath.endsWith(suppress)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                deleted.add(suppress);
            }
        }
        return deleted;
    }

    /**
     * Метод возвращает имя и фамилию разработчика.
     * @return Евгений Воронин
     */
    public String getDeveloperName() {
        return "Евгений Воронин";
    }
}
