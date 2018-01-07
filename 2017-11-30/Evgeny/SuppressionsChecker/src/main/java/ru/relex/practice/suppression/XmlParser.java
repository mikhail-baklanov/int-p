package ru.relex.practice.suppression;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс, который получает список выражений из xml файла.
 * @author Евгений Воронин
 */
public class XmlParser {

    private static final String SEPARATOR = "[\\\\/]";
    private static final String JAVA_EXTENSION = ".java";

    /**
     * Метод получает спискок выражений из указанного xml файла.
     * @param suppressionFile путь к xml файлу
     * @return suppressList список выражений из файла
     */
    public static List<String> suppressionParser(String suppressionFile) {

        final List<String> suppressList = new LinkedList<String>();

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

            saxParser.parse(suppressionFile, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return suppressList;
    }
}
