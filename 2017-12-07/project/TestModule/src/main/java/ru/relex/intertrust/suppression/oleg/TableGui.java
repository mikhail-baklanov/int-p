package ru.relex.intertrust.suppression.oleg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TableGui extends JFrame{
    private JButton check =new JButton("Deleted files");
    private String [][] date;
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
        container.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady=100;
        c.ipadx=430;
        c.weightx = 0;
        c.weighty=0;
        c.gridx = 0;
        c.gridy = 0;
        container.add(scrollPane,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_END;
        c.weighty=0.1;
        c.ipady = 0;
        c.ipadx=0;
        c.gridx = 0;
        c.gridy = 1;
        container.add(check,c);
        check.addActionListener(new ButtonEventListener());
        frame.setPreferredSize(new Dimension(450, 200));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String[] message = new String[date.length];
            for (int i = 0; i < date.length; i++) {
                message[i] = date[i][2];
                try {
                    JOptionPane.showMessageDialog(null,
                            message[i],
                            date[i][0] + " deleted files",
                            JOptionPane.PLAIN_MESSAGE);
                }
                catch (StackOverflowError ex) {
                    JOptionPane.showMessageDialog(null,"StackOverflow",date[i][0]+" error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
