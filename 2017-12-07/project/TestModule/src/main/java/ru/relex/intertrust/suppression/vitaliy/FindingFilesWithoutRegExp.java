package ru.relex.intertrust.suppression.vitaliy;

import ru.relex.intertrust.suppression.Registrator;
import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindingFilesWithoutRegExp implements SuppressionChecker
{

    //private List<String> paths = new ArrayList<>();
    //private List<String> FileSystem= new ArrayList<>();
    private final String developerName="Vitaliy Safonov";


    public String getDeveloperName()
    {
        return developerName;
    }

    public List<String> dir(String filename)
    {
        List<String> FileSystem= new ArrayList<>();
        int condition = ru.relex.intertrust.suppression.vitaliy.ArgsTest.linesCounter(filename);
        String str;
        int q = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            for(int i=0;i<condition;i++)
            {
                str = reader.readLine().replaceAll("\\\\","/");//приводим путь у кнужному для поиска виду
                                                                                //сразу при считывании
                int k = 0;
                int startingPoint = 0;
                int finishingPoint = 0;

                String str1;
                for (int j = 0; j < str.length(); j++)//доходим до . и дем назад 2 \
                {
                    if (str.charAt(j) == (char) 46)
                    {
                        finishingPoint = j;
                        k = j;
                        int counter=0;
                        while(counter<2)
                        {
                            if(str.charAt(k) == (char) 47) counter++;
                            k--;
                            startingPoint = k + 1;
                        }
                    }
                }
                FileSystem.add(str.substring(startingPoint, finishingPoint) + ".java");

            }

            reader.close();
        }
        catch (IOException ex)
        {System.out.println(ex.getMessage());}
        return FileSystem;

    }

    public List<String> parseSuppression(String suppressionsFilename)//получение информации о файлах
    {
        //открытие файла и считывание его строк в paths
        List<String> paths = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(suppressionsFilename)))
        {
            String str;
            int condition = ru.relex.intertrust.suppression.vitaliy.ArgsTest.linesCounter(suppressionsFilename);//считаем количество строк в файле suppressions.xml
                                                                        //позволяет повысить скорость
            int i=0;
            while (i<condition)
            {
                str=reader.readLine();
                if(CheckingStrings(str)) paths.add(str);
                i++;
            }
            reader.close();




        }
        catch (IOException ex)
        {System.out.println(ex.getMessage());}
        //return findDeletedFiles(paths,FileSystem);
        //fixPath();//исправление [/\\] на /
        //getPath();// извлечение пути к файлу
        getClassNameFromXML(getPath(fixPath(paths)));//приводим строки из suppressions.xml к приемлимому для поиска виду
        return paths;
    }

    public List<String> findDeletedFiles(List<String> sup,List<String> dir)
    {

        List<String> list = new ArrayList<>();
        int x=0;
        int j;
        int counter;
        //сравнивание двух строк из suppressions и files.txt
        //в привиденном виде
        for(int i=0;i<sup.size();i++)
        {
            counter=0;
            for (j = 0; j < dir.size(); j++)
            {
                if (sup.get(i).equals(dir.get(j))) {
                    counter++;
                    break;
                }
                if((j==dir.size()-1)&&(counter==0))//проверка отсутствия файла
                {

                    list.add(sup.get(i));
                }
            }

        }

        return list;
    }

public List<String> getClassNameFromXML(List<String> paths)
{

    List<String> paths2 = new ArrayList<>();

	for(int i=0;i<paths.size();i++)
	{
		StringBuilder str=new StringBuilder(paths.get(i));
		String str1=paths.get(i);
        int k = 0;
        int startingPoint=0;
        int finishingPoint=0;
		for(int j=0;j<str1.length();j++)
		{//Алгоритм как и в dir
        	if(str1.charAt(j)==(char)46)
        	{
        		finishingPoint=j;
        		k=j;
        		int counter=0;
                while(counter<2)
        		{
        		    if(str1.charAt(k)==(char)47)counter++;
        			k--;
                    startingPoint=k+1;
                    if(k==0){startingPoint=0;break;}
        		}
        	}
		}
		paths2.add(str1.substring(startingPoint,finishingPoint)+".java");
    }
    paths.clear();
    for(String e:paths2)
        paths.add(e);
    return paths;

}

    public static boolean CheckingStrings(String str)//проверяем строки в suppressions.xml и смотрим есть ли в них suppress files=
    {
        Pattern p = Pattern.compile(" *<suppress files=.+");
        Matcher m = p.matcher(str);
        return m.matches();
    }


    public List<String> fixPath(List<String> paths)
    {
        StringBuilder str;
        int startingSize = paths.size();
        String s = "\\";
        for (int i=0;i<startingSize;i++)//доходим до [ и заменя её и все внутреннее до ] на /
        {
            str = new StringBuilder(paths.get(i));
            for(int j=0;j<str.length();j++)
            {
                if(str.charAt(j)==(char)91) str.replace(j,j+5,"/");// [ = 91
            }
            paths.add(str.toString());
        }

        for (int j=0;j<startingSize;j++)
            paths.remove(0);
        return paths;
    }

    public List<String> getPath(List<String> paths)//забираем готовый путь в arrayList
    {
        String str="<suppress files=";
        StringBuilder path;
        int counter;
        int startingSize = paths.size();
        int k =0;

        for(int i=0;i<startingSize;i++)
        {

            path=new StringBuilder(paths.get(i).substring(str.length()+3,paths.get(i).length()));
            counter=0;
            for(int j=0;j<path.length();j++)
                if (path.charAt(j) == (char)34) { counter = j;break; }//34="

            paths.add(path.toString().substring(0,counter));
        }

        for (int j=0;j<startingSize;j++)
            paths.remove(0);


        return paths;
    }


}
