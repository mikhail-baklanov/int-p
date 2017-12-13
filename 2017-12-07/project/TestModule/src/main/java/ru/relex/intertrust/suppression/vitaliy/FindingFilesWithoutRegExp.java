package ru.relex.intertrust.suppressions;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindingFilesWithoutRegExp implements SuppressionChecker, Controller
{

    private List<String> paths = new ArrayList<>();
    private List<String> FileSystem= new ArrayList<>();
    private final String developerName="VitSaf";
    static
    {
        Registrator.register((SuppressionChecker) new FindingFilesWithoutRegExp());
        Registrator.register((Controller) new FindingFilesWithoutRegExp());
    }

    public String getDeveloperName()
    {
        return developerName;
    }

    public List<String> dir(String filename)
    {
        int condition = ArgsTest.linesCounter(filename);
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
        try(BufferedReader reader = new BufferedReader(new FileReader(suppressionsFilename)))
        {
            String str;
            int condition = ArgsTest.linesCounter(suppressionsFilename);//считаем количество строк в файле suppressions.xml
                                                                        //позволяет повысить скорость
            int i=0;
            while (i<condition)
            {
                str=reader.readLine();
                if(CheckingStrings(str)) paths.add(str);
                i++;
            }
            reader.close();

            //dir("files.txt");
            fixPath();//исправление [/\\] на /
            getPath();// извлечение пути к файлу
            getClassNameFromXML();//приводим строки из suppressions.xml к приемлимому для поиска виду

        }
        catch (IOException ex)
        {System.out.println(ex.getMessage());}
        //return findDeletedFiles(paths,FileSystem);
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
                    //System.out.println((x++)+"/1928 "+ dir.get(j) + " exist");
                    counter++;
                    break;
                }
                if((j==dir.size()-1)&&(counter==0))//проверка отсутствия файла
                {
                    //System.out.println("File "+sup.get(i)+" doesn't exist");
                    list.add(sup.get(i));
                }
            }

        }

        return list;
    }

public void getClassNameFromXML()
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

}

    public static boolean CheckingStrings(String str)//проверяем строки в suppressions.xml и смотрим есть ли в них suppress files=
    {
        Pattern p = Pattern.compile(" *<suppress files=.+");
        Matcher m = p.matcher(str);
        return m.matches();
    }


    public void fixPath()
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
    }

    public void getPath()//забираем готовый путь в arrayList
    {
        String str="<suppress files=";
        StringBuilder path;
        int counter;
        int startingSize = paths.size();

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
    }

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
