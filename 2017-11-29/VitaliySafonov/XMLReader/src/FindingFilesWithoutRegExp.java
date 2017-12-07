import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindingFilesWithoutRegExp
{
    private List<String> paths = new ArrayList<>();
    private List<String> FileSystem= new ArrayList<>();





    public void getFileSystem(String filename)
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
                        //while (str.charAt(k) != (char) 92)
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

    }

    public void readFile(String suppressionsFilename,String FSystFilename)//получение информации о файлах
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

            getFileSystem(FSystFilename);

            fixPath();//исправление [/\\] на /

            getPath();// извлечение пути к файлу

            //makeRegExp();
            //check();
            getClassNameFromXML();
            findExistingFiles();

        }
        catch (IOException ex)
        {System.out.println(ex.getMessage());}
    }

    public void findExistingFiles()
    {

        int x=0;
        int j;
        int counter;
        for(int i=0;i<paths.size();i++)
        {
            counter=0;
            for (j = 0; j < FileSystem.size(); j++)
                if (paths.get(i).equals(FileSystem.get(j)))
                {
                    System.out.println((x++)+FileSystem.get(j) + " exist");
                    FileSystem.remove(j);
                    counter++;
                    break;
                }
                if(counter==FileSystem.size()-1)System.out.println("File "+paths.get(i)+" doesn't exist");
        }
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
                    if(k==0)break;
        		}
        	}
		}
		paths2.add(str1.substring(startingPoint,finishingPoint)+".java");
    }
    paths.clear();
    for(String e:paths2)
        paths.add(e);

}


    /*public void makeRegExp()
    {
        Iterator<String> iterator = FileSystem.iterator();

        Matcher m;
        Pattern p;
        String str;



        for(int i=0;i<paths.size();i++)
        {
            str= ".+";
            str+=paths.get(i)+".+";

           //System.out.println("RegExp="+str);//Как составить правильно регулярное выражение???

            //p = Pattern.compile(paths.get(i));//Не находит??????
            p = Pattern.compile(str);
            while(iterator.hasNext())
            {
                m = p.matcher(iterator.next());
                if(m.matches()) {System.out.println(iterator.next()+" exist");}
            }

        }

    }*/



    /*public void check()
    {
        Iterator<String> iterator = FileSystem.iterator();
        String str;
        int j=0;

        for(int i=0;i<paths.size();i++)
        {
            //str="E:/work/projects/InterTrust/CM4-gwt/workspaces/dev-cmj-4.3"+paths.get(i);
            //System.out.println("RegExp="+str);//Как составить правильно регулярное выражение???

            while(iterator.hasNext())
            {
                //System.out.println("iterator="+iterator.next());
               if (str.equals(iterator.next()))

                    System.out.println((j++)+iterator.next()+" exist");
            }

        }
    }*/

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

    public static void main(String[] args)
    {
        long timeStart = System.currentTimeMillis() ;
        FindingFilesWithoutRegExp t1=new FindingFilesWithoutRegExp();
        t1.readFile("supp.xml","AllFiles.txt");
        long finishTime =System.currentTimeMillis() ;
        System.out.println("Время работы программы "+ (finishTime-timeStart)+"ms");
    }

}
