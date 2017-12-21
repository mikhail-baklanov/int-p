package ru.relex.intertrust.suppression.vitaliy;

import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Vitaliy implements SuppressionChecker
{
    private final static String NAME="Vitaliy Safonov";

    /**
     * Проходим по каждой строке suppression.xml и проверяем регулярным
     * выражением.Если строка подходит под regExp,то забираем путь и
     * преобразуем его к рабочему виду
     * @param fullFileName Полное имя к файлу suppressions.xml
     * @return
     */

    @Override
    public List<String> parseSuppression(String fullFileName)
    {
        Pattern p = Pattern.compile("<suppress\\s");
        List<String> suppList = new ArrayList<>();
        try
        {
            for(int i=0;i<Files.readAllLines(Paths.get(fullFileName), StandardCharsets.UTF_8).size();i++)
            {
                String str=Files.readAllLines(Paths.get(fullFileName), StandardCharsets.UTF_8).get(i);
                if ( (str.indexOf("<!--") == -1) && (p.matcher(str).find()) && (str.indexOf("files=") != -1) )
                {
                    String begining = "files=\"";
                    int start = str.indexOf(begining) + begining.length();
                    int finish = str.indexOf("\"", start + 1);
                    String path = str.substring(start, finish);
                    if ( (!suppList.contains(path)) && (path.endsWith(".java")))
                        suppList.add(str.substring(start, finish).replace("[\\\\/]", "\\"));
                }
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        return suppList;
    }

    /**
     * Во вложенном цикле сверяем каждый путь из dir с каждым из suppression.xml и при
     * нахождении одинаковых выходим из цикла, если этого не произощло, то путь из suppressions.xml добавляется в
     * лист deletedFiles
     * @param suppressionsPaths Список имен java-классов из файла suppressions.xml
     * @param dirPaths Список полных имен java-классов проекта, для которого выполняется проверка
     * @return
     */
    @Override
    public List<String> findDeletedFiles(List<String> suppressionsPaths, List<String> dirPaths)
    {
        List<String> deletedFiles =new ArrayList<>();

        for(int i=0;i<suppressionsPaths.size();i++)
        {
            boolean exist=false;
            for(int j=0;j<dirPaths.size();j++)
                if(dirPaths.get(j).endsWith(suppressionsPaths.get(i)))
                {
                    exist=true;
                    break;
                }
                if(!exist) deletedFiles.add(suppressionsPaths.get(i));
        }

        return deletedFiles;
    }

    /**
     * Читаем пути из файла и скидываем их в лист
     * @param path
     * @return
     */

    @Override
    public List<String> dir(String path)
    {
        List<String> dirPaths=new ArrayList<>();
        try
        {
            dirPaths=Files.readAllLines(Paths.get(path),StandardCharsets.UTF_8);
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        return dirPaths;
    }

    @Override
    public String getDeveloperName() { return NAME; }

}
