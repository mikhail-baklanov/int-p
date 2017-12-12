package ru.relex.intertrust.suppression.Users.Vladilen;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DenisovSC {
    public static void main(String[] args){
        String filePath = "Suppressions.xml";
        String projectFolderPath = "C:\\Users\\User\\Downloads";
        Document doc = getDocument(filePath);
        List<String> supList = getSuppressionPathesList(doc);
        List<String> fileList = getAllPathes(new File(projectFolderPath));
        List<String> removedFileList = getRemovedFilesList(supList, fileList);
        for (String elem : removedFileList)
            System.out.println(elem + " doesn't exist anymore");
    }

    static List<String> getSuppressionPathesList(Document doc) {
        //todo сделать парс файлов прямо здесь или запилить хорошие регулярки в getRemovedFilesList
        List<String> list = new ArrayList<>();
        NodeList l = doc.getChildNodes();
        for (int i = 0; i < l.getLength(); i++)
            searchSuppressionPathes(list, l.item(i));
        return list;
    }

    private static void searchSuppressionPathes(List<String> list, Node node) {
        if (node.getNodeName().equals("suppress")) {
            NamedNodeMap attributes = node.getAttributes();
            Node files = attributes.getNamedItem("files");
            if (files != null) {
                String valueToInsert = files.getNodeValue();
                StringBuilder sb = new StringBuilder(valueToInsert);
                list.add(sb.toString().trim());
            }
        }
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
            searchSuppressionPathes(list, nodeList.item(i));
    }

    static Document getDocument(String filePath){
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder = f.newDocumentBuilder();
            return builder.parse(new File(filePath));
        } catch (Exception exception) {
            String message = "XML parsing error!";
            //throw new Exception(message); //это было бы в тему :(
        }
        return null;//Кто вообще придумал отказаться от выбрасывания Exception? Как же мне этого будет нехватать :(
    }

    static List<String> getAllPathes(File directory) {
        List<String> list = new ArrayList<>();
        if (directory.isFile())
            list.add(directory.getAbsolutePath());
        else {
            File[] files = directory.listFiles();
            if (files != null)
                for (int i = 0; i < files.length; i++)
                    list.addAll(getAllPathes(files[i]));
        }
        return list;
    }

    static List<String> getRemovedFilesList(List<String> supList, List<String> pathList) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < supList.size(); i++) {
            boolean existingFlag = false;
            String suppression = supList.get(i).replaceAll("\\[\\\\\\/\\]", "\\");
            for (int j = 0; j < pathList.size(); j++) {
                if (pathList.get(j).endsWith(suppression))
                {
                    existingFlag = true;
                    break;
                }
            }
            if (!existingFlag)
                list.add(supList.get(i));
        }
        return list;
    }
}
