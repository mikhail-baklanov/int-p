package ru.relex.intertrust.suppression.oleg;




import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class OlegController implements Controller {

    public void start(String xmlName, String filePath, List<SuppressionChecker> listOfChekers)  {
        String[][] data= new String[listOfChekers.size()][3];
        Test(xmlName, filePath, data, listOfChekers);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                TableGui tableGui= new TableGui();
                tableGui.createGUI(data);
            }
        });
    }

    private void Test(String xmlName, String filePath,String[][] data, List<SuppressionChecker> listOfChekers) {
        for (int i=0;i<listOfChekers.size();i++) {
            SuppressionChecker SC=listOfChekers.get(i);
            List<String> dir,parseSuppression,findDeletedFiles;
            Date start = new Date();
            dir=SC.dir(filePath);
            parseSuppression=SC.parseSuppression(xmlName);
            findDeletedFiles=SC.findDeletedFiles(parseSuppression,dir);
            Date end= new Date();
                data[i][0] = SC.getDeveloperName();
                data[i][1] = end.getTime() - start.getTime() + " ms";
                if (findDeletedFiles.size()!=0)
                    data[i][2]=findDeletedFiles.get(0)+ " ";
                for (int j = 1; j < findDeletedFiles.size(); j++) {
                    data[i][2] +="\n" +findDeletedFiles.get(j) + " ";


              }
        }
    }



}
