import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindingFilesWithoutRegExp implements SuppressionChecker
{

    private List<String> paths = new ArrayList<>();
    private List<String> FileSystem= new ArrayList<>();
    private final String developerName="VitSaf";
    static
    {
        Registration.registration(new FindingFilesWithoutRegExp());
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
                str = reader.readLine().replaceAll("\\\\","/");

                int k = 0;
                int startingPoint = 0;
                int finishingPoint = 0;

                String str1;
                for (int j = 0; j < str.length(); j++)
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
            int condition = ArgsTest.linesCounter(suppressionsFilename);

            int i=0;
            while (i<condition)//???????
            {
                str=reader.readLine();
                if(CheckingStrings(str)) paths.add(str);
                i++;
            }
            reader.close();

            dir("AllFiles.txt");
            fixPath();//исправление [/\\] на /
            getPath();// извлечение пути к файлу
            getClassNameFromXML();
            findDeletedFiles(paths,FileSystem);


        }
        catch (IOException ex)
        {System.out.println(ex.getMessage());}
        return paths;
    }

    public void findDeletedFiles(List<String> sup,List<String> dir)
    {
        List<String> list = new ArrayList<>();
        int x=0;
        int j;
        int counter;
        for(int i=0;i<sup.size();i++)
        {
            counter=0;
            for (j = 0; j < dir.size(); j++)
            {
                if (sup.get(i).equals(dir.get(j))) {
                    System.out.println((x++)+"/1928 "+ dir.get(j) + " exist");
                    counter++;
                    break;
                }
                if((j==dir.size()-1)&&(counter!=dir.size()-1))//проверка отсутствия файла
                {
                    System.out.println("File "+sup.get(i)+" doesn't exist");
                    list.add(sup.get(i));
                }
            }

        }
        System.out.println("------------------------------------------------------");
        System.out.println("Deleted files:");
        for(String e:list)
            System.out.println(e);

//14000*1900=26600000 итераций=1500-1800ms
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
		{
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

    public static boolean CheckingStrings(String str)
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
        for (int i=0;i<startingSize;i++)
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

    public void getPath()//а тут???
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

    /*public static void main(String[] args)
    {
        long timeStart = System.currentTimeMillis() ;
        FindingFilesWithoutRegExp t1=new FindingFilesWithoutRegExp();
        t1.parseSuppression("supp.xml");
        long finishTime =System.currentTimeMillis() ;
        System.out.println("Время работы программы "+ (finishTime-timeStart)+"ms");
    }*/

}
