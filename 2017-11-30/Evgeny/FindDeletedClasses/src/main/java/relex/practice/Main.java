package relex.practice;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 1 on 08.12.2017.
 */
public class Main {
    public static void main(String[] args) {
            String path = "F:\\Projects\\Relex\\SimpleTest\\IntrTrust";
            String suppressionFile = "F:\\Projects\\Relex\\SimpleTest\\test.xml";
            List<String> classes = ClassFinder.processDirectory(path);
            List<String> suppressions = XmlParser.suppressionParser(suppressionFile);
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
