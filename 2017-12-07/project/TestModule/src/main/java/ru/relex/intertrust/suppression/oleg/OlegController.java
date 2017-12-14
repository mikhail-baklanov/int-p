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
        long [] time=new long[listOfChekers.size()];
        for (int i=0;i<listOfChekers.size();i++) {
            SuppressionChecker SC=listOfChekers.get(i);
            List<String> dir,parseSuppression,findDeletedFiles;
            Date start = new Date();
            dir=SC.dir(filePath);
            parseSuppression=SC.parseSuppression(xmlName);
            findDeletedFiles=SC.findDeletedFiles(parseSuppression,dir);
            Date end= new Date();
                data[i][0] = SC.getDeveloperName();
                time[i]=end.getTime() - start.getTime();
                data[i][1] = time[i] + " ms";
                if (findDeletedFiles.size()!=0)
                    data[i][2]=findDeletedFiles.get(0)+ " ";
                for (int j = 1; j < findDeletedFiles.size(); j++) {
                    data[i][2] +="\n" +findDeletedFiles.get(j) + " ";
              }
        }
        for (int b = data.length-1; b > 0; b--) {
            for (int j = 0; j < b; j++) {
                if (time[j] > time[j + 1]) {
                    long times=time[j];
                    time[j]=time[j+1];
                    time[j+1]=times;
                    System.out.println("t1="+time[j]+" , t2="+time[j+1]+" , n1="+data[j][0]+" , n2="+data[j+1][0]);
                    for (int i=0;i<3;i++) {
                        String t = data[j][i];
                        data[j][i] = data[j + 1][i];
                        data[j + 1][i] = t;
                    }
                }
            }
        }
    }



}
