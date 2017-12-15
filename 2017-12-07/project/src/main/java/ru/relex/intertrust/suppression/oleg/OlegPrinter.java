package ru.relex.intertrust.suppression.oleg;


import ru.relex.intertrust.suppression.Result;
import ru.relex.intertrust.suppression.interfaces.ListPrinter;

import javax.swing.*;
import java.util.List;

public class OlegPrinter implements ListPrinter {

    public void visualize(List<Result> list) {
        String[][] data= new String[list.size()][5];
         Test(data,list);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                TableGui tableGui= new TableGui();
                tableGui.createGUI(data);
            }
        });
    }

    private void Test(String[][] data, List<Result> list) {
        for (int i=0;i<list.size();i++) {
                data[i][0] = list.get(i).getDeveloperName();
                data[i][1] = list.get(i).getParseTime() +"ms";
                data[i][2] = list.get(i).getDirTime()+"ms";
                data[i][3] = list.get(i).getFindTime()+"ms";
                if (list.get(i).getFileList().size()!=0)
                    data[i][4]=list.get(i).getFileList().get(0)+ " ";
                for (int j = 1; j < list.get(i).getFileList().size(); j++) {
                    data[i][4] +="\n" +list.get(i).getFileList().get(j) + " ";
              }
        }
    }



}
