package ru.relex.intertrust.suppression.vitaliy;

import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class RegStart implements Controller
{


    @Override
    public void start(String suppressionFilename, String dir, List<SuppressionChecker> listOfChekers)
    {
        for(int i=0;i<listOfChekers.size();i++)
        {
            long start=System.currentTimeMillis();
            List<String> list =listOfChekers.get(i).findDeletedFiles(listOfChekers.get(i).parseSuppression(suppressionFilename),listOfChekers.get(i).dir(dir));
            //List<String> list =  listOfChekers.get(i).parseSuppression(suppressionFilename);
            long finish=System.currentTimeMillis();

            for(String e:list)
                try(FileWriter writer = new FileWriter("report.txt", true))
                {
                    // запись всей строки
                    String text = "File "+e+" doesn't exist ";
                    writer.write(text);
                    // запись по символам

                    writer.append('\r');
                    writer.append('\n');


                    writer.flush();
                }
                catch(IOException ex){

                    System.out.println(ex.getMessage());
                }
            //System.out.println("File "+e+" doesn't exist");
            System.out.println("Developer name: "+listOfChekers.get(i).getDeveloperName()+" Time:"+(finish-start));

        }

    }
}
