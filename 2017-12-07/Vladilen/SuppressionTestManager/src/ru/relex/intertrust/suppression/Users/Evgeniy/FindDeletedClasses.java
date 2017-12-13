package ru.relex.intertrust.suppression.Users.Evgeniy;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.relex.intertrust.suppression.CommonElements.Registrator;
import ru.relex.intertrust.suppression.CommonElements.SuppressionChecker;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 1 on 10.12.2017.
 */
public class FindDeletedClasses implements SuppressionChecker{
    static {
        Registrator.register(new FindDeletedClasses());
    }
    public List<String> parseSuppression(String fullFileName) {
        List<String> suppressionList = new ArrayList<String>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fullFileName);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("suppress");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getAttribute("files").matches("(^ru).*?(java)$")) {
                        suppressionList.add(eElement.getAttribute("files"));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return suppressionList;
    }

    public List<String> dir(String path) {
        File directory = new File(path);
        ArrayList<String> classes = new ArrayList<String>();

        try {
            // Get the list of the files contained in the package
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                String className = null;

                // we are only interested in .java files
                if (fileName.endsWith(".java")) {
                    className = files[i].getAbsolutePath();
                }

                if (className != null) {
                    classes.add(className);
                }

                // If the file is a directory recursively find class.
                File subdir = new File(directory, fileName);
                if (subdir.isDirectory()) {
                    classes.addAll(dir(subdir.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    public List<String> findDeletedFiles(List<String> dir, List<String> suppressedFileNames) {
        for (Iterator<String> classPathIt = dir.iterator(); classPathIt.hasNext();) {
            String classPath = classPathIt.next();
            for (Iterator<String> regExpIt = suppressedFileNames.iterator(); regExpIt.hasNext();) {
                String regExp = regExpIt.next();
                if (classPath.matches(".*?"+regExp)) {
                    classPathIt.remove();
                    regExpIt.remove();
                    break;
                }
            }
        }
        return suppressedFileNames;
    }

    public String getDeveloperName() {
        return "Evgeny";
    }
}
