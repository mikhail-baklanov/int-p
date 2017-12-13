package ru.relex.intertrust.suppression.oleg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TableGui extends JFrame{
    private ArrayList<JButton> check =new ArrayList<JButton>();
    private String [][] date;
    private int i;
    public void createGUI(String [][] data) {
        date= data;
        JFrame frame = new JFrame("SpeedTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columnNames = {
                "Name",
                "Time"
               // "Deleted files"
        };


        JTable table = new JTable(date, columnNames);

        JScrollPane scrollPane = new JScrollPane(table);

        Container container=frame.getContentPane();
        container.add(scrollPane);
        for (i=0;i<date.length;i++) {
            check.add(new JButton("Deleted files for " + date[i][0]));
            container.add(check.get(i));
          //  check.get(i).addActionListener(new ButtonEventListener());
        }
        container.setLayout(new GridLayout(2,1,2,2));
        frame.setPreferredSize(new Dimension(450, 200));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
//    class ButtonEventListener implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//            String message = "";
//            message += date[i][2];
//            JOptionPane.showMessageDialog(null,
//                    message,
//                    "Deleted files",
//                    JOptionPane.PLAIN_MESSAGE);
//        }
//    }
}
