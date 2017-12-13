package ru.relex.suppression.intertrust.alexander;

import ru.relex.suppression.intertrust.interfaces.SuppressionChecker;
import ru.relex.suppression.intertrust.Registrator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Alexander implements SuppressionChecker {
    private final static String developerName = "Александр Ерофеев";

    static {
        Registrator.register(new Alexander());
    }

    public List<String> parseSuppression(String fullFileName){
        List<String> paths = new ArrayList<>();

        // Строим объектную модель исходного XML файла
        File xmlFile = new File(fullFileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);

        // Выполнять нормализацию не обязательно, но рекомендуется
        doc.getDocumentElement().normalize();

        // Получаем все узлы с именем "suppress"
        NodeList nodeList = doc.getElementsByTagName("suppress");

        //Перебор каждого узла и проверка его на соответствие регулярному выражению
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Pattern pattern = Pattern.compile("[a-zA-Z0-9_]*.java");
            Matcher m = pattern.matcher(node.getAttributes().getNamedItem("files").getTextContent());
            if(m.find())
                paths.add(node.getAttributes().getNamedItem("files").getTextContent());
        }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return paths;
    }

    public List<String> dir(String path){
        List<String> fileNames = null;
        try {
            fileNames = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        File dir = new File(path);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();

            for (File file : files)
                if (file.isDirectory())
                    fileNames.addAll(dir(file.getCanonicalPath()));

            for (File file : files)
                if (file.isFile())   //проверяем, файл ли это
                    fileNames.add(file.getAbsolutePath());
        }
        */
        return fileNames;
    }

    public String getDeveloperName(){
        return developerName;
    }

    public List<String> findDeletedFiles(List<String> suppressionsPaths, List<String> dirPaths){
        List<String> deletedFilePaths = new ArrayList<>();
        for(String item: suppressionsPaths) {
            Pattern pattern = Pattern.compile(item);

            boolean isExist = false;
            for (int i = 0; i < dirPaths.size(); i++) {
                Matcher m = pattern.matcher(dirPaths.get(i));
                if (m.find()) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist)
                deletedFilePaths.add(item);
        }
        return deletedFilePaths;
    }

}
