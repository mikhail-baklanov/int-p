package Oleg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static List dirFiles = new ArrayList<String>();
    static List delFiles = new ArrayList<String>();
    static List xmlNames = new ArrayList<String>();
    static Scanner in = new Scanner(System.in);
    static String filePath, xmlName;

    public static void main(String[] args) throws Exception {
        findDeletedFiles(parseSuppression(xmlName()),dir(fileDir()));
    }
    public static List<String> parseSuppression (String fullFileName) throws Exception{
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
        return xmlNames;
    }
    public static List<String> findDeletedFiles(List<String> suppressionsList, List<String> filesList) {
        for (int i=0; i<suppressionsList.size();i++) {
            String deletedFile=suppressionsList.get(i);
            Pattern pattern=Pattern.compile(deletedFile);
            for (int j=0;j<filesList.size();j++) {
                Matcher matcher = pattern.matcher(filesList.get(j));
                if (matcher.find()) {
                    deletedFile="";
                }
            }
            if (deletedFile!="") {
                delFiles.add(deletedFile);
            }
        }
        return delFiles;
    }
    public static List<String> dir (String puth) {
        showTree(puth, 0);
        return dirFiles;
    }
    public static void showTree(final String dirName, final int nest){
        File dir = new File(dirName);
        if (!dir.isDirectory()){
            return;
        }
        File files[] = dir.listFiles();
        for (File file : files){
            if (file.isDirectory()){
                showTree(file.getAbsolutePath(), nest + 1);
            }
            else {
                if (file.getName().endsWith(".java")) {
                    dirFiles.add(file.getAbsolutePath());
                }
            }
        }
    }

    public static String fileDir() {
        System.out.print("directory: ");
        filePath=in.nextLine();
        return filePath;
    }
    public static String xmlName() {
        System.out.println("xml file: ");
        xmlName=in.nextLine();
        return xmlName;
    }
}