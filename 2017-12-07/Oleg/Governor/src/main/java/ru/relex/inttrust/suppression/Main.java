package ru.relex.inttrust.suppression;
import ru.relex.inttrust.suppression.Interfaces.SuppressionChecker;
import ru.relex.inttrust.suppression.Oleg.SuppressionCheckerAdapter;


import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner in = new Scanner(System.in);
    static String filePath, xmlName;
    static {
        Registration();
    }
    static String[][] data= new String[Registrator.getList().size()][3];

    public static void main(String[] args) throws Exception{
        fileDir();
        xmlName();
        Test();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                createGUI();
            }
        });
    }
    private static void Registration() {
        Registrator.register(new SuppressionCheckerAdapter());
    }
    private static void Test() throws Exception{
        for (int i=0;i<Registrator.getList().size();i++) {
            SuppressionChecker SC=Registrator.getList().get(i);
            List dir,parseSuppression,findDeletedFiles;
            Date start = new Date();
            dir=SC.dir(filePath);
            parseSuppression=SC.parseSuppression(xmlName);
            findDeletedFiles=SC.findDeletedFiles(parseSuppression,dir);
            Date end= new Date();
                data[i][0] = Integer.toString(i+1);
                data[i][1] = end.getTime() - start.getTime() + " ms";
                for (int j = 0; j < findDeletedFiles.size(); j++) {
                    data[i][2] += " " + findDeletedFiles.get(j);
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
    public static void createGUI() {
        JFrame frame = new JFrame("SpeedTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columnNames = {
                "Name",
                "Time",
                "Deleted files"
        };


        JTable table = new JTable(data, columnNames);

        JScrollPane scrollPane = new JScrollPane(table);

        frame.getContentPane().add(scrollPane);
        frame.setPreferredSize(new Dimension(450, 200));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
