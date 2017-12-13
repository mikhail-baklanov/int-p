package ru.relex.intertrust.suppression.oleg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SupOleg implements SuppressionChecker {
    private final static String developerName = "Олег Слепичев";
    public List<String> parseSuppression(String fullFileName) {
        List xmlNames = new ArrayList<String>();
        try {
            final File xmlFile = new File(fullFileName);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("suppress");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element element = (Element) node;
                xmlNames.add(element.getAttribute("files"));
            }
        } catch (IOException ex) {
            System.out.println("Ill-fated path.");
        }
        catch (SAXException ex) {
            ex.printStackTrace();
        }
        catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
        return xmlNames;
    }

    public List<String> dir(String path) {
        List dirFiles = new ArrayList<String>();
        try {
            dirFiles = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
//        showTree(path, 0, dirFiles);
        return dirFiles;
    }
//    public void showTree(final String dirName, final int nest, List dirFiles){
//        File dir = new File(dirName);
//        if (!dir.isDirectory()){
//            return;
//        }
//        File files[] = dir.listFiles();
//        for (File file : files){
//            if (file.isDirectory()){
//                showTree(file.getAbsolutePath(), nest + 1, dirFiles);
//            }
//            else {
//                if (file.getName().endsWith(".java")) {
//                    dirFiles.add(file.getAbsolutePath());
//                }
//            }
//        }
//    }

    public List<String> findDeletedFiles(List<String> suppressionsList, List<String> filesList) {
        List delFiles = new ArrayList<String>();
        for (int i=0; i<suppressionsList.size();i++) {
            String deletedFile=suppressionsList.get(i);
            Pattern pattern=Pattern.compile(deletedFile);
            for (int j=0;j<filesList.size();j++) {
                Matcher matcher = pattern.matcher(filesList.get(j));
                if (matcher.find()) {
                    deletedFile="";
                    break;
                }
            }
            if (deletedFile!="") {
                delFiles.add(deletedFile);
            }
        }
        return delFiles;
    }

    public String getDeveloperName(){
        return developerName;
    }
}
