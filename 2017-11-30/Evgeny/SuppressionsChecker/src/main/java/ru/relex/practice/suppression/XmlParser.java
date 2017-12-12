package ru.relex.practice.suppression;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, в котором получаем список регулярных выражений из xml файла.
 * @author Евгений Воронин
 */
public class XmlParser {
    /**
     * Получаем спискок регулярных выражений из указанного файла.
     * @param suppressionFile
     * @return suppresList
     */
    public static List<String> suppressionParser(String suppressionFile) {

        List<String> suppresList = new ArrayList<String>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(suppressionFile);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("suppress");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    suppresList.add(eElement.getAttribute("files"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return suppresList;
    }
}
