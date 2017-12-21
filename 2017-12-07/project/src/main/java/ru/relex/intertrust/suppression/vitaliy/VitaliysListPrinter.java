package ru.relex.intertrust.suppression.vitaliy;

import ru.relex.intertrust.suppression.Result;
import ru.relex.intertrust.suppression.interfaces.ListPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class VitaliysListPrinter implements ListPrinter 
{

    @Override
    /**
    *Вывод в тхт файл в \output\report.txt"
    **/
    public void visualize(List<Result> list) 
    {
        File folder = new File( "output");
        if (!folder.exists()) folder.mkdirs();

        try (FileWriter writer = new FileWriter("output\\report.txt", false))
        {
            writer.append("");
            writer.flush();
            writer.close();

        } catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }


        for (Result r : list)
        {

            try (FileWriter writer = new FileWriter("output\\report.txt", true))
            {
                writer.append("---------------------------------------------------------------------------------------------");
                writer.append('\r');
                writer.append('\n');
                // запись всей строки
                String text = "Developer name: " + r.getDeveloperName() + " Time:" + (r.getParseTime() + r.getDirTime() + r.getFindTime());
                writer.write(text);
                writer.append('\r');
                writer.append('\n');

                for (String DF : r.getFileList())
                {
                    writer.write("File " + DF + " doesn't exist ");
                    writer.append('\r');
                    writer.append('\n');
                }

                writer.flush();

            } catch (IOException ex)
            {

                System.out.println(ex.getMessage());
            }

            /*try (FileWriter writer = new FileWriter("report.txt", true))
            {
                for (String DF : r.getFileList())
                {
                    writer.write("File " + DF + " doesn't exist ");
                    writer.append('\r');
                    writer.append('\n');
                    writer.flush();
                }
                // запись по символам

                writer.append('\r');
                writer.append('\n');

                writer.flush();
            } catch (IOException ex)
            {
                System.out.println(ex.getMessage());
            }*/
        }
    }
}
